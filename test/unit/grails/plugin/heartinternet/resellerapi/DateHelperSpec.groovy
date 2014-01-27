package grails.plugin.heartinternet.resellerapi
import grails.plugin.spock.UnitSpec
import spock.lang.Unroll

import java.text.SimpleDateFormat

class DateHelperSpec extends UnitSpec {

	static originalDefaultTimeZone

	def setupSpec() {
		originalDefaultTimeZone = TimeZone.default
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+0100"));
	}

	def cleanupSpec() {
		TimeZone.setDefault(originalDefaultTimeZone)
	}

	@Unroll("#theString should be parsed to #expectedDate")
	def "Can parse a string of unspecified date format to a date"() {
		given:
			def outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm")
		expect:
			outputFormat.format(DateHelper.date(theString)) == expectedDate
		where:
			theString				    | expectedDate
			"2012/01/01"			    | "01/01/2012 00:00"
			"2012/01/31 23:59:59"	    | "31/01/2012 23:59"
			"2012/01/31 23:59:59"	    | "31/01/2012 23:59"
			"2013-01-15T18:00:00Z"	    | "15/01/2013 19:00"
			"2013-09-28 19:23:11"	    | "28/09/2013 19:23"
			"2013-09-28 19:24:11 +0800"	| "28/09/2013 12:24"
			"2013-09-28 19:25:11 0800"	| "28/09/2013 12:25"
			"2013-09-28 19:26:11  0000"	| "28/09/2013 20:26"
	}

	def "attempting to parse a null date should return a null date"() {
		expect:	DateHelper.date(null) == null
	}

	def "can parse an ISO 8601 date"() {
		expect:
			DateHelper.isoDate("2010-08-31T15:23:00Z").format("dd/MM/yyyy HH:mm") == "31/08/2010 16:23"
	}

	def "can write an ISO date"() {
		given:
			def theDate = DateHelper.date("2012/06/01 23:59:59")
		expect:
			DateHelper.formatAsIso(theDate) == "2012-06-01T22:59:59Z"
	}

}
