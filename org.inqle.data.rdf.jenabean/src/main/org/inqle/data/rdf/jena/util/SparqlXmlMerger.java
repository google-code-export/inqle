package org.inqle.data.rdf.jena.util;

import org.apache.log4j.Logger;
import org.inqle.core.util.XmlDocumentSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SparqlXmlMerger {

	private static Logger log = Logger.getLogger(SparqlXmlMerger.class);
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
	public static Document merge(Document originalDocument, Document addDocument) {		
		if (addDocument == null) return originalDocument;
		if (originalDocument == null) return addDocument;
		
		log.info("Merging XML Documents: FIRST\n" + XmlDocumentSerializer.xmlToString(originalDocument));
		log.info("Merging XML Documents: SECOND\n" + XmlDocumentSerializer.xmlToString(addDocument));
		
		Document doc = originalDocument;
		try {
			NodeList resultsNL = originalDocument.getElementsByTagName("results");
			Element resultsElement = (Element)resultsNL.item(0);
			
			NodeList addRows = addDocument.getElementsByTagName("result");
			
			//get list of <result> tags from the 2nd set of results, to be added
//			NodeList addResults = originalDocument.getElementsByTagName("results");
//			Element addResultsElement = (Element)originalResults.item(0);
//			NodeList addRows = addResultsElement.getElementsByTagName("result");
			for (int i=0; i<addRows.getLength(); i++) {
				
				Object addRowObject = addRows.item(i);
				Node addRowNode = (Node)addRowObject;
//				Node newAddRowNode = addRowNode.cloneNode(true);
//				log.info("Adding this node:" + addRowNode.getNodeName() + "=" + addRowNode.getTextContent());
				//originalResultsElement.insertBefore(newAddRowElement, null);
				//originalResultsElement.appendChild(newAddRowElement);				
//				doc.importNode(addRowNode, true);
				
				//
				if (isNewNode(addRowNode, resultsElement)) {
					doc.adoptNode(addRowNode);
					resultsElement.appendChild(addRowNode);
				}
			}
		} catch (Exception e) {
			log.error("Error merging XML documents", e);
		}
		log.info("Returning this MERGED document\n" + XmlDocumentSerializer.xmlToString(doc));
		return doc;
		
	}
	
	/**
	 * Returns true if a node is not present in the parentElement
	 * @param externalNode to be tested for redundancy
	 * @param parentElement the element to test for already existing identical child nodes
	 * @return
	 */
	private static boolean isNewNode(Node externalNode, Element parentElement) {
		NodeList nodeList = parentElement.getChildNodes();
		for (int i=0; i < nodeList.getLength(); i++) {
			Node existingNode = nodeList.item(i);
			if (externalNode.isEqualNode(existingNode)) {
				log.info("Node is equivalent to an existing one: " + externalNode.getNodeName() + "=" + externalNode.getTextContent());
				return false;
			}
		}
		return true;
	}

	public static int countResults(Document document) {
		NodeList results = document.getElementsByTagName("result");
		log.info("Counting document results.  Found " + results.getLength());
		return results.getLength();
	}
}
