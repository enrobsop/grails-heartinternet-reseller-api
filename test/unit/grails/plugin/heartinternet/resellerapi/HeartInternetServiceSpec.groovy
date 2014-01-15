package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.ListDomainsRequest
import grails.plugin.heartinternet.resellerapi.request.ListInvoicesRequest
import grails.plugin.heartinternet.resellerapi.request.ListPackagesRequest
import grails.plugin.heartinternet.resellerapi.request.LogoutRequest
import grails.plugin.spock.UnitSpec
import grails.test.mixin.TestFor

import java.text.SimpleDateFormat

@TestFor(HeartInternetService)
class HeartInternetServiceSpec extends UnitSpec {

	def dateFormat = new SimpleDateFormat("yyyy-MM-dd")

	def api

	def setup() {
		api = Mock(EppClient)
		service.eppClient = api
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

	static final def LOGOUT_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0">
  <response>
    <result code='1500'>
      <msg>Command completed successfully</msg>
    </result>
    <trID>
      <clTRID>90908b2caabbb97c1e79899816efc093</clTRID>
      <svTRID>test-673076c2a80828e53c296681f29b5eaf</svTRID>
    </trID>
  </response>
</epp>
""".trim())

	static final def LIST_DOMAINS_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-domain="http://www.heartinternet.co.uk/whapi/ext-domain-2.0" xmlns:domain="urn:ietf:params:xml:ns:domain-1.0">
  <response>
    <result code='1000'>
      <msg>Command completed successfully</msg>
    </result>
    <resData>
      <ext-domain:lstData>
        <ext-domain:domainInfo hosted='1'>foo.example.org</ext-domain:domainInfo>
        <ext-domain:domainInfo>bez.example.org</ext-domain:domainInfo>
      </ext-domain:lstData>
    </resData>
    <trID>
      <clTRID>cff2cad609661333bad93296ecdd60c7</clTRID>
      <svTRID>test-e52f4ce3fbf33ba0d5802769f073108c</svTRID>
    </trID>
  </response>
</epp>
""".trim())

	static final def LIST_PACKAGES_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-package="http://www.heartinternet.co.uk/whapi/ext-package-2.0">
  <response>
    <result code='1000'>
      <msg>Command completed successfully</msg>
    </result>
    <resData>
      <ext-package:lstData>
        <ext-package:package>
          <ext-package:id>3e50664779a66336</ext-package:id>
          <ext-package:domainName>foo.example.org</ext-package:domainName>
        </ext-package:package>
        <ext-package:package>
          <ext-package:id>bb00181b84305c57</ext-package:id>
          <ext-package:domainName>bar.example.org</ext-package:domainName>
        </ext-package:package>
        <ext-package:package>
          <ext-package:id>308142b49153f743</ext-package:id>
          <ext-package:domainName>boo.example.org</ext-package:domainName>
        </ext-package:package>
      </ext-package:lstData>
    </resData>
    <trID>
      <clTRID>b57b7ac295ac79664fe5176761b35529</clTRID>
      <svTRID>test-7635e02f43ac2d1538e1b5a5ed1434ea</svTRID>
    </trID>
  </response>
</epp>
""".trim())

	static final def LIST_INVOICES_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-billing="http://www.heartinternet.co.uk/whapi/ext-billing-2.0">
  <response>
    <result code='1000'>
      <msg>Command completed successfully</msg>
    </result>
    <resData>
      <ext-billing:lstData>
        <ext-billing:invoice id='101230423' dateOrdered='2000-12-31'>
          <ext-billing:price exVAT='100.00' incVAT='117.5'/>
        </ext-billing:invoice>
        <ext-billing:invoice id='101530201' dateOrdered='2009-01-01'>
          <ext-billing:price exVAT='100.00' incVAT='115.0'/>
        </ext-billing:invoice>
      </ext-billing:lstData>
    </resData>
    <trID>
      <clTRID>938d1139bfd358cfa3d6439dc9c64da9</clTRID>
      <svTRID>test-18de054d4a734313bd79f46c7d325881</svTRID>
    </trID>
  </response>
</epp>
""".trim())

}
