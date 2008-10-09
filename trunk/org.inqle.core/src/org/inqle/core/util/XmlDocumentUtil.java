package org.inqle.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XmlDocumentUtil {

	public static final String DEFAULT_XML_CHARACTER_SET = "UTF-8";
	//"ISO-8859-1"
	public static Logger log = Logger.getLogger(XmlDocumentUtil.class);
	
	public static Document getDocument(String xmlString) {
		ByteArrayInputStream in;
		try {
			in = new ByteArrayInputStream(xmlString.getBytes(DEFAULT_XML_CHARACTER_SET));
		} catch (Exception e) {
			log.error("Character set " + DEFAULT_XML_CHARACTER_SET + " not supported??");
			return null;
		}
		return getDocument(in);
	}
	
	public static Document getDocument(InputStream in) {
		Document document = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(in);
		} catch (Exception e) {
			log.error("Unable to build/parse XML from local SPARQL query", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				//do not close if unable to do so
			}
		}
		return document;
	}
	
	public static String xmlToString(Element element) {
		String xmlString = null;
		OutputFormat outputFormat = new OutputFormat("XML", DEFAULT_XML_CHARACTER_SET, true);
		outputFormat.setIndent(1);
		outputFormat.setIndenting(true);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XMLSerializer serializer = new XMLSerializer(outputStream, outputFormat);
		// As a DOM Serializer
		try {
			serializer.asDOMSerializer();
			serializer.serialize( element );
			xmlString = new String(outputStream.toByteArray());
		} catch (IOException e) {
			log.error("Unable to serialize received XML Document", e);
			return null;
		}
		return xmlString;
	}

	public static String xmlToString(Document document) {
		if (document==null) return null;
		return xmlToString(document.getDocumentElement());
	}
}
