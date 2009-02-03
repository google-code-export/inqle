package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
	private static Logger log = Logger.getLogger(Finder.class);
	
	public static List<?> listJenabeansWithStringValue(Datamodel datamodel, Class<?> theClass, String predicateUri, String value) {
		Persister persister = Persister.getInstance();
		List<String> ids = listJenabeanIdsWithStringValue(
				datamodel, 
				theClass, 
				predicateUri, 
				value);
		List<Object> jenabeanList = new ArrayList<Object>();
		for (String id: ids) {
			Object jenabean = persister.reconstitute(theClass, id, true);
			jenabeanList.add(jenabean);
		}
		return jenabeanList;
	}
	
	public static List<String> listJenabeanIdsWithStringValue(Datamodel datamodel, Class<?> theClass, String predicateUri, String value) {
		String sparql = getSparqlMatchStringValue(theClass, predicateUri, value);
		log.info("SPARQL to find Jenabeans:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(sparql);
		queryCriteria.addDatamodel(datamodel);
//		log.info("Querying this datamodel:" + JenabeanWriter.toString(datamodel));
		List<String> results = Queryer.selectSimpleList(queryCriteria, ID_VARIABLE);
		log.info("Found Jenabean IDs=" + results);
		return results;
	}

	
	private static String getSparqlMatchStringValue(Class<?> theClass, String propertyUri, String value) {
	
		String sparql = "PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"PREFIX xsd: <" + RDF.XSD + "> \n " + 
			"SELECT DISTINCT ?" + ID_VARIABLE + " \n" +
			"WHERE {\n" +
			"GRAPH ?anyGraph {\n" +
//			"?id ?p ?o " +
			"?uri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?" + ID_VARIABLE + " \n" +
			" . ?uri a ?classUri \n" +
			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + theClass.getName() + "\" \n" +
//			" . ?uri <" + propertyUri + "> \"" + value + "\" \n" +
//			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + theClass.getName() + "\"^^xsd:string \n" +
			" . ?uri <" + propertyUri + "> \"" + value + "\"^^xsd:string \n" +
			"} } \n";
		return sparql;
	}
}
