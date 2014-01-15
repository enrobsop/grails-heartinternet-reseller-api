package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class ListPackagesRequest implements ApiRequest {

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-package="http://www.heartinternet.co.uk/whapi/ext-package-2.0">
  <extension>
    <ext-package:list/>
    <ext-whapi:clTRID xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0">b57b7ac295ac79664fe5176761b35529</ext-whapi:clTRID>
  </extension>
</epp>
""".trim()
	}

}

