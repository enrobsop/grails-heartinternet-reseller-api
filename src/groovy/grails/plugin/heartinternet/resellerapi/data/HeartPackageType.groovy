package grails.plugin.heartinternet.resellerapi.data

import grails.validation.Validateable

@Validateable
class HeartPackageType {

	String heartId
	String name
	String serverType

	static constraints = {
	}

}
