package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.EppClientHelper
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

class EppClientHelperSpec extends UnitSpec {

	@Unroll("packing #value should give #expectedResult")
	def "packN should work"() {
		expect:
		EppClientHelper.packN(value).bytes == expectedResult as byte[]

		where:
		value   | expectedResult
		1839    | [0,0,7,47]
		100     | [0,0,0,100]
		123     | [0,0,0,123]
	}

	@Unroll("unpacking #value should reverse packN")
	def "unpackN should reverse packN"() {
		when:
		def packed = EppClientHelper.packN(value)
		then:
		EppClientHelper.unpackN(packed) == value
		where:
		value << [1839, 100, 123]
	}

}