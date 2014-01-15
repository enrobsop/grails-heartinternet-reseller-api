package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class ListInvoicesRequest implements ApiRequest {

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:ext-billing="http://www.heartinternet.co.uk/whapi/ext-billing-2.0">
  <extension>
    <ext-billing:listInvoices/>
    <ext-whapi:clTRID xmlns:ext-whapi="http://www.heartinternet.co.uk/whapi/ext-whapi-2.0">938d1139bfd358cfa3d6439dc9c64da9</ext-whapi:clTRID>
  </extension>
</epp>
""".trim()
	}

}

