package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class LogoutRequest extends ApiRequest {

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0">
  <command>
    <logout />
    <clTRID>${clTRID}</clTRID>
  </command>
</epp>
""".trim()
	}

}

