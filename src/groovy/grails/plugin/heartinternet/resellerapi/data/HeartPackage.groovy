package grails.plugin.heartinternet.resellerapi.data

import grails.validation.Validateable

@Validateable
class HeartPackage {

	String heartId
	String domainName

	static constraints = {
	}

}
