/**
 * 
 */
package org.inqle.ui.rap.table;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;
import org.inqle.data.rdf.jena.util.SparqlXmlUtil;
import org.inqle.ui.rap.csv.CsvImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * Sets the values in a Table, using XML generated from a SPARQL query,
 * as the model.
 * To use this, set the XML names using the <code>setXml</code> 
 * method.
 * 
 * @author David Donohue
 * Mar 11, 2008
 */
public class SparqlXmlTableLabelProvider extends CellLabelProvider {

//	protected int rowNumColumnIndex = -1;


	private Document xmlDocument;

	private ArrayList<String> headerVariables;

	private List<Map<String, String>> rowElements;
	
	public static final Logger log = Logger.getLogger(SparqlXmlTableLabelProvider.class);

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Map<String, String> row = (Map<String, String>) cell.getElement();
		String columnName = headerVariables.get(cell.getColumnIndex());
		String cellValue = row.get(columnName);
//		log.info("Update cell for column #" + cell.getColumnIndex() + ":" + columnName + "=" + cellValue);
		cell.setText(cellValue);
	}

	public Document getXmlDocument() {
		return xmlDocument;
	}

	public void setXmlDocument(Document xmlDoc) {
		this.xmlDocument = xmlDoc;
		NodeList headers = xmlDocument.getElementsByTagName("head");
		Element header = (Element)headers.item(0);
		NodeList variables = header.getElementsByTagName("variable");
		headerVariables = new ArrayList<String>();
		for (int i=0; i<variables.getLength(); i++) {
			Element variableNode = (Element)variables.item(i);
			String variableStr = variableNode.getAttribute("name");
			headerVariables.add(variableStr);
		}
		
//		rowElements = new ArrayList<Map<String, String>>();
//		NodeList results = xmlDocument.getElementsByTagName("results");
//		Element resultsElement = (Element)results.item(0);
//		NodeList rows = resultsElement.getElementsByTagName("result");
//		
//		for (int j=0; j<rows.getLength(); j++) {
//			HashMap<String, String> valueMap = new HashMap<String, String>();
//			Element rowElement = (Element)rows.item(j);
//			NodeList bindingNodes = rowElement.getElementsByTagName("binding");
//			for (int k=0; k<bindingNodes.getLength(); k++) {
//				Element cellElement = (Element)bindingNodes.item(k);
//				String varName = cellElement.getAttribute("name");
//				String elementValue = cellElement.getTextContent();
////				log.info("Row=" + j + ", Col=" + k + " Adding:" + varName + "=" + elementValue);
//				valueMap.put(varName, elementValue);
//			}
//			rowElements.add(valueMap);
//		}
		
		rowElements = SparqlXmlUtil.getRowValues(xmlDoc);
	}

	public ArrayList<String> getHeaderVariables() {
		return headerVariables;
	}

	public List<Map<String, String>> getRowElements() {
		return rowElements;
	}

}
