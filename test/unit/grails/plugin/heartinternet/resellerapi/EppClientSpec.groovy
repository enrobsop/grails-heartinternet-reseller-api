package grails.plugin.heartinternet.resellerapi
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

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

	def "it creates a stream correctly"() {

		when: "opening a stream"
			dummyClient.openStream()
			def theStream = dummyClient.stream

		then: "the stream is correct"
			theStream != null
			theStream.host == dummyClient.host
			theStream.port == dummyClient.port

	}

	def "closing a stream works correctly"() {

		given: "a client with an open stream"
			def stream = Mock(Socket)
			dummyClient.stream = stream

		when: "closing the stream"
			dummyClient.closeStream()

		then: "the stream closes correctly"
			1 * stream.close()
			dummyClient.stream == null

	}

	def "opening a second stream should close the first stream"() {

		given: "a client with an open stream"
			def firstStream = Mock(Socket)
			dummyClient.stream = firstStream

		when: "opening a second stream"
			dummyClient.openStream()

		then: "the first stream is closed"
			1 * firstStream.close()

	}

	@Unroll("the correct stream status is given when #status")
	def "the correct stream status is given"() {

		given: "a stream"
			def stream = exists ? Mock(Socket) : null
			dummyClient.stream = stream
		and: "stream state"
			stream.isClosed()           >> isClosed
			stream.isConnected()        >> isConnected
			stream.isInputShutdown()    >> isInputShutdown
			stream.isOutputShutdown()   >> isOutputShutdown

		when: "getting the status"
			def status = dummyClient.streamStatus
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
			"stream not open"               | false     | null      | null          | null              | null              | false
			"stream open but in closed"     | true      | false     | true          | true              | false             | false
			"stream closed"                 | true      | true      | true          | true              | false             | false

	}

	def "a request is executed correctly"() {

		given: "a request"
			def theRequest      = Mock(ApiRequest)
			def theTestMessage  = "<test>mymessage</test>"
			theRequest.getMessage() >> theTestMessage
		and: "a stream"
			def stream = mockReadyStream()
			dummyClient.stream = stream
		and: "with io streams"
			def outgoing = Mock(OutputStream)
			def incoming = Mock(InputStream)
			stream.getInputStream()     >> incoming
			stream.getOutputStream()    >> outgoing

		when: "it is executed"
			dummyClient.send(theRequest)

		then:
			1 * outgoing.write(theTestMessage.bytes)
			1 * outgoing.flush()
			3 * incoming.read(_, 0, 1000) >>> [0, 0, 5]
			1 * incoming.read(_, 5, 1000) >> 5
			1 * incoming.read(_, 10, 1000) >> 10
			1 * incoming.read(_, 20, 1000) >> 10
			1 * incoming.read(_, 30, 1000) >> 13
			1 * incoming.read(_, 43, 1000) >> -1
			1 * theRequest.handleResponse(_)

	}

	def "attempting to send while the stream is not ready should result in an exception"() {
		given: "a request"
			def theRequest = Mock(ApiRequest)

		when:
			dummyClient.send(theRequest)

		then:
			thrown EppClientException
	}

	private def mockReadyStream() {
		def stream = Mock(Socket)
		stream.isClosed()           >> false
		stream.isConnected()        >> true
		stream.isInputShutdown()    >> false
		stream.isOutputShutdown()   >> false
		stream
	}

}
