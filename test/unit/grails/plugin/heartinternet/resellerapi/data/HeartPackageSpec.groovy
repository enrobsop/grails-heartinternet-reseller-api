package grails.plugin.heartinternet.resellerapi.data
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

class HeartPackageSpec extends UnitSpec {

	@Unroll("should validate correctly when #scenario")
	def "class validates correctly"() {

		given:
			mockForConstraintsTests(HeartPackage)
			def thePackage = new HeartPackage()
			thePackage.with {
				heartId     = heartId
				domainName  = domainName
			}

		when: "validating"
			thePackage.validate()

		then:
			thePackage.hasErrors() != "$isValid"

		where:
			scenario                | heartId   | domainName        | isValid
			"nothing set"           | null      | null              | false
			"fully set"             | "ABC123"  | "something.com"   | true

	}

}
