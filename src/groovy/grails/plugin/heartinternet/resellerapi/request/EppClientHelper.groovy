package grails.plugin.heartinternet.resellerapi.request

import java.nio.ByteBuffer

class EppClientHelper {

	static String prepareForEpp(msg) {
		String sizeInfo = packN(msg.length() + 4)
		"${sizeInfo}${msg}"
	}

	static String packN(int value) {
		byte[] bytes = ByteBuffer.allocate(4).putInt(new Integer(value)).array();
		new String(bytes, 'UTF-8')
	}

	static int unpackN(String value) {
		ByteBuffer.wrap(value.bytes).getInt()
	}

}
