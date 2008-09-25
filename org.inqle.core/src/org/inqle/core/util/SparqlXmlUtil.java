package org.inqle.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SparqlXmlUtil {

	private static Logger log = Logger.getLogger(SparqlXmlUtil.class);
	/**
	 * This method merges the results of 2 SPARQL
	 * queries, each having been converted to XML format
	 * The second Document (addDocument) is added to the first.
	 * 
	 * Note that it assumes that the 2 Documents 
	 * have identical structure in terms of variables
	 * (columns) present.
	 * 
	 * Note that this method only works for deduplicating 2 XML docs generated 
	 * locally.  It fails to identify duplicate records between 2 documents 
	 * from different origin (1 from lookup service and 1 from local query).
	 * Instead, convert to a List<SortedMap<String, String>> using getRows method, 
	 * then use ListMapUtil to merge.
	 * @param originalDocument
	 * @param addDocument
	 * @return
	 */
	public static Document merge(Document originalDocument, Document addDocument) {
		//log.info("SparqlXmlUtil.merge()...");
		Document doc = null;
		try {
			doc = (Document)originalDocument.cloneNode(true);
			//log.info("doc initialized to null...");
//			originalDocument.setStrictErrorChecking(false);
	//		addDocument.setStrictErrorChecking(false);
			if (addDocument == null) return originalDocument;
			if (originalDocument == null) return addDocument;
		
			//log.info("Neither Document is null.");
			NodeList originalDocRows = null;
			//log.info("A...");
			try {
				//log.info("B...");
//				originalDocRows = originalDocument.getElementsByTagName("result");
				XpathEvaluator xpathEvaluator = new XpathEvaluator("//sparql/results/result");
				//log.info("created xpathEvaluator");
				originalDocRows = xpathEvaluator.queryGetNodeList(originalDocument);
				//log.info("C...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("Error in getting NodeList from originalDocument", e);
			}
			
			//log.info("Merging XML Documents: FIRST\n" + XmlDocumentUtil.xmlToString(originalDocument.getDocumentElement()));
			//log.info("Merging XML Documents: SECOND\n" + XmlDocumentUtil.xmlToString(addDocument.getDocumentElement()));
			
			//log.info("originalDocRows.getLength()=" + originalDocRows.getLength());
			if (originalDocRows == null || originalDocRows.getLength()==0) {
				return addDocument;
			}
		
			//log.info("Doing merge...");
		
			//log.info("1: originalDocument==null?" + (originalDocument==null));
			Object resultsObj = originalDocument.getElementsByTagName("results");
			//log.info("2...");
			NodeList resultsNL = (NodeList)resultsObj;
			//log.info("3...");
			Element resultsElement = (Element)resultsNL.item(0);
			
			Object mergedResultsObj = doc.getElementsByTagName("results");
			//log.info("2...");
			NodeList mergedResultsNL = (NodeList)mergedResultsObj;
			//log.info("3...");
			Element mergedResultsElement = (Element)mergedResultsNL.item(0);
			
			//log.info("4...");
			NodeList addRows = addDocument.getElementsByTagName("result");
//			log.info("addRows=NodeList of length:" + addRows.getLength());
			//get list of <result> tags from the 2nd set of results, to be added
//			NodeList addResults = addDocument.getElementsByTagName("results");
//			Element addResultsElement = (Element)addResults.item(0);
//			NodeList addRows = addResultsElement.getElementsByTagName("result");
			int addRowsCount = addRows.getLength();
			for (int resultCount=0; resultCount<addRowsCount; resultCount++) {
//				log.info("resultCount=" + resultCount);
				Node addRowNode = addRows.item(resultCount).cloneNode(true);
//				Node newAddRowNode = addRowNode.cloneNode(true);
				//log.info("Adding this node:" + addRowNode.getNodeName() + "=" + addRowNode.getTextContent());
				//originalResultsElement.insertBefore(newAddRowElement, null);
				//originalResultsElement.appendChild(newAddRowElement);				
//				doc.importNode(addRowNode, true);
				
				//
				if (isNewResultNode(addRowNode, resultsElement)) {
					doc.adoptNode(addRowNode);
					mergedResultsElement.appendChild(addRowNode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error merging XML documents", e);
		}
		//log.info("Returning this MERGED document\n" + XmlDocumentUtil.xmlToString(doc.getDocumentElement()));
		return doc;
		
	}
	
	/**
	 * Returns true if a node is not present in the parentElement
	 * @param externalNode to be tested for redundancy
	 * @param parentElement the element to test for already existing identical child nodes
	 * @return
	 */
	private static boolean isNewResultNode(Node externalNode, Element parentElement) {
		externalNode.normalize();
//		log.info("IS NODE UNIQUE?" + XmlDocumentUtil.xmlToString((Element)externalNode));
		//parentElement.normalize();
//		NodeList nodeList = parentElement.getChildNodes();
		NodeList nodeList = parentElement.getElementsByTagName("result");
		for (int nodeCount=0; nodeCount < nodeList.getLength(); nodeCount++) {
			Node existingNode = nodeList.item(nodeCount);
			//log.info("Test existing node #" +nodeCount + ": " + existingNode.getNodeName() + "=" + existingNode.getTextContent());
			existingNode.normalize();
//			log.info("Test existing node #" +nodeCount + ": " + XmlDocumentUtil.xmlToString((Element)existingNode));
			if (externalNode.isEqualNode(existingNode)) {
//				log.info("NODE is equivalent to an existing one: " + externalNode.getNodeName() + "=" + externalNode.getTextContent());
//				log.info("NO. It is equivalent to an existing one.");
				return false;
			}
		}
//		log.info("YES. It is unique.");
		return true;
	}

	public static int countResults(Document document) {
		NodeList results = document.getElementsByTagName("result");
		//log.info("Counting document results.  Found " + results.getLength());
		return results.getLength();
	}
	
	@Deprecated
	public NodeList getRows(Document sparqlDocument) {		
		NodeList rowNodes = sparqlDocument.getElementsByTagName("result");
		return rowNodes;
	}
	
	/**
	 * Given a XML Document containing a SPARQL result set,
	 * retrieves a List (one entry per row) of Maps (one entry per column)
	 * @param sparqlDocument
	 * @return
	 */
	public static List<SortedMap<String, String>> getRowValues(Document sparqlDocument) {
		ArrayList<SortedMap<String, String>> rowElements = new ArrayList<SortedMap<String, String>>();
		NodeList results = sparqlDocument.getElementsByTagName("results");
		Element resultsElement = (Element)results.item(0);
		NodeList rows = resultsElement.getElementsByTagName("result");
		
		for (int j=0; j<rows.getLength(); j++) {
			SortedMap<String, String> valueMap = new TreeMap<String, String>();
			Element rowElement = (Element)rows.item(j);
			NodeList bindingNodes = rowElement.getElementsByTagName("binding");
			for (int k=0; k<bindingNodes.getLength(); k++) {
				Element cellElement = (Element)bindingNodes.item(k);
				String varName = cellElement.getAttribute("name");
				String elementValue = cellElement.getTextContent();
//				//log.info("Row=" + j + ", Col=" + k + " Adding:" + varName + "=" + elementValue);
				valueMap.put(varName, elementValue);
			}
			rowElements.add(valueMap);
		}
		return rowElements;
	}
	
	public static List<String> getHeaderVariables(Document xmlDocument) {
		NodeList headers = xmlDocument.getElementsByTagName("head");
		Element header = (Element)headers.item(0);
		NodeList variables = header.getElementsByTagName("variable");
		List<String> headerVariables = new ArrayList<String>();
		for (int i=0; i<variables.getLength(); i++) {
			Element variableNode = (Element)variables.item(i);
			String variableStr = variableNode.getAttribute("name");
			headerVariables.add(variableStr);
		}
		return headerVariables;
	}
}
