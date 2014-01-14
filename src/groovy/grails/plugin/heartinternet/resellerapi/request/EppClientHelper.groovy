package grails.plugin.heartinternet.resellerapi.request
import java.nio.ByteBuffer
import java.nio.ByteOrder

class EppClientHelper {

	static String prepareForEpp(msg) {
		String sizeInfo = packN(msg.length() + 4)
		"${sizeInfo}${msg}"
	}

	static def toPositiveByteArray(bytes) {
		bytes.collect {
			it < 0 ? 256 + it : it
		}
	}

	static String packN(int value) {
		def bytes = ByteBuffer.allocate(4).putInt(value).array()
		bytes = toPositiveByteArray(bytes)
		(bytes as char[]).toString()
	}

	static int unpackN(String value) {
		def bytes = (value.chars as int[]) as byte[]
		ByteBuffer buf = ByteBuffer.allocate(4)
		buf.order(ByteOrder.BIG_ENDIAN)
		buf.put(bytes)
		buf.flip()
		buf.getInt()
	}

}
