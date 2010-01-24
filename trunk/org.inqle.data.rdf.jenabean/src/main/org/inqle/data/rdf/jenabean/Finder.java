package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.rdf.RDF;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Finds Jenabean objects
 * @author David Donohue
 * Dec 16, 2008
 */
public class Finder {

	private static final String ID_VARIABLE = "id";
	private static Logger log = Logger.getLogger(Finder.class);
	
	/**
	 * 
	 * @param <T> the type to reconstitute
	 * @param datamodelId the ID of the datamodel, from which to reconstitute
	 * @param theClass the class of the type to reconstitute
	 * @param predicateUri the property to test
	 * @param value the value to test
	 * @return
	 */
	public static <T> List<T> listJenabeansWithStringValue(
			String datamodelId, 
			Class<T> theClass, 
			String predicateUri, 
			String value) {
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodelId);
		List<String> ids = listJenabeanIdsWithStringValue(
			datamodelId, 
			theClass, 
			predicateUri, 
			value);
		List<T> jenabeanList = new ArrayList<T>();
		for (String id: ids) {
			T jenabean = Persister.reconstitute(theClass, id, model, true);
			jenabeanList.add(jenabean);
		}
		return jenabeanList;
	}
	
	public static List<String> listJenabeanIdsWithStringValue(
			String datamodelId, 
			Class<?> theClass, 
			String predicateUri, 
			String value) {
		String sparql = getSparqlMatchStringValue(theClass, predicateUri, value);
		log.info("SPARQL to find Jenabeans:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(sparql);
		queryCriteria.addDatamodel(datamodelId);
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
