package grails.plugin.heartinternet.resellerapi.request

import grails.plugin.spock.UnitSpec

class GetPackageInfoRequestSpec extends UnitSpec {

	def "the properties are correctly used in the generated message"() {

		when: "creating a request"
			def req = new GetPackageInfoRequest()
		and: "with custom values"
			req.with {
				packageId   = "aaaabbbccc111222333"
				clTRID      = "abc123"
			}

		then: "the custom values are used in the message"
			req.message.contains "<package:id>aaaabbbccc111222333</package:id>"
			req.message.contains "<clTRID>abc123</clTRID>"

	}

}
