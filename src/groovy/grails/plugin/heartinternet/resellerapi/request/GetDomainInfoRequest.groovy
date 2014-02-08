package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class GetDomainInfoRequest extends ApiRequest {

	String domainName

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0">
  <command>
    <info>
      <domain:info xmlns:domain="urn:ietf:params:xml:ns:domain-1.0">
        <domain:name>${domainName}</domain:name>
      </domain:info>
    </info>
    <extension>
      <ext-domain:info xmlns:ext-domain="http://www.heartinternet.co.uk/whapi/ext-domain-2.1"/>
    </extension>
    <clTRID>${clTRID}</clTRID>
  </command>
</epp>
""".trim()
	}

}

