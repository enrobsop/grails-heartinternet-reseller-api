package grails.plugin.heartinternet.resellerapi
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

import java.nio.channels.SocketChannel

class EppClientSpec extends UnitSpec {

	def dummyClient

	def setup() {
		dummyClient = new EppClient(
			host:   "api.heartinternet.co.uk",
			port:   1701,
			clID:   "a0aa1234567aa998",
			pw:     "+epERjmIBb"
		)
	}

	def "it initialises correctly"() {
		when: "initialising with values"
			def theClient = new EppClient(
				host:   "api.somehost.co.uk",
				port:   1234,
				clID:   "clientId",
				pw:     "aPassword"
			)
		then: "the values are correctly set"
			theClient.host  == "api.somehost.co.uk"
			theClient.port  == 1234
			theClient.clID  == "clientId"
			theClient.pw    == "aPassword"
	}

	def "it connects correctly"() {

		when: "connecting to server"
			dummyClient.connect()
			def theConnection = dummyClient.connection

		then: "the connection is correct"
			theConnection != null
			theConnection.remoteAddress.hostName == dummyClient.host
			theConnection.remoteAddress.port == dummyClient.port

	}

	def "closing a connection works correctly"() {

		given: "a client with an open connection"
			def connection = Mock(SocketChannel)
			dummyClient.connection = connection

		when: "closing the connection"
			dummyClient.closeConnection()

		then: "the connection closes correctly"
			1 * connection.implCloseSelectableChannel()
			dummyClient.connection == null

	}

	def "making a second connection should close the first"() {

		given: "a client with an open connection"
			def firstConnection = Mock(SocketChannel)
			dummyClient.connection = firstConnection

		when: "opening a second connection"
			dummyClient.connect()

		then: "the first connection is closed"
			1 * firstConnection.implCloseSelectableChannel()

	}

	@Unroll("the correct connection status is given when #scenario")
	def "the correct connection status is given"() {

		given: "a connection"
			def connection = exists ? Mock(SocketChannel) : null
			dummyClient.connection = connection
		and: "connection state"
			connection.isOpen()             >> isOpen
			connection.isConnected()        >> isConnected

		when: "getting the status"
			def status = dummyClient.connectionStatus

		then: "the status is correct"
			status                  != null
			status.exists           == exists
			status.isOpen           == isOpen
			status.isConnected      == isConnected
			status.isReady          == isReady
			dummyClient.ready       == isReady

		where:
			scenario                | exists    | isOpen    | isConnected   | isReady
			"fully open"            | true      | true      | true          | true
			"connection not open"   | false     | null      | null          | false

	}

	def "a request is executed correctly"() {

		given: "a request"
			def theRequest      = Mock(ApiRequest)
			def theTestMessage  = "<test>mymessage</test>"
			theRequest.getMessage() >> theTestMessage
		and: "a connection"
			def connection = mockReadyConnection()
			dummyClient.connection = connection

		when: "it is executed"
			dummyClient.send(theRequest)

		then:
			// TODO add more interaction tests when implementation is better known.
			1 * theRequest.handleResponse(_)

	}

	def "attempting to send while the connection is not ready should result in an exception"() {
		given: "a request"
			def theRequest = Mock(ApiRequest)

		when:
			dummyClient.send(theRequest)

		then:
			thrown EppClientException
	}

	private def mockReadyConnection() {
		def connection = Mock(SocketChannel)
		connection.isOpen()             >> true
		connection.isConnected()        >> true
		connection
	}

}
