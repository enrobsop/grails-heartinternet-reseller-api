package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.GetDomainInfoRequest
import grails.plugin.heartinternet.resellerapi.request.GetPackageInfoRequest
import grails.plugin.heartinternet.resellerapi.request.ListDomainsRequest
import grails.plugin.heartinternet.resellerapi.request.ListInvoicesRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackageTypesRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackagesRequest
import grails.plugin.heartinternet.resellerapi.request.LogoutRequest
import grails.plugin.spock.UnitSpec
import grails.test.mixin.TestFor
import spock.lang.Unroll

import java.text.SimpleDateFormat

import static grails.plugin.heartinternet.resellerapi.SampleXml.*
import static grails.plugin.heartinternet.resellerapi.DateHelper.*

@TestFor(HeartInternetService)
class HeartInternetServiceSpec extends UnitSpec {

	def dateFormat = new SimpleDateFormat("yyyy-MM-dd")

	def api

	def setup() {
		api = Mock(EppClient)
		service.eppClient = api
		service.reuseClient = true
	}

	void "logging out works"() {

		when: "logging out"
		def result = service.logout()

		then: "the correct calls are made"
		1 * api.send(_ as LogoutRequest) >> api
		1 * api.getResponseAsXml() >> LOGOUT_XML

		and: "the correct result is given"
		result == true

		and: "the service is cleaned up"
		service.eppClient == null

	}

	void "getting a list of domains works"() {

		when: "getting the list of domains"
		def result = service.listDomains()

		then: "the correct calls are made"
		1 * api.send(_ as ListDomainsRequest) >> api
		1 * api.getResponseAsXml() >> LIST_DOMAINS_XML

		and: "the correct results are returned"
		result.size()       == 2
		result*.name        == ['foo.example.org','bez.example.org']
		result.isHosted     == [true, false]

	}

	void "getting domain info works"() {

		when: "getting info about a domain"
		def result = service.getDomainInfo("example.org")

		then: "the correct calls are made"
		1 * api.send({ it.domainName == "example.org" } as GetDomainInfoRequest) >> api
		1 * api.getResponseAsXml() >> DOMAIN_INFO_XML

		and: "the correct results are returned"
		result.domainName           == "example.org"
		result.roid                 == "B4C2639A2CAB62E6-HI"
		result.status               == "ok"
		result.statusDescription    == "ok"
		result.registrant           == "de9c9da15eec8961"
		result.contact              == "d95d20725a673156"
		result.contactType          == "admin"
		result.nameservers          == ["ns.domain.com", "ns2.domain.com"]
		result.createdDate          == isoDate("2000-01-01T09:34:55")
		result.expiryDate           == isoDate("2011-03-13T00:00:00")
		result.authInfoPw           == "9N+uh1Sa"
		result.extStatus            == "notRegisteredHere"
		result.hasPrivacy           == true
		result.hostingPackage       == "2e3cc0422afb0503"

	}

	void "getting a list of packages works"() {

		when: "getting the list of packages"
		def result = service.listPackages()

		then: "the correct calls are made"
		1 * api.send(_ as ListPackagesRequest) >> api
		1 * api.getResponseAsXml() >> LIST_PACKAGES_XML

		and: "the correct packages are returned"
		result.size()       == 3
		result*.heartId     == ['3e50664779a66336','bb00181b84305c57','308142b49153f743']
		result*.domainName  == ['foo.example.org','bar.example.org','boo.example.org']

	}

	def "getting package info works"() {

		when: "getting info about a domain"
		def result = service.getPackageInfo("thepackageid")

		then: "the correct calls are made"
		1 * api.send({ it.packageId == "thepackageid" } as GetPackageInfoRequest) >> api
		1 * api.getResponseAsXml() >> PACKAGE_INFO_XML

		and: "the correct result is returned"
		result.packageId            == "2e3cc0422afb0503"
		result.roid                 == "2E3CC0422AFB0503-HI"
		result.status               == "ok"
		result.statusDescription    == "ok"
		result.addedDate	== isoDate("2008-03-14T22:03:55")
		result.updatedDate	== isoDate("2009-09-21T19:35:22")
		result.packageType	== "0be9f5a18732b4c1"
		result.domainNames	== ["one.example.org", "three.example.org"]

	}

