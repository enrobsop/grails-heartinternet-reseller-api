package grails.plugin.heartinternet.resellerapi

import java.nio.channels.SocketChannel

class EppClient {

	def host
	def port
	def clID
	def pw

	SocketChannel connection

	void connect() {
		connection?.close()
		connection = SocketChannel.open()
		connection.connect(new InetSocketAddress(host, port))
	}

	void closeConnection() {
		connection?.close()
		connection = null
	}

	def getConnectionStatus() {
		[
			isReady:            connection && connection.isOpen() && connection.connected,
			exists:             connection != null,
			isOpen:             connection?.isOpen(),
			isConnected:        connection?.connected
		]
	}

	boolean isReady() {
		connectionStatus.isReady
	}

	def send(ApiRequest request) {
		println "------------------------"
		sendData(request.message)
		println " ------"
		def received = receiveFromStream()
		println "------------------------"
		request.handleResponse(received)
	}

	private void sendData(message) {
		handleConnectionNotReady()
		println "Sending...\n$message"
		// TODO send message
		println "Sending complete."
	}

	private String receiveFromStream() {
		println "Receiving..."
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
