package org.inqle.ui.rap.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
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
		//get <results> tag from the original set of results
//		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder documentBuilder = null;
//		Document newDocument = null;
//		try {
//			documentBuilder = documentBuilderFactory.newDocumentBuilder();
//		} catch (ParserConfigurationException e) {
//			//should not happen
//			log.error("Should not happen: error creating DocumentBuilder", e);
//			return null;
//		}
//		
//		try {
//			newDocument = documentBuilder.newDocument();
//			
//			Element root = (Element)originalDocument.getDocumentElement().cloneNode(false);
//			newDocument.adoptNode(root);
//			newDocument.appendChild(root);
//			Element head = (Element)originalDocument.getElementsByTagName("head").item(0).cloneNode(true);
//			newDocument.adoptNode(head);
//			root.appendChild(head);
//			Element resultsElement = newDocument.createElement("results");
//			root.appendChild(resultsElement);
//			
//			Element originalResultsElement = (Element)newDocument.getElementsByTagName("results").item(0);
//		} catch (Exception e) {
//			log.error("Error merging XML documents", e);
//		}
		
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
		return doc;
		
	}
	
	/**
	 * Returns true is 
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
}
