package grails.plugin.heartinternet.resellerapi

class XmlResponseHelper {

	static int getResultCode(xml) {
		getResult(xml).code
	}

	static String getResultMessage(xml) {
		getResult(xml).msg
	}

	static getResult(xml) {
		def result = xml.response?.result
		[
			code:   Integer.parseInt(result?.@code?.text()),
			msg:    result.text()
		]
	}

}
