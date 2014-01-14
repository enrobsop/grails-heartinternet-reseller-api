package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class ListPackagesRequest implements ApiRequest {

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-package="http://www.heartinternet.co.uk/whapi/ext-package-2.0">
  <extension>
    <ext-package:listTypes/>
    <ext-whapi:clTRID xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0">82562e1830f07de8e8913cb894efd6b5</ext-whapi:clTRID>
  </extension>
</epp>
""".trim()
	}

	@Override
	def handleResponse(Object text) {
		return null
	}

}

