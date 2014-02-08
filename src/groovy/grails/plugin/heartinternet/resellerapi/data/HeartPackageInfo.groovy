package grails.plugin.heartinternet.resellerapi.data

import grails.validation.Validateable

@Validateable
class HeartPackageInfo {

	String      packageId
	String      roid
	String      status
	String      statusDescription
	Date        addedDate
	Date        updatedDate
	String      packageType
	String[]    domainNames

	static constraints = {
	}

}
