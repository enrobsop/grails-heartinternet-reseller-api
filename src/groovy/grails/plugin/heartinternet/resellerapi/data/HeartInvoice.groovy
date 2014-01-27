package grails.plugin.heartinternet.resellerapi.data

import grails.validation.Validateable

@Validateable
class HeartInvoice {

	String heartId
	Date dateOrdered
	Float priceIncVat
	Float priceExVat

	static constraints = {
	}

}
