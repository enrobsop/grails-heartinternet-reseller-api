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
		def received = receiveFromStream()
		println "------------------------"
		request.handleResponse(received)
	}

	private void sendData(message) {
		handleConnectionNotReady()
		println "Sending...\n$message"
		def sout = connection.outputStream
		sout.write(message.bytes)
		sout.flush()
		println "Sending complete."
	}

	private String receiveFromStream() {
		println "Receiving..."

		final int maxBytes = 1000

		def sin     = connection.inputStream
		int mark    = 0
		def results = []
		int n       = 0

		byte[] bytesRead = new byte[maxBytes]

		while ((n = sin.read(bytesRead, mark, maxBytes)) >= 0) {
			def str = new String(Arrays.copyOfRange(bytesRead,0,n), 'UTF-8')
			results << str
			mark += n
		}

		def received = results.join("").toString()
		println "Receiving complete.\n$received"
		received

	}

	private void handleConnectionNotReady() {
		if (!ready) {
			throw new EppClientException("EppClient is not ready. Has the connection been opened using EppClient.connect? Connection status=$connectionStatus", null)
		}
	}

}
