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

}
