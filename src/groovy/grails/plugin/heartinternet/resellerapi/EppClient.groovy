package grails.plugin.heartinternet.resellerapi

import javax.net.ssl.SSLSocketFactory

class EppClient {

	def host
	def port
	def clID
	def pw

	Socket connection
	private BufferedReader reader
	private PrintWriter writer

	void connect() {
		connection?.close()
		connection  = SSLSocketFactory.default.createSocket(host, port)
		reader      = new BufferedReader(new InputStreamReader(connection.inputStream))
		writer      = writer = new PrintWriter(connection.outputStream, true)
		println "Connected to: $host on port $port"
	}

	void closeConnection() {
		connection?.close()
		connection = null
	}

	def ping() {
		sendData ""
		getResponse()
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
		println "Sending...\n[$message]\n$connectionStatus"

		writer.println(prepareForEpp(message))

		// TODO implement request sending
		println "Sending complete.\n$connectionStatus"
	}

	private String getResponse() {
		println "Receiving...\n$connectionStatus"

		def received = ""
		def line
		while (line = reader.readLine()) {
			println "\tline[line[$line]]"
			received += line
		}

		println "Receiving complete.\n[$received]\n$connectionStatus"
		received.trim()
	}

	private void handleConnectionNotReady() {
		if (!ready) {
			throw new EppClientException("EppClient is not ready. Has the connection been opened using EppClient.connect? Connection status=$connectionStatus", null)
		}
	}

	private String prepareForEpp(msg) {
		msg
	}

}
