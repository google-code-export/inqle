package org.inqle.data.rdf.jena.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class QuerySolutionValueExtractor {

	public static final String DISPLAY_NULL = "[null]";

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static Logger log = Logger.getLogger(QuerySolutionValueExtractor.class);
	
	/**
	 * Retrieves for display a value from the provided QuerySolution
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public static String getDisplayable(QuerySolution querySolution, String fieldName) {
		
		RDFNode node = querySolution.get(fieldName);
		if (node instanceof Literal) {
			Literal literalObj = (Literal)node;
			Object literalVal = null;
			try {
				literalVal = literalObj.getValue();
			} catch (Exception e) {
				log.warn("Unable to get literal value " + literalObj, e);
				return "";
			}
			
			if (literalVal == null) {
				literalVal = literalObj.getLexicalForm();
			}
			
			if (literalVal instanceof XSDDateTime) {
				XSDDateTime xsdDateVal = (XSDDateTime)literalVal;
				Calendar calVal = xsdDateVal.asCalendar();
				return simpleDateFormat.format(calVal.getTime());
			} else {
				return literalVal.toString();
			}
		} else if (node != null) {
			return node.toString();
		} else {
			return DISPLAY_NULL;
		}
	}

	/**
	 * Retrieve a List of (String) values, from a List of QuerySolution objects
	 * @param asList
	 * @param idField
	 * @return
	 */
	public static List<String> getValues(List<QuerySolution> querySolutions, String idField) {
		List<String> values = new ArrayList<String>();
		for (QuerySolution querySolution: querySolutions) {
			values.add(getDisplayable(querySolution, idField));
		}
		return values;
	}

}
