package grails.plugin.heartinternet.resellerapi.data

import grails.validation.Validateable

@Validateable
class HeartDomainInfo {

	String domainName
	String roid
	String status
	String statusDescription
	String registrant
	String contact
	String contactType
	String[] nameservers
	Date createdDate
	Date expiryDate
	String authInfoPw
	String extStatus
	boolean hasPrivacy
	String hostingPackage

	static constraints = {
	}

}
