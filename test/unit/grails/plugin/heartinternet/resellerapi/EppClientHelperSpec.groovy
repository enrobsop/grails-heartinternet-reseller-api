package grails.plugin.heartinternet.resellerapi

import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

import static grails.plugin.heartinternet.resellerapi.request.EppClientHelper.packN
import static grails.plugin.heartinternet.resellerapi.request.EppClientHelper.unpackN

class EppClientHelperSpec extends UnitSpec {

	@Unroll("packing #value should give #expectedResult")
	def "packN should work"() {

		expect: packN(value) == expectedResult

		where:
		value   | expectedResult
		100     | "\u0000\u0000\u0000d"
		123     | "\u0000\u0000\u0000{"
		1839    | "\u0000\u0000\u0007/"
		4862    | "\u0000\u0000\u0012\u00FE"

	}

	@Unroll("unpacking #value should give #expectedInt")
	def "unpacking works correctly"() {

		expect: unpackN(value) == expectedInt

		where:
		value                       | expectedInt
		"\u0000\u0000\u0012\u00FE"  | 4862
		"\u0000\u0000\u0000{"       | 123

	}

}