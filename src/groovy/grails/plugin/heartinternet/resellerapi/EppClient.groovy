package grails.plugin.heartinternet.resellerapi

import javax.net.ssl.SSLSocketFactory

class EppClient {

	def host
	def port
	def clID
	def pw

	Socket stream

	void openStream() {
		stream?.close()
		stream = SSLSocketFactory.default.createSocket(host, port)
	}

	void closeStream() {
		stream?.close()
		stream = null
	}

	def getStreamStatus() {
		[
			isReady:            stream && !stream.closed && stream.connected && !stream.inputShutdown && !stream.outputShutdown,
			exists:             stream != null,
			isClosed:           stream?.closed,
			isConnected:        stream?.connected,
			isInputShutdown:    stream?.inputShutdown,
			isOutputShutdown:   stream?.outputShutdown
		]
	}

	boolean isReady() {
		streamStatus.isReady
	}

}
