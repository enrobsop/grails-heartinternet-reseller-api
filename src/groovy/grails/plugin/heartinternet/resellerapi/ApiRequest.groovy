package grails.plugin.heartinternet.resellerapi

abstract class ApiRequest {

	String clTRID = "${this.getClass().simpleName}-${System.currentTimeMillis()}"

	abstract String getMessage()

}
