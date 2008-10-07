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
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;
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
public class ListMapTableLabelProvider extends CellLabelProvider {

//	protected int rowNumColumnIndex = -1;


	private Document xmlDocument;

	private List<String> headerVariables;

	private List<SortedMap<String, String>> rowElements = new ArrayList<SortedMap<String, String>>();
	
	public static final Logger log = Logger.getLogger(ListMapTableLabelProvider.class);

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

	
	public void setRowElements(List<SortedMap<String, String>> sortedMap) {
		if (sortedMap == null) {
			rowElements = new ArrayList<SortedMap<String, String>>();
		} else {
			rowElements = sortedMap;
		}
	}

	public List<String> getHeaderVariables() {
		return headerVariables;
	}

	public List<SortedMap<String, String>> getRowElements() {
		return rowElements;
	}

	public void setHeaderVariables(List<String> headerVariables) {
		this.headerVariables = headerVariables;
	}

}
