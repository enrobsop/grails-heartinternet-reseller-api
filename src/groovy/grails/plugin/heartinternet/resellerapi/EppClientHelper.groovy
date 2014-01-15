package grails.plugin.heartinternet.resellerapi
import java.nio.ByteBuffer

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
		limitTo4Bytes((bytes as char[]).toString())
	}

	static int unpackN(String value) {
		def bytes = (padTo4Chars(value).chars as int[]) as byte[]
		ByteBuffer buf = ByteBuffer.allocate(4).put(bytes).flip()
		buf.getInt()
	}

	private static String limitTo4Bytes(str) {
		byte[] bytes = str.getBytes("UTF-8")
		int offset = Math.max(bytes.length - 4, 0)
		new String(bytes, offset, 4)
	}

	private static String padTo4Chars(str) {
		def holderChar = "\u0000"
		str.padLeft(4, holderChar)
	}

}
