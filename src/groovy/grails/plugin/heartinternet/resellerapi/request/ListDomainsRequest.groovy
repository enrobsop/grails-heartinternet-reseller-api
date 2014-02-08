package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class ListDomainsRequest extends ApiRequest {

	String purpose = "manage"

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-domain="http://www.heartinternet.co.uk/whapi/ext-domain-2.1" xmlns:domain="urn:ietf:params:xml:ns:domain-1.0">
  <extension>
    <ext-domain:list purpose="${purpose}"/>
    <ext-whapi:clTRID xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0">${clTRID}</ext-whapi:clTRID>
  </extension>
</epp>
""".trim()
	}

}

