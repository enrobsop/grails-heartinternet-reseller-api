package grails.plugin.heartinternet.resellerapi

import grails.plugin.spock.UnitSpec

class EppClientSpec extends UnitSpec {

	def "it checks that config has been completed"() {
		when: "initialising with values"
			def theClient = new EppClient(
				host:   "api.somehost.co.uk",
				port:   1701,
				clID:   "clientId",
				pw:     "aPassword"
			)
		then: "the values are correctly set"
			theClient.host  == "api.somehost.co.uk"
			theClient.port  == 1701
			theClient.clID  == "clientId"
			theClient.pw    == "aPassword"
	}

}
