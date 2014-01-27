package grails.plugin.heartinternet.resellerapi

import org.apache.commons.lang.time.DateUtils
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import java.text.DateFormat
import java.text.SimpleDateFormat

class DateHelper {

	static final DateTimeFormatter ISO_PARSER = ISODateTimeFormat.dateTimeParser()
	static final DateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
	static final TimeZone UTC_TZ = TimeZone.getTimeZone("UTC")

	static final TZ_MATCHER = /(.*)\s+(\d{4})/
	static final String[] ALLOWED_DATE_PATTERNS = [
			"yyyy/MM/dd",
			"yyyy/MM/dd HH:mm",
			"yyyy/MM/dd HH:mm:ss",
			"yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd HH:mm:ss Z"
	]

	static def date(String str, allowedPatterns=ALLOWED_DATE_PATTERNS) {
		if (!str?.trim()) {
			return null
		}
		if (str.trim().endsWith("Z")) {
			return isoDate(str)
		}
		str = cleanIsoDateWithTimezone(str)
		DateUtils.parseDateStrictly(str, allowedPatterns)
	}

	static def isoDate(str) {
		ISO_PARSER.parseDateTime(str).toDate()
	}

	static String formatAsIso(date) {
		ISO_FORMAT.setTimeZone(UTC_TZ)
		ISO_FORMAT.format(date)
	}

	private static def cleanIsoDateWithTimezone(str) {
		def matcher = (str =~ TZ_MATCHER)
		if (matcher.matches()) {
			return "${matcher[0][1]} +${matcher[0][2]}"
		}
		str
	}


}
