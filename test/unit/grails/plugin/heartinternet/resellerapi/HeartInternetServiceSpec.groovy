package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.ListPackagesRequest
import grails.plugin.spock.UnitSpec
import grails.test.mixin.TestFor

@TestFor(HeartInternetService)
class HeartInternetServiceSpec extends UnitSpec {

	void "getting a list of packages works"() {

		given: "an API client"
		def api = Mock(EppClient)
		service.resellerEppClient = api

		when: "getting the list of pacakges"
		def result = service.listPackages()

		then: "the correct calls are made"
		1 * api.send(_ as ListPackagesRequest) >> api
		1 * api.getResponseAsXml() >> LIST_PACKAGES_XML

		and: "the correct packages are returned"
		result.size()       == 3
		result*.heartId     == ['3e50664779a66336','bb00181b84305c57','308142b49153f743']
		result*.domainName  == ['foo.example.org','bar.example.org','boo.example.org']


	}


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

}
