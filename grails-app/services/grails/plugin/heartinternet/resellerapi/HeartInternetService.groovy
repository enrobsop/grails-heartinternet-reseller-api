package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.data.HeartDomain
import grails.plugin.heartinternet.resellerapi.data.HeartDomainInfo
import grails.plugin.heartinternet.resellerapi.data.HeartInvoice
import grails.plugin.heartinternet.resellerapi.data.HeartPackage
import grails.plugin.heartinternet.resellerapi.data.HeartPackageInfo
import grails.plugin.heartinternet.resellerapi.data.HeartPackageType
import grails.plugin.heartinternet.resellerapi.request.GetDomainInfoRequest
import grails.plugin.heartinternet.resellerapi.request.GetPackageInfoRequest
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
		xml = xml.declareNamespace('ext-domain': "http://www.heartinternet.co.uk/whapi/ext-domain-2.1")

		def domains = xml.response.resData.'ext-domain:lstData'.'ext-domain:domainInfo'
		domains.collect {
			new HeartDomain(
				name:       it.text(),
				isHosted:   it.@hosted.text() == "1"
			)
		}
	}

	HeartDomainInfo getDomainInfo(String domainName) {
		def xml = send(new GetDomainInfoRequest(domainName: domainName))
		xml = xml.declareNamespace('domain':        "urn:ietf:params:xml:ns:domain-1.0")
		xml = xml.declareNamespace('ext-domain':    "http://www.heartinternet.co.uk/whapi/ext-domain-2.1")

		def domainInfo      = xml.response.resData.'domain:infData'
		def domainInfoExtra = xml.response.extension.'ext-domain:infData'
		new HeartDomainInfo(
			domainName:         domainInfo."domain:name",
			roid:               domainInfo."domain:roid",
			status:             domainInfo."domain:status".@s,
			statusDescription:  domainInfo."domain:status".text(),
			registrant:         domainInfo."domain:registrant",
			contact:            domainInfo."domain:contact".text(),
			contactType:        domainInfo."domain:contact".@type,
			nameservers:        domainInfo."domain:ns"."domain:hostAttr"*."domain:hostName"*.text(),
			createdDate:        tryParseIsoDate(domainInfo."domain:crDate".text()),
			expiryDate:         tryParseIsoDate(domainInfo."domain:exDate".text()),
			authInfoPw:         domainInfo."domain:authInfo"."domain:pw".text(),
			extStatus:          domainInfoExtra."ext-domain:extStatus".text(),
			hasPrivacy:         domainInfoExtra."ext-domain:privacy".size() > 0,
			hostingPackage:     domainInfoExtra."ext-domain:package".text(),
		)
	}

	private Date tryParseIsoDate(str) {
		try {
			return DateHelper.isoDate(str)
		} catch (Exception e) {
			log.error(e.message, e)
		}
		null
	}

	List<HeartPackageType> listPackageTypes() {
		def xml = send(new ListPackageTypesRequest())
		xml = xml.declareNamespace('ext-package':"http://www.heartinternet.co.uk/whapi/ext-package-2.1")

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
		xml = xml.declareNamespace('ext-package':"http://www.heartinternet.co.uk/whapi/ext-package-2.1")

		def packages = xml.response.resData.'ext-package:lstData'.'ext-package:package'
		packages.collect {
			new HeartPackage(
				heartId:    it.'ext-package:id'.text(),
				domainName: it.'ext-package:domainName'.text()
			)
		}
	}

	HeartPackageInfo getPackageInfo(String packageId) {
		def xml = send(new GetPackageInfoRequest(packageId: packageId))
		xml = xml.declareNamespace('package':"http://www.heartinternet.co.uk/whapi/package-2.1")

		def packageInfo = xml.response.resData.'package:infData'
		new HeartPackageInfo(
			packageId:          packageInfo."package:id",
			roid:               packageInfo."package:roid",
			status:             packageInfo."package:status".@s,
			statusDescription:  packageInfo."package:status".text(),

			addedDate:	    tryParseIsoDate(packageInfo."package:detail".@addedDate.text()),
			updatedDate:	tryParseIsoDate(packageInfo."package:detail".@updatedDate.text()),
			packageType:	packageInfo."package:detail"."package:type".text(),
			domainNames:	packageInfo."package:domainName"*.text()

		)
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
