package grails.plugin.heartinternet.resellerapi

import groovy.xml.MarkupBuilder

class XmlRequest {

	private def writer
	def xml

	XmlRequest() {
		writer = new StringWriter()
		xml = new MarkupBuilder(writer)
	}

	String toString() {
		"<?xml version=\"1.0\"?>" + writer.toString()
	}

	String toFlattenedString() {
		def flattened = toString().replaceAll(/>\s+</,"><")
		flattened.replaceAll("[\n\t]","").trim()
	}

}
