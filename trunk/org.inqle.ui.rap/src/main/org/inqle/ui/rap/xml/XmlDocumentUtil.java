package org.inqle.ui.rap.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class XmlDocumentUtil {

	public static Logger log = Logger.getLogger(XmlDocumentUtil.class);
	
	public static Document getDocument(String xmlString) {
		ByteArrayInputStream in = new ByteArrayInputStream(xmlString.getBytes());
		return getDocument(in);
	}
	
	public static Document getDocument(InputStream in) {
		Document document = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(in);
		} catch (Exception e) {
			log.error("Unable to build/parse XML from local SPARQL query", e);
		}
		return document;
	}
}
