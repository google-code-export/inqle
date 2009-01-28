package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;

/**
 * Finds Jenabean objects
 * @author David Donohue
 * Dec 16, 2008
 */
public class Finder {

	private static final String ID_VARIABLE = "id";

	public static List<?> listJenabeansWithStringValue(Datamodel datamodel, Class<?> theClass, String predicateUri, String value) {
		Persister persister = Persister.getInstance();
		List<String> ids = listJenabeanIdsWithStringValue(datamodel, theClass, predicateUri, value);
		List<Object> jenabeanList = new ArrayList<Object>();
		for (String id: ids) {
			Object jenabean = persister.reconstitute(theClass, id, true);
			jenabeanList.add(jenabean);
		}
		return jenabeanList;
	}
	
	public static List<String> listJenabeanIdsWithStringValue(Datamodel datamodel, Class<?> theClass, String predicateUri, String value) {
		String sparql = getSparqlMatchStringValue(theClass, predicateUri, value);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(sparql);
		queryCriteria.addDatamodel(datamodel);
		List<String> results = Queryer.selectSimpleList(queryCriteria, ID_VARIABLE);
		return results;
	}

	private static String getSparqlMatchStringValue(Class<?> theClass, String propertyUri, String value) {
	
		String sparql = "PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT DISTINCT ?id \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?uri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?" + ID_VARIABLE + " \n" +
			" . ?uri a ?classUri\n" +
			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + theClass.getName() + "\" \n" +
			" . ?uri <" + propertyUri + "> \"" + value + "\" \n" +
			"} }\n";
		return sparql;
	}
}
