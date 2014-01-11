package grails.plugin.heartinternet.resellerapi

interface ApiRequest {

	String getMessage()

	def handleResponse(text)

}
