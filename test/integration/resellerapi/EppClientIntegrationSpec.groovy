package resellerapi
import grails.plugin.heartinternet.resellerapi.EppClient
import grails.plugin.heartinternet.resellerapi.XmlResponseHelper
import grails.plugin.heartinternet.resellerapi.request.ListDomainsRequest
import grails.plugin.heartinternet.resellerapi.request.ListInvoicesRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackageTypesRequest
import grails.plugin.heartinternet.resellerapi.request.LogoutRequest
import grails.plugin.spock.UnitSpec

class EppClientIntegrationSpec extends UnitSpec {

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
		def xml = dummyClient.connect().responseAsXml

		then: "a valid xml response is received"
		xml != null
		xml.greeting?.svID?.text() == "Heart Internet Test EPP Service"
	}

	def "using the wrong login credentials gives an error response"() {
		when: "logging in"
		def xml = dummyClient.connect().login().responseAsXml

		then: "a login failed response is received"
		assertResult xml, 2200, "Invalid clID or pw"
	}

	def "listing domains should return a response"() {
		given: "client is connected but not logged in"
		dummyClient.connect()

		when: "requesting the list of domains"
		def xml = send(new ListDomainsRequest())

		then:
		assertIsCheckYourLogin xml
	}

	def "logging out returns a response"() {
		given: "client is connected but not logged in"
		dummyClient.connect()

		when: "a logout request is sent"
		def xml = send(new LogoutRequest())

		then: "a response is received"
		assertResult xml, 1500, "Logging you out"
	}

	def "listing invoices returns a response"() {
		given: "client is connected but not logged in"
		dummyClient.connect()

		when: "a list invoices request is sent"
		def xml = send(new ListInvoicesRequest())

		then: "a response is received"
		assertIsCheckYourLogin xml
	}

	def "list package types returns a response"() {
		given: "client is connected but not logged in"
		dummyClient.connect()

		when: "a list invoices request is sent"
		def xml = send(new ListPackageTypesRequest())

		then: "a response is received"
		assertIsCheckYourLogin xml
	}

	private def send(request) {
		dummyClient.send(request).responseAsXml
	}

	private void assertIsCheckYourLogin(xml) {
		assert xml != null
		def result = XmlResponseHelper.getResult(xml)
		assert result.code == 2101
		assert result.msg.contains("check your login")
	}

	private void assertResult(xml, expectedCode, expectedMsg) {
		assert xml != null
		def result = XmlResponseHelper.getResult(xml)
		assert result.code  == expectedCode
		assert result.msg   == expectedMsg
	}

}