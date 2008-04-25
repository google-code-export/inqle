package org.inqle.data.rdf.jena.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.RdfTable;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * Provides static methods to convert 1 container into another
 * @author David Donohue
 * 2007-03-02
 *
 */
public class Converter {

	static Logger log = Logger.getLogger(Converter.class);

	public static List<Resource> resIteratorToList(ResIterator resI) {
		ArrayList<Resource> returnL = new ArrayList<Resource>();
		while (resI.hasNext()) {
			Resource res = resI.nextResource();
			returnL.add(res);
		}
		return returnL;
	}
	
	public static List<Statement> stmtIteratorToList(StmtIterator resI) {
		ArrayList<Statement> returnL = new ArrayList<Statement>();
		while (resI.hasNext()) {
			Statement stmt = resI.nextStatement();
			returnL.add(stmt);
		}
		return returnL;
	}
	
	/**
	 * converts jena ResultSet into an ArrayList of QuerySolution objects
	 * @param results the jena ResultSet
	 * @return ArrayList of QuerySolution objects
	 */
	public static List<QuerySolution> resultSetToList(ResultSet results) {
		List<QuerySolution> returnL = new ArrayList<QuerySolution>();
		while (results.hasNext()) {//loop thru each row
			QuerySolution qs = results.nextSolution();
			log.debug("Adding Query Result:" + qs.toString());
			returnL.add(qs);
		}//loop back to next row
		return returnL;
	}

	/**
	 * Converts a Model to a list of Statements
	 * @param model
	 * @return
	 */
	public static List<Statement> modelToList(Model model) {
		ArrayList<Statement> returnL = new ArrayList<Statement>();
		StmtIterator sI = model.listStatements();
		while (sI.hasNext()) {
			Statement stmt = sI.nextStatement();
			returnL.add(stmt);
		}
		return returnL;
	}
	
	/*remove this dependency on CSVFormatter
	public static String resultSetToCSV(ResultSet results) {
		String csv = "";
		
		//first add headers
		List<?> resultVars = results.getResultVars();
		Iterator<?> headerI = resultVars.iterator();
		int column = 0;
		String row = "";
		while (headerI.hasNext()) {
			column++;
			if (column > 1) row += ",";
			String header = (String)headerI.next();
			row += CSVFormatter.qualify(header);
		}
		csv += row;
		
		log.debug("Results: hasNext()? " + results.hasNext() + "; Row Number=" + results.getRowNumber());
		//next add rows of data
		while (results.hasNext()) {//loop thru each row
			row = "\n";
			headerI = resultVars.iterator();
			QuerySolution qs = results.nextSolution();
			column = 0;
			while (headerI.hasNext()) {//loop thru each column
				column++;
				if (column > 1) row += ",";
				String header = (String)headerI.next();
				RDFNode cell = qs.get(header);
				row += CSVFormatter.qualify(cell.toString());
			}//loop back to next column
			csv += row;
		}//loop back to next row
		return csv;
	}
	*/
	
	/*remove this dependency on CSVFormatter

	 * Converts a RdfTable object to a String of CSV
	 * @param results
	 * @return
	 *
	public static String memoryResultToCSV(RdfTable results) {
		String csv = "";
		if (results == null) return csv;
		
		//first add headers
		List<?> headers = results.getVarNameList();
		Iterator<?> headerI = headers.iterator();
		int column = 0;
		String row = "";
		while (headerI.hasNext()) {
			String header = (String)headerI.next();
			column++;
			if (column > 1) row += ",";
			row += CSVFormatter.qualify(header);
		}
		csv += row;
		
		List<?> resultList = results.getResultList();
		Iterator<?> resultI = resultList.iterator();
		//next add rows of data
		while (resultI.hasNext()) {//loop thru each row
			row = "\n";
			headerI = results.getVarNameList().iterator();
			QuerySolution qs = (QuerySolution)resultI.next();
			column = 0;
			while (headerI.hasNext()) {//loop thru each column
				column++;
				if (column > 1) row += ",";
				String header = (String) headerI.next();
				RDFNode cell = qs.get(header);
				row += CSVFormatter.qualify(cell.toString());
			}//loop back to next column
			csv += row;
		}//loop back to next row
		return csv;
	}
	*/
	
	public static RdfTable resultSetToRdfTable(ResultSet resultSet) {
		RdfTable rdfTable = new RdfTable();
		List<QuerySolution> resultList = Converter.resultSetToList(resultSet);
		rdfTable.setResultList(resultList);
		List<String> varNameList = resultSet.getResultVars();
		rdfTable.setVarNameList(varNameList);
		return rdfTable;
	}
	
	/**
	 * Expects that the ResultSet should contain a single column/attribute,
	 * and that attribute should be a URI resource.
	 * @param resultSet the Jena ResultSet to convert
	 * @return resultList a list of URIs
	 */
	public static List<String> resultSetToUriList(ResultSet resultSet) {
		List<?> varNameList = resultSet.getResultVars();
		if (varNameList.size() != 1) {
			throw new RuntimeException("Converter.resultSetToUriList() expects a ResultSet of 1 attribute, but found " + varNameList.size());
		}
		String varName = (String)varNameList.get(0);
		List<String> resultList = new ArrayList<String>();
		while (resultSet.hasNext()) {//loop thru each row
			QuerySolution qs = resultSet.nextSolution();
			Resource resource = qs.getResource(varName);
			if (resource == null) continue;
			String uriStr = resource.getURI();
			resultList.add(uriStr);
		}//loop back to next row
		
		return resultList;
	}
	
	/**
	 * Extracts a list containing a single column/attribute,
	 * and that attribute should be a URI resource.
	 * @param resultSet the Jena ResultSet to convert
	 * @param varName the name of the column to retrieve
	 * @return resultList a list of values
	 */
	public static List<String> resultSetToSimpleList(ResultSet resultSet, String varName) {
		List<String> resultList = new ArrayList<String>();
		while (resultSet.hasNext()) {//loop thru each row
			QuerySolution qs = resultSet.nextSolution();
			RDFNode node = qs.get(varName);
			if (node == null) continue;
			String stringVal = "";
			if (node instanceof Resource) {
				Resource resource = (Resource)node;
				stringVal = resource.getURI();
			} else if (node instanceof Literal) {
				Literal literal = (Literal)node;
				stringVal = literal.getLexicalForm();
			}
			
			resultList.add(stringVal);
		}//loop back to next row
		
		return resultList;
	}
}
