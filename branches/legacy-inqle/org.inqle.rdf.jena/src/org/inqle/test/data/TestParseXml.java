package org.inqle.test.data;

import org.apache.log4j.Logger;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.junit.Test;
import org.w3c.dom.Document;

public class TestParseXml {

	private static Logger log = Logger.getLogger(TestParseXml.class);
	
	private static final String XML_SOME_RESULTS = "<?xml version=\"1.0\"?><sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">  <head>    <variable name=\"URI\"/>    <variable name=\"Label\"/>    <variable name=\"Comment\"/>  </head>  <results>    <result>      <binding name=\"URI\">        <uri>http://xmlns.com/foaf/0.1/Person</uri>      </binding>      <binding name=\"Label\">        <literal>Person</literal>      </binding>      <binding name=\"Comment\">        <literal>A person.</literal>      </binding>    </result>    <result>      <binding name=\"URI\">        <uri>http://www.geonames.org/ontology#S.SHRN</uri>      </binding>      <binding name=\"Label\">        <literal xml:lang=\"en\">shrine</literal>      </binding>      <binding name=\"Comment\">        <literal xml:lang=\"en\">a structure or place memorializing a person or religious concept</literal>      </binding>    </result>    <result>      <binding name=\"URI\">        <uri>http://xmlns.com/foaf/0.1/Agent</uri>      </binding>      <binding name=\"Label\">        <literal>Agent</literal>      </binding>      <binding name=\"Comment\">        <literal>An agent (eg. person, group, software or physical artifact).</literal>      </binding>    </result>  </results></sparql>";
	private static final String XML_NO_RESULTS = "<?xml version=\"1.0\"?><sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">  <head>    <variable name=\"URI\"/>    <variable name=\"Label\"/>    <variable name=\"Comment\"/>  </head>  <results>  </results></sparql>";

	@Test
	public void testMergeXml() {
		Document docNoResults = XmlDocumentUtil.getDocument(XML_NO_RESULTS);
		log.info("Created XML:" + XmlDocumentUtil.xmlToString(docNoResults.getDocumentElement()));
		Document docSomeResults = XmlDocumentUtil.getDocument(XML_SOME_RESULTS);
		log.info("Created XML:" + XmlDocumentUtil.xmlToString(docSomeResults.getDocumentElement()));
		Document mergedDocument = SparqlXmlUtil.merge(docSomeResults, docSomeResults);
		log.info("Merged into XML:" + XmlDocumentUtil.xmlToString(mergedDocument.getDocumentElement()));
	}
}