	void "getting a list of invoices works"() {

		when: "getting the list of invoices"
		def result = service.listInvoices()

		then: "the correct calls are made"
		1 * api.send(_ as ListInvoicesRequest) >> api
		1 * api.getResponseAsXml() >> LIST_INVOICES_XML

		and: "the correct invoices are returned"
		result.size()       == 2
		result*.heartId     == ['101230423','101530201']
		result*.dateOrdered == [dateFormat.parse('2000-12-31'),dateFormat.parse('2009-01-01')]
		result*.priceExVat  == [100, 100]
		result*.priceIncVat == [117.5, 115]

	}

	void "getting a list of package types works"() {

		when: "getting the list of package types"
		def result = service.listPackageTypes()

		then: "the correct calls are made"
		1 * api.send(_ as ListPackageTypesRequest) >> api
		1 * api.getResponseAsXml() >> LIST_PACKAGE_TYPES_XML

		and: "the correct types are returned"
		result.size()       == 3
		result*.heartId     == ['63b3d8d7a1383273','3b2db89769d20c0d','d646b5a8b964f8c6']
		result*.serverType  == ['linux','windows','linux']
		result*.name        == ['Gold Package','Silver Package','My Custom Config']

	}

	void "can send explicit ApiRequests"() {

		given: "a custom request"
		def theCustomRequest = new ApiRequest() {
			@Override
			String getMessage() {"<custom>request</custom>"}
		}
		and: "a response"
		def expectedTextResponse    = "<response>Custom Request Result</response>"
		api.send(theCustomRequest)  >> api
		api.getResponseAsXml()      >> new XmlSlurper().parseText(expectedTextResponse)
		api.getResponse()           >> expectedTextResponse

		when: "executing and expecting an XML repsonse"
		def xmlResult = service.sendWithResponseAsXml(theCustomRequest)
		then: "the correct xml is returned"
		xmlResult.text() == "Custom Request Result"

		when: "executing and expecting a text response"
		def strResult = service.sendWithResponseAsText(theCustomRequest)
		then: "the correctly text is returned"
		strResult == expectedTextResponse

	}

	@Unroll("can wrap #action with login and logout")
	void "can wrap existing actions with login and logout"() {

		given: "the expected order of xml responses"
			api.getResponseAsXml() >>> [LOGIN_XML, actionXmlResponse, LOGOUT_XML]

		when: "prefixing and suffixing a method call"
			service."login${action.capitalize()}Logout"()

		then: "the dynamic method is accepted"
			notThrown MissingMethodException
		and: "the correct calls are made"
			1 * api.connect() >> api
			1 * api.login()
			1 * api.send({it.getClass() == expectedRequestType}) >> api
			1 * api.send(_ as LogoutRequest) >> api

		where:
			action                  | actionXmlResponse         | expectedRequestType
			"listDomains"           | LIST_DOMAINS_XML          | ListDomainsRequest
			"listPackageTypes"      | LIST_PACKAGE_TYPES_XML    | ListPackageTypesRequest
			"listPackages"          | LIST_PACKAGES_XML         | ListPackagesRequest
			"listInvoices"          | LIST_INVOICES_XML         | ListInvoicesRequest
			"getDomainInfo"         | DOMAIN_INFO_XML           | GetDomainInfoRequest
			"getPackageInfo"        | PACKAGE_INFO_XML          | GetPackageInfoRequest

	}

	@Unroll("can wrap #action with login and logout")
	void "can wrap explicit actions with login and logout"() {

		given: "the expected order of xml responses"
			api.getResponseAsXml() >>> [LOGIN_XML, SOME_XML, LOGOUT_XML]
		and: "the explicit action"
			def theCustomApiRequest = new ApiRequest() {
				String getMessage() { "" }
			}

		when: "prefixing and suffixing a method call"
			service."login${action.capitalize()}Logout"(theCustomApiRequest)

		then: "the dynamic method is accepted"
			notThrown MissingMethodException
		and: "the correct calls are made"
			1 * api.connect() >> api
			1 * api.login()
			1 * api.send(theCustomApiRequest) >> api
			1 * api.send(_ as LogoutRequest) >> api

		where:
			action << ["sendWithResponseAsXml", "sendWithResponseAsText"]

	}

}
