package grails.plugin.heartinternet.resellerapi

import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

class EppClientSpec extends UnitSpec {

	def dummyClient

	def setup() {
		dummyClient = new EppClient(
				host:   "api.heartinternet.co.uk",
				port:   1701,
				clID:   "aaaaaaaaaaaaaaaa",
				pw:     "++++++++++"
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

	def "closing a connection works correctly"() {

		given: "a client with an open connection"
		def connection = Mock(Socket)
		dummyClient.connection = connection

		when: "closing the connection"
		dummyClient.closeConnection()

		then: "the connection closes correctly"
		1 * connection.close()
		dummyClient.connection == null

	}

	@Unroll("the correct connection status is given when #status")
	def "the correct connection status is given"() {

		given: "a connection"
		def connection = exists ? Mock(Socket) : null
		dummyClient.connection = connection

		and: "connection state"
		connection.isClosed()           >> isClosed
		connection.isConnected()        >> isConnected
		connection.isInputShutdown()    >> isInputShutdown
		connection.isOutputShutdown()   >> isOutputShutdown

		when: "getting the status"
		def status = dummyClient.connectionStatus
		println status

		then: "the status is correct"
		status                  != null
		status.exists           == exists
		status.isClosed         == isClosed
		status.isConnected      == isConnected
		status.isInputShutdown  == isInputShutdown
		status.isOutputShutdown == isOutputShutdown
		status.isReady          == isOkay
		dummyClient.ready       == isOkay

		where:
		scenario                        | exists    | isClosed  | isConnected   | isInputShutdown   | isOutputShutdown  | isOkay
		"fully open"                    | true      | false     | true          | false             | false             | true
		"connection not open"           | false     | null      | null          | null              | null              | false
		"connection open but in closed" | true      | false     | true          | true              | false             | false
		"connection closed"             | true      | true      | true          | true              | false             | false

	}

	def "attempting to send while the connection is not ready should result in an exception"() {
		given: "a request"
		def theRequest = Mock(ApiRequest)

		when:
		dummyClient.send(theRequest)

		then:
		thrown EppClientException
	}

}