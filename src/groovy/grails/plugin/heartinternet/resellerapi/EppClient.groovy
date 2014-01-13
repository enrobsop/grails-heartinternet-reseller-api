package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.EppClientHelper
import grails.plugin.heartinternet.resellerapi.request.LoginRequest

import javax.net.ssl.SSLSocketFactory

@Mixin(EppClientHelper)
class EppClient {

	def host
	def port
	def clID
	def pw

	Socket connection
	private PrintWriter writer
	private InputStream inputStream

	def connect() {
		connection?.close()
		connection  = SSLSocketFactory.default.createSocket(host, port)
		writer      = new PrintWriter(connection.outputStream, true)
		inputStream = connection.inputStream
		println "Connected to: $host on port $port"
		response
	}

	void closeConnection() {
		connection?.close()
		connection = null
	}

	def login() {
		send(new LoginRequest(clID: clID, password: pw))
		response
	}

	def getConnectionStatus() {
		[
				isReady:            connection && !connection.closed && connection.connected && !connection.inputShutdown && !connection.outputShutdown,
				exists:             connection != null,
				isClosed:           connection?.closed,
				isConnected:        connection?.connected,
				isInputShutdown:    connection?.inputShutdown,
				isOutputShutdown:   connection?.outputShutdown,
				writerError:        writer?.checkError()
		]
	}

	boolean isReady() {
		connectionStatus.isReady
	}

	void send(ApiRequest request) {
		sendData request.message
	}

	void sendData(String message) {
		handleConnectionNotReady()
		def prepared = prepareForEpp(message)
		println "\nSending...\n[$prepared]\n"
		writer.println(prepared)
		println "Sending complete."
	}

	String getResponse() {
		print "Receiving..."

		int dataSize = incomingBytesAvailable
		print " $dataSize bytes... "

		byte[] data = new byte[dataSize]
		inputStream.read(data, 4, dataSize)
		def received = new String(data, 'UTF-8')

		println " complete.\n[$received]\n\n"
		received.trim()
	}

	private int getIncomingBytesAvailable() {
		byte[] initBytes = new byte[4]
		inputStream.read(initBytes,0,4)
		unpackN(new String(initBytes, 'UTF-8'))
	}

	private void handleConnectionNotReady() {
		if (!ready) {
			throw new EppClientException("EppClient is not ready. Has the connection been opened using EppClient.connect? Connection status=$connectionStatus", null)
		}
	}

}