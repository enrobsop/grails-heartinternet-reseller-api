package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest

class GetPackageInfoRequest extends ApiRequest {

	String packageId

	@Override
	String getMessage() {
		"""<?xml version="1.0"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0" xmlns:package="http://www.heartinternet.co.uk/whapi/package-2.1">
  <command>
    <info>
      <package:info>
        <package:id>${packageId}</package:id>
      </package:info>
    </info>
    <extension>
      <ext-package:info xmlns:ext-package="http://www.heartinternet.co.uk/whapi/ext-package-2.1">
        <ext-package:detail>mailbox</ext-package:detail>
      </ext-package:info>
    </extension>
    <clTRID>${clTRID}</clTRID>
  </command>
</epp>
""".trim()
	}

}

