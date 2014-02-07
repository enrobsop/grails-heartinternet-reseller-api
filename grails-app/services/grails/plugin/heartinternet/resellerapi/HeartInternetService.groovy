package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.data.HeartDomain
import grails.plugin.heartinternet.resellerapi.data.HeartInvoice
import grails.plugin.heartinternet.resellerapi.data.HeartPackage
import grails.plugin.heartinternet.resellerapi.data.HeartPackageType
import grails.plugin.heartinternet.resellerapi.request.ListDomainsRequest
import grails.plugin.heartinternet.resellerapi.request.ListInvoicesRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackageTypesRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackagesRequest
import grails.plugin.heartinternet.resellerapi.request.LogoutRequest
import groovy.util.slurpersupport.GPathResult

import java.beans.Introspector
import java.text.SimpleDateFormat

class HeartInternetService {

	def grailsApplication
	EppClient eppClient
	boolean reuseClient = false // intended for testing to allow mock logins.

	private static final def LOGIN_LOGOUT_PATTERN = /^login(\p{Alnum}+)Logout/

	boolean login() {
		def clID    = grailsApplication.config.heartinternet.resellerapi.clID
		def pw      = grailsApplication.config.heartinternet.resellerapi.pw

		if (!reuseClient) {
			eppClient = new EppClient(
					host: grailsApplication.config.heartinternet.resellerapi.host ?: "api.heartinternet.co.uk",
					port: grailsApplication.config.heartinternet.resellerapi.port ?: 1701,
					clID: grailsApplication.config.heartinternet.resellerapi.clID,
					pw:   grailsApplication.config.heartinternet.resellerapi.pw
			)
		}
		eppClient.connect().login()

		def xml = eppClient.responseAsXml
		getResultCode(xml) == 1000
	}

	boolean logout() {
		def xml = send(new LogoutRequest())
		eppClient = null
		getResultCode(xml) == 1500
	}

	GPathResult sendWithResponseAsXml(ApiRequest request) {
		send(request)
	}

	String sendWithResponseAsText(ApiRequest request) {
		eppClient.send(request)
		eppClient.response
	}

	List<HeartDomain> listDomains(props=[:]) {
		def xml = send(new ListDomainsRequest(props))
		xml = xml.declareNamespace('ext-domain': "http://www.heartinternet.co.uk/whapi/ext-domain-2.0")

		def domains = xml.response.resData.'ext-domain:lstData'.'ext-domain:domainInfo'
		domains.collect {
			new HeartDomain(
				name:       it.text(),
				isHosted:   it.@hosted.text() == "1"
			)
		}
	}

	List<HeartPackageType> listPackageTypes() {
		def xml = send(new ListPackageTypesRequest())
		xml = xml.declareNamespace('ext-package':"http://www.heartinternet.co.uk/whapi/ext-package-2.0")

		def packageTypes = xml.response.resData.'ext-package:lstData'.'ext-package:packageType'
		packageTypes.collect {
			new HeartPackageType(
				heartId:    it.@id.text(),
				name:       it.text(),
				serverType: it.@serverType.text()
			)
		}
	}

	List<HeartPackage> listPackages() {
		def xml = send(new ListPackagesRequest())
		xml = xml.declareNamespace('ext-package':"http://www.heartinternet.co.uk/whapi/ext-package-2.0")

		def packages = xml.response.resData.'ext-package:lstData'.'ext-package:package'
		packages.collect {
			new HeartPackage(
				heartId:    it.'ext-package:id'.text(),
				domainName: it.'ext-package:domainName'.text()
			)
		}
	}

	List<HeartInvoice> listInvoices() {
		def xml = send(new ListInvoicesRequest())
		xml = xml.declareNamespace('ext-billing':"http://www.heartinternet.co.uk/whapi/ext-billing-2.0")

		def dateFmt = new SimpleDateFormat("yyyy-MM-dd")
		def invoices = xml.response.resData.'ext-billing:lstData'.'ext-billing:invoice'
		invoices.collect {
			def price = it.'ext-billing:price'
			new HeartInvoice(
				heartId:        it.@id.text(),
				dateOrdered:    dateFmt.parse(it.@dateOrdered.text()),
				priceExVat:     Float.parseFloat(price.@exVAT.text()),
				priceIncVat:    Float.parseFloat(price.@incVAT.text())
			)
		}
	}

	private def send(request) {
		eppClient.send(request).responseAsXml
	}

	private int getResultCode(xml) {
		xml ? XmlResponseHelper.getResult(xml).code : -1
	}

	def methodMissing(String name, args) {
		if( name ==~ LOGIN_LOGOUT_PATTERN) {
			return handleActionWrappedWithLoginAndLogout(name, args)
		}
	}

	private def handleActionWrappedWithLoginAndLogout(String name, args) {
		def matcher = name =~ LOGIN_LOGOUT_PATTERN
		def action = Introspector.decapitalize(matcher[0][1])
		login()
		def result
		if (args.length == 1) {
			result = this."${action}"(args[0])
		} else {
			result = this."${action}"()
		}
		logout()
		result
	}

}
