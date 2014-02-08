package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class ListPackagesRequest extends ApiRequest {

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-package="http://www.heartinternet.co.uk/whapi/ext-package-2.1">
  <extension>
    <ext-package:list/>
    <ext-whapi:clTRID xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0">${clTRID}</ext-whapi:clTRID>
  </extension>
</epp>
""".trim()
	}

}

