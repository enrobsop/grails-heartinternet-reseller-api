package grails.plugin.heartinternet.resellerapi

import javax.net.ssl.SSLSocketFactory

class EppClient {

	def host
	def port
	def clID
	def pw

	Socket connection

	void connect() {
		connection?.close()
		connection = SSLSocketFactory.default.createSocket(host, port)
	}

	void closeConnection() {
		connection?.close()
		connection = null
	}

	def getConnectionStatus() {
		[
			isReady:            connection && !connection.closed && connection.connected && !connection.inputShutdown && !connection.outputShutdown,
			exists:             connection != null,
			isClosed:           connection?.closed,
			isConnected:        connection?.connected,
			isInputShutdown:    connection?.inputShutdown,
			isOutputShutdown:   connection?.outputShutdown
		]
	}

	boolean isReady() {
		connectionStatus.isReady
	}

	def send(ApiRequest request) {
		println "------------------------"
		sendData(request.message)
		println " ------"
		def received = getResponse()
		println "------------------------"
		request.handleResponse(received)
	}

	private void sendData(message) {
		handleConnectionNotReady()
		println "Sending...\n$message"
		// TODO implement request sending
		println "Sending complete."
	}

	private String getResponse() {
		println "Receiving..."
		// TODO implement response reading.
		def received = ""
		println "Receiving complete.\n$received"
		received

	}

	private void handleConnectionNotReady() {
		if (!ready) {
			throw new EppClientException("EppClient is not ready. Has the connection been opened using EppClient.connect? Connection status=$connectionStatus", null)
		}
	}

}
