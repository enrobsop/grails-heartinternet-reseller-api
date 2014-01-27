package grails.plugin.heartinternet.resellerapi.data
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

class HeartDomainSpec extends UnitSpec {

	@Unroll("should validate correctly when #scenario")
	def "class validates correctly"() {

		given:
			mockForConstraintsTests(HeartDomain)
			def theDomain = new HeartDomain()
			theDomain.with {
				name        = name
				isHosted    = isHosted
			}

		when: "validating"
			theDomain.validate()

		then:
			theDomain.hasErrors() != "$isValid"

		where:
			scenario                | name              | isHosted  | isValid
			"nothing set"           | null              | null      | false
			"fully set"             | "somewhere.com"   | true      | true
			"fully set, not hosted" | "somewhere.com"   | false     | true

	}

}
