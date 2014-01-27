package grails.plugin.heartinternet.resellerapi.data
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

class HeartPackageTypeSpec extends UnitSpec {

	@Unroll("should validate correctly when #scenario")
	def "class validates correctly"() {

		given:
			mockForConstraintsTests(HeartPackageType)
			def thePackageType = new HeartPackageType()
			thePackageType.with {
				heartId     = heartId
				name        = name
				serverType  = serverType
			}

		when: "validating"
			thePackageType.validate()

		then:
			thePackageType.hasErrors() != "$isValid"

		where:
			scenario                | heartId   | name      | serverType    | isValid
			"nothing set"           | null      | null      | null          | false
			"fully set"             | "ABC123"  | "BASIC"   | "Linux"       | true

	}

}
