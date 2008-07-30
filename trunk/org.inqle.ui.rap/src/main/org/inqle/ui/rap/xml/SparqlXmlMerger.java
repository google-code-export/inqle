package org.inqle.ui.rap.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SparqlXmlMerger {

	/**
	 * This method merges the results of 2 SPARQL
	 * queries, each having been converted to XML format
	 * The second Document (addDocument) is added to the first.
	 * 
	 * Note that it assumes that the 2 Documents 
	 * have identical structure in terms of variables
	 * (columns) present.
	 * @param originalDocument
	 * @param addDocument
	 * @return
	 */
	public static void merge(Document originalDocument, Document addDocument) {
		//get <results> tag from the original set of results
		NodeList originalResults = originalDocument.getElementsByTagName("results");
		Element originalResultsElement = (Element)originalResults.item(0);
		
		//get list of <result> tags from the 2nd set of results, to be added
		NodeList addResults = originalDocument.getElementsByTagName("results");
		Element addResultsElement = (Element)originalResults.item(0);
		NodeList addRows = addResultsElement.getElementsByTagName("result");
		for (int i=0; i<addRows.getLength(); i++) {
			Object addRowObject = addRows.item(i);
			Element addRowElement = (Element)addRowObject;
			originalResultsElement.insertBefore(addRowElement, null);
		}
		
	}
}
