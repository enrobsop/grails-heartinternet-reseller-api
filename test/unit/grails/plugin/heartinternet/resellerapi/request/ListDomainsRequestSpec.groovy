package grails.plugin.heartinternet.resellerapi.request

import grails.plugin.spock.UnitSpec

class ListDomainsRequestSpec extends UnitSpec {

	def "the properties are correctly used in the generated message"() {

		when: "creating a request"
			def req = new ListDomainsRequest()
		and: "with custom values"
			req.with {
				purpose = "specialPurpose"
				clTRID  = "abc123"
			}

		then: "the custom values are used in the message"
			req.message.contains "purpose=\"specialPurpose\""
			req.message.contains ">abc123</ext-whapi:clTRID>"

	}

	def "the properties default sensibly"() {
		expect:
			new ListDomainsRequest().purpose == "manage"
			new ListDomainsRequest().clTRID ==~ /${(ListDomainsRequest.simpleName)}\-\d+/
	}

}
