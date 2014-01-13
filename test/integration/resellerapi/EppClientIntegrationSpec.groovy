package resellerapi
import grails.plugin.heartinternet.resellerapi.EppClient
import grails.plugin.spock.IntegrationSpec

class EppClientIntegrationSpec extends IntegrationSpec {

	def dummyClient

	def setup() {
		dummyClient = new EppClient(
				host:   "api.heartinternet.co.uk",
				port:   1701,
				clID:   "aaaaaaaaaaaaaaaa",
				pw:     "++++++++++"
		)
	}

	def "it connects correctly"() {

		when: "connecting to server"
		dummyClient.connect()
		def theConnection = dummyClient.connection

		then: "the connection is correct"
		theConnection != null
		theConnection.host == dummyClient.host
		theConnection.port == dummyClient.port

	}

	def "making a second connection should close the first"() {

		given: "a client with an open connection"
		def firstConnection = Mock(Socket)
		dummyClient.connection = firstConnection

		when: "opening a second connection"
		dummyClient.connect()

		then: "the first connection is closed"
		1 * firstConnection.close()

	}

	def "connecting returns a greeting message"() {
		when: "connecting"
		def response = dummyClient.connect()
		def xml = new XmlSlurper().parseText(response)

		then: "a valid xml response is received"
		xml != null
		xml.greeting?.svID?.text() == "Heart Internet Test EPP Service"
	}

	def "using the wrong login credentials gives an error response"() {
		given: "a request"
		dummyClient.connect()

		when: "logging in"
		def response = dummyClient.login()
		def xml = new XmlSlurper().parseText(response)

		then: "a login failed response is received"
		xml != null
		xml.response.result.@code.text()    == "2200"
		xml.response.result.msg.text()      == "Invalid clID or pw"
	}

}