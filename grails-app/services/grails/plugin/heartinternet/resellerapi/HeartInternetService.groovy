package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.ListDomainsRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackagesRequest
import grails.plugin.heartinternet.resellerapi.request.LogoutRequest

class HeartInternetService {

	def grailsApplication
	EppClient eppClient

	boolean login() {
		def clID    = grailsApplication.config.heartinternet.resellerapi.clID
		def pw      = grailsApplication.config.heartinternet.resellerapi.pw

		eppClient = new EppClient(
				host: grailsApplication.config.heartinternet.resellerapi.host ?: "api.heartinternet.co.uk",
				port: grailsApplication.config.heartinternet.resellerapi.port ?: 1701,
				clID: grailsApplication.config.heartinternet.resellerapi.clID,
				pw:   grailsApplication.config.heartinternet.resellerapi.pw
		).connect().login()

		def xml = eppClient.responseAsXml
		getResultCode(xml) == 1000
	}

	boolean logout() {
		def xml = send(new LogoutRequest())
		getResultCode(xml) == 1500
	}

	def listDomains() {
		def xml = send(new ListDomainsRequest())
		xml = xml.declareNamespace('ext-domain': "http://www.heartinternet.co.uk/whapi/ext-domain-2.0")

		def domains = xml.response.resData.'ext-domain:lstData'.'ext-domain:domainInfo'
		domains.collect {
			new HeartDomain(
				name:       it.text(),
				isHosted:   it.@hosted.text() == "1"
			)
		}
	}

	def listPackages() {
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

	private def send(request) {
		eppClient.send(request).responseAsXml
	}

	private int getResultCode(xml) {
		Integer.parseInt(xml.response?.result?.@code?.text())
	}

}
