package grails.plugin.heartinternet.resellerapi.request

import grails.plugin.spock.UnitSpec

class GetDomainInfoRequestSpec extends UnitSpec {

	def "the properties are correctly used in the generated message"() {

		when: "creating a request"
			def req = new GetDomainInfoRequest()
		and: "with custom values"
			req.with {
				domainName  = "thedomain.com"
				clTRID      = "abc123"
			}

		then: "the custom values are used in the message"
			req.message.contains "<domain:name>thedomain.com</domain:name>"
			req.message.contains "<clTRID>abc123</clTRID>"

	}

}
