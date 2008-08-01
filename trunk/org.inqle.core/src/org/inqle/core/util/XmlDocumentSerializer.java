package org.inqle.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XmlDocumentSerializer {

	private static Logger log = Logger.getLogger(XmlDocumentSerializer.class);
	
	public static String xmlToString(Document doc) {
		String xmlString = null;
		OutputFormat outputFormat = new OutputFormat("XML","ISO-8859-1",true);
		outputFormat.setIndent(1);
		outputFormat.setIndenting(true);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XMLSerializer serializer = new XMLSerializer(outputStream, outputFormat);
		// As a DOM Serializer
		try {
			serializer.asDOMSerializer();
			serializer.serialize( doc.getDocumentElement() );
			xmlString = new String(outputStream.toByteArray());
		} catch (IOException e) {
			log.error("Unable to serialize received XML Document", e);
			return null;
		}
		return xmlString;
	}
}
