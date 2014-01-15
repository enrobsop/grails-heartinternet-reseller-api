package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class ListDomainsRequest implements ApiRequest {

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-domain="http://www.heartinternet.co.uk/whapi/ext-domain-2.0" xmlns:domain="urn:ietf:params:xml:ns:domain-1.0">
  <extension>
    <ext-domain:list purpose="manage"/>
    <ext-whapi:clTRID xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0">cff2cad609661333bad93296ecdd60c7</ext-whapi:clTRID>
  </extension>
</epp>
""".trim()
	}

}

