package grails.plugin.heartinternet.resellerapi.data

import grails.plugin.heartinternet.resellerapi.DateHelper
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

@Mixin(DateHelper)
class HeartInvoiceSpec extends UnitSpec {

	@Unroll("should validate correctly when #scenario")
	def "class validates correctly"() {

		given:
			mockForConstraintsTests(HeartInvoice)
			def theInvoice = new HeartInvoice()
			theInvoice.with {
				heartId     = heartId
				dateOrdered = date(dateOrdered)
				priceIncVat = priceIncVat
				priceExVat  = priceExVat
		}

		when: "validating"
			theInvoice.validate()

		then:
			theInvoice.hasErrors() != "$isValid"

		where:
			scenario        | heartId           | dateOrdered   | priceIncVat   | priceExVat    | isValid
			"nothing set"   | null              | null          | null          | null          | false
			"all set"       | "ABC123"          | "2012/01/01"  | 24.00         | 20.00         | true

	}

}
