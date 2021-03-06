package grails.plugin.heartinternet.resellerapi

import grails.plugin.heartinternet.resellerapi.request.LoginRequest
import groovy.util.logging.Log4j

import javax.net.ssl.SSLSocketFactory

import static grails.plugin.heartinternet.resellerapi.EppClientHelper.prepareForEpp
import static grails.plugin.heartinternet.resellerapi.EppClientHelper.unpackN

@Mixin(EppClientHelper)
@Log4j
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
		log.info "Connected to: $host on port $port"
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
		def flattened = XmlResponseHelper.flatten(response)
		log.debug "Flattening response: raw:${response.length()} chars, flattened:${flattened.length()} chars"
		response ? new XmlSlurper().parseText(flattened) : null
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
		log.debug "\nSending...${prepared.length() -4}bytes...\n[$prepared]\n"
		writer.print(prepared)
		writer.flush()
		log.debug "Sending complete."
		lastResponse = readResponse()
		this
	}

	private String readResponse() {
		log.debug "Receiving..."

		int dataSize = incomingBytesAvailable
		log.debug " $dataSize bytes... "

		def received = receive(dataSize)
		received = ensureXmlIsComplete(received)

		log.debug " complete. [${received.bytes.length} bytes, ${received.length()} chars]\n[$received]\n\n"
		received.trim()
	}

	private String receive(int dataSize) {
		byte[] data = new byte[dataSize]
		inputStream.read(data, 0, dataSize)
		new String(data, 'UTF-8')
	}

	private int getIncomingBytesAvailable() {
		byte[] initBytes = new byte[4]
		def size = 0
		while (incomingSizeIsUnrealisticOrZero(size)) {
			inputStream.read(initBytes,0,4)
			size = unpackN(new String(initBytes, 'UTF-8'))
		}
		size
	}

	private boolean incomingSizeIsUnrealisticOrZero(n) {
		n == 0 || n > 1000000
	}

	// The list invoices response contains ~50% invisible chars so the end of xml is not read.
	// This method carries on reading until the XML is closed.
	private String ensureXmlIsComplete(received) {
		def b
		while (!received.contains("</epp>") && (b = inputStream.read()) != -1) {
			received += new String([b] as byte[], "UTF-8")
		}
		received
	}

	private void handleConnectionNotReady() {
		if (!ready) {
			throw new EppClientException("EppClient is not ready. Has the connection been opened using EppClient.connect? Connection status=$connectionStatus", null)
		}
	}

}