package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.ListDomainsRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackagesRequest

class HeartInternetService {

	def grailsApplication
	EppClient resellerEppClient

	boolean loginToResellerApi() {
		def clID    = grailsApplication.config.heartinternet.resellerapi.clID
		def pw      = grailsApplication.config.heartinternet.resellerapi.pw

		resellerEppClient = new EppClient(
				host: grailsApplication.config.heartinternet.resellerapi.host ?: "api.heartinternet.co.uk",
				port: grailsApplication.config.heartinternet.resellerapi.port ?: 1701,
				clID: grailsApplication.config.heartinternet.resellerapi.clID,
				pw:   grailsApplication.config.heartinternet.resellerapi.pw
		).connect().login()

		def xml = resellerEppClient.responseAsXml
		xml.epp.response.result.@code.text() == 1000
	}

	def listDomains() {
		def request = new ListDomainsRequest()
		def xml = resellerEppClient.send(request).responseAsXml
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
		def request = new ListPackagesRequest()
		def xml = resellerEppClient.send(request).responseAsXml
		xml = xml.declareNamespace('ext-package':"http://www.heartinternet.co.uk/whapi/ext-package-2.0")

		def packages = xml.response.resData.'ext-package:lstData'.'ext-package:package'
		packages.collect {
			new HeartPackage(
				heartId:    it.'ext-package:id'.text(),
				domainName: it.'ext-package:domainName'.text()
			)
		}
	}
}
