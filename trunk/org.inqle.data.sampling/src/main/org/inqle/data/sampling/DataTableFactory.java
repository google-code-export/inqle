package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.inqle.data.rdf.jena.RdfTable;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Creates DataTable objects.  It does not attempt to traverse RDF to "fill in" blank nodes, but assumes the 
 * RdfTable might have been cleansed using RdfTableFactory prior to creation of the DataTable.
 * @author David Donohue
 * May 16, 2007
 * 
 * TODO add method to persist a DataTable as RDF
 * TODO add method to store component data as RDF statements
 */
public class DataTableFactory {
	
	public static DataTable createDataTable(RdfTable rdfTable, List<DataColumn> dataColumns) {
		DataTable dataTable = new DataTable();
		
		//TODO consider remove this to reduce memory usage
		dataTable.setRdfTable(rdfTable);
		
		List<?> results = rdfTable.getResultList();
		
		if (results == null || results.size() == 0) return null;
		if (dataColumns == null || dataColumns.size() == 0) return null;
		
		dataTable.setColumns(dataColumns);
		
		//use the first record to assess the contents of RDF objects, and to find predicates which point to them
		//QuerySolution firstSolution = (QuerySolution)results.get(0);
	
		//Next add rows to the data table
		Iterator<?> resultI = results.iterator();
		while (resultI.hasNext()) {//Loop thru each result
			QuerySolution resultQS = (QuerySolution)resultI.next();
			//ListOrderedMap row = new ListOrderedMap();//the row to build
			List<DataCell> row = new ArrayList<DataCell>();
			
			for (DataColumn dataColumn: dataColumns) {//Loop thru each column
				String queryLabel = dataColumn.getQueryLabel();
				RDFNode node = resultQS.get(queryLabel);
				DataCell cell = new DataCell(node);
				row.add(cell);
			}//Loop back to next column
			
			dataTable.addRow(row);
		}//loop back to next result
		
		return dataTable;
	}
}