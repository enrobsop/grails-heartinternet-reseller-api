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

	def send(ApiRequest request) {
		println "------------------------"
		sendToStream(request.message)
		println " ------"
		def received = receiveFromStream()
		println "------------------------"
		request.handleResponse(received)
	}

	private void sendToStream(message) {
		handleSendWhenNotReady()
		println "Sending...\n$message"
		def sout = stream.outputStream
		sout.write(message.bytes)
		sout.flush()
		println "Sending complete."
	}

	private String receiveFromStream() {
		println "Receiving..."

		final int maxBytes = 1000

		def sin     = stream.inputStream
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

	private void handleSendWhenNotReady() {
		if (!ready) {
			throw new EppClientException("EppClient is not ready. Has the stream been opened using EppClient.openStream? Stream status=$streamStatus", null)
		}
	}

}
