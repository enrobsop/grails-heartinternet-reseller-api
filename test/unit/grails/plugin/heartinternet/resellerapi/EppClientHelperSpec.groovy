package grails.plugin.heartinternet.resellerapi

import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

import static EppClientHelper.packN
import static EppClientHelper.unpackN

class EppClientHelperSpec extends UnitSpec {

	@Unroll("packing #value should give #expectedResult")
	def "packN should work"() {

		expect: packN(value) == expectedResult

		where:
		value   | expectedResult
		100     | "\u0000\u0000\u0000d"
		123     | "\u0000\u0000\u0000{"
		1839    | "\u0000\u0000\u0007/"
		1886    | "\u0000\u0000\u0007\u005E"
		4862    | "\u0000\u0012\u00FE"

	}

	@Unroll("unpacking #value should give #expectedInt")
	def "unpacking works correctly"() {

		expect: unpackN(value) == expectedInt

		where:
		value                       | expectedInt
		"\u0000\u0000\u0012\u00FE"  | 4862
		"\u0000\u0012\u00FE"        | 4862
		"\u0000\u0000\u0000{"       | 123

	}

	@Unroll("packN should return 4 bytes when n=#n")
	def "packN should always return 4 bytes"() {

		expect:
			packN(n).getBytes("UTF-8").length == 4

		where:
			n << [100, 150, 395, 296, 4862]

	}

}