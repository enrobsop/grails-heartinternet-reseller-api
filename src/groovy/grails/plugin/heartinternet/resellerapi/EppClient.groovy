package grails.plugin.heartinternet.resellerapi

import javax.net.ssl.SSLSocketFactory

class EppClient {

	def host
	def port
	def clID
	def pw

	Socket stream

	void openStream() {
		stream = SSLSocketFactory.default.createSocket(host, port)
	}

}
