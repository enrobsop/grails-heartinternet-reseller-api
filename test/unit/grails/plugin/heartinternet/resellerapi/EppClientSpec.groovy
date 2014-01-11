package grails.plugin.heartinternet.resellerapi

import grails.plugin.spock.UnitSpec

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

	def "it checks that config has been completed"() {
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

}
