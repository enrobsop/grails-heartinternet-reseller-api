package grails.plugin.heartinternet.resellerapi;

public class SampleXml {

	static final def LOGIN_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0">
  <response>
    <result code='1000'>
      <msg>Command completed successfully</msg>
    </result>
    <extension>
      <ext-whapi:sessionExpiry xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0" unit="s">600</ext-whapi:sessionExpiry>
    </extension>
    <trID>
      <clTRID>1b8257ac1c3d2ee9d667a252cfe23373</clTRID>
      <svTRID>test-97384f32ba0a56cf40fd047a5b01e39b</svTRID>
    </trID>
  </response>
</epp>
""".trim())

	static final def SOME_XML = LOGIN_XML

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
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-domain="http://www.heartinternet.co.uk/whapi/ext-domain-2.1" xmlns:domain="urn:ietf:params:xml:ns:domain-1.0">
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

	static final def LIST_PACKAGE_TYPES_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-package="http://www.heartinternet.co.uk/whapi/ext-package-2.0">
  <response>
    <result code='1000'>
      <msg>Command completed successfully</msg>
    </result>
    <resData>
      <ext-package:lstData>
        <ext-package:packageType id='63b3d8d7a1383273' serverType='linux'>Gold Package</ext-package:packageType>
        <ext-package:packageType id='3b2db89769d20c0d' serverType='windows'>Silver Package</ext-package:packageType>
        <ext-package:packageType id='d646b5a8b964f8c6' serverType='linux'>My Custom Config</ext-package:packageType>
      </ext-package:lstData>
    </resData>
    <trID>
      <clTRID>82562e1830f07de8e8913cb894efd6b5</clTRID>
      <svTRID>test-0ba2a26d4d9c5b44a8b268f102a71fc3</svTRID>
    </trID>
  </response>
</epp>
""".trim())

	static final def DOMAIN_INFO_XML = new XmlSlurper().parseText("""
<?xml version='1.0'?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0">
  <response>
    <result code='1000'>
      <msg>Command completed successfully</msg>
    </result>
    <resData>
      <domain:infData xmlns:domain="urn:ietf:params:xml:ns:domain-1.0">
        <domain:name>example.org</domain:name>
        <domain:roid>B4C2639A2CAB62E6-HI</domain:roid>
        <domain:status s="ok">ok</domain:status>
        <domain:registrant>de9c9da15eec8961</domain:registrant>
        <domain:contact type="admin">d95d20725a673156</domain:contact>
        <domain:ns>
          <domain:hostAttr>
            <domain:hostName>ns.domain.com</domain:hostName>
          </domain:hostAttr>
          <domain:hostAttr>
            <domain:hostName>ns2.domain.com</domain:hostName>
          </domain:hostAttr>
        </domain:ns>
        <domain:clID>20217eb4bf7f0414</domain:clID>
        <domain:crDate>2000-01-01T09:34:55</domain:crDate>
        <domain:exDate>2011-03-13T00:00:00</domain:exDate>
        <domain:authInfo>
          <domain:pw>9N+uh1Sa</domain:pw>
        </domain:authInfo>
      </domain:infData>
    </resData>
    <extension>
      <ext-domain:infData xmlns:ext-domain="http://www.heartinternet.co.uk/whapi/ext-domain-2.1">
        <ext-domain:extStatus>notRegisteredHere</ext-domain:extStatus>
        <ext-domain:privacy/>
        <ext-domain:package>2e3cc0422afb0503</ext-domain:package>
      </ext-domain:infData>
    </extension>
    <trID>
      <clTRID>1f70c2f01bfc883b12e12bbf721cb462</clTRID>
      <svTRID>test-eca3ec96408bd0a2e8e0b5738f5becaf</svTRID>
    </trID>
  </response>
</epp>
""".trim())

}
