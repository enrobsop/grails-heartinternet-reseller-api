package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.LoginRequest

import javax.net.ssl.SSLSocketFactory

import static grails.plugin.heartinternet.resellerapi.EppClientHelper.prepareForEpp
import static grails.plugin.heartinternet.resellerapi.EppClientHelper.unpackN

@Mixin(EppClientHelper)
class EppClient {

	def host
	def port
	def clID
	def pw

	Socket connection
	private PrintWriter writer
	private InputStream inputStream
	private String lastResponse

	EppClient connect() {
		connection?.close()
		connection  = SSLSocketFactory.default.createSocket(host, port)
		writer      = new PrintWriter(connection.outputStream, true)
		inputStream = connection.inputStream
		println "Connected to: $host on port $port"
		lastResponse = readResponse()
		this
	}

	void closeConnection() {
		connection?.close()
		connection = null
	}

	EppClient login() {
		send(new LoginRequest(clID: clID, password: pw))
		this
	}

	String getResponse() {
		lastResponse
	}

	def getResponseAsXml() {
		response ? new XmlSlurper().parseText(response?.trim()) : null
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

	EppClient send(ApiRequest request) {
		sendData request.message
		this
	}

	EppClient sendData(String message) {
		handleConnectionNotReady()
		def prepared = prepareForEpp(message)
		print "\nSending...${prepared.length() -4}bytes...\n[$prepared]\n"
		writer.print(prepared)
		writer.flush()
		println "Sending complete."
		lastResponse = readResponse()
		this
	}

	private String readResponse() {
		print "Receiving..."

		int dataSize = incomingBytesAvailable
		print " $dataSize bytes... "

		def received = receive(dataSize)

		println " complete.\n[$received]\n\n"
		received.trim()
	}

	private String receive(int dataSize) {
		byte[] data = new byte[dataSize]
		inputStream.read(data, 0, dataSize)
		new String(data, 'UTF-8')
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