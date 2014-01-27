package grails.plugin.heartinternet.resellerapi.data

import grails.validation.Validateable

@Validateable
class HeartDomain {

	String name
	boolean isHosted

	static constraints = {
	}

}
