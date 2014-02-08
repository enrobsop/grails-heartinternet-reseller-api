package grails.plugin.heartinternet.resellerapi.request
import grails.plugin.heartinternet.resellerapi.ApiRequest
import grails.plugin.heartinternet.resellerapi.XmlRequest

class LoginRequest extends ApiRequest {

	def clID
	def password

	@Override
	String getMessage() {
		def request = new XmlRequest()
		request.xml.epp(xmlns:"urn:ietf:params:xml:ns:epp-1.0") {
			command {
				login {
					clID(clID)
					pw(password)
					options {
						version("1.0")
						lang("en")
					}
					svcs {
						objURI("urn:ietf:params:xml:ns:contact-1.0")
						objURI("urn:ietf:params:xml:ns:domain-1.0")
						objURI("http://www.heartinternet.co.uk/whapi/database-2.0")
						objURI("http://www.heartinternet.co.uk/whapi/mailbox-2.0")
						objURI("http://www.heartinternet.co.uk/whapi/null-2.0")
						objURI("http://www.heartinternet.co.uk/whapi/offsite-package-2.1")
						objURI("http://www.heartinternet.co.uk/whapi/package-2.0")
						objURI("http://www.heartinternet.co.uk/whapi/server-2.0")
						objURI("http://www.heartinternet.co.uk/whapi/support-2.0")
						svcExtension {
							extURI("http://www.heartinternet.co.uk/whapi/ext-antivirus-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-billing-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-contact-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-database-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-dns-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-domain-2.1")
							extURI("http://www.heartinternet.co.uk/whapi/ext-host-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-mailbox-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-null-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-package-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-security-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-server-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-support-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-wbp-2.0")
							extURI("http://www.heartinternet.co.uk/whapi/ext-whapi-2.0")
						}
					}
				}
				clTRID("${clTRID}")
			}
		}
		request.toFlattenedString()
	}

}

