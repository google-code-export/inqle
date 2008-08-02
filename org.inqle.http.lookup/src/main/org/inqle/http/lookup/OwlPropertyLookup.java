package org.inqle.http.lookup;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.DataMapping;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;

/**
 * This class gets 2 kinds of properties:
 *  * properties of a particular OWL class
 *    (e.g. can be used to find all properties a subclasses of inqle:DataSubject)
 *  * properties of a subclass of inqle:Data, which itself has a inqle:subject of a particular native
 *    subject class.
 * @author David Donohue
 * Aug 1, 2008
 */
public class OwlPropertyLookup {

	private static final Logger log = Logger.getLogger(OwlPropertyLookup.class);
	private static final String MINIMUM_SCORE_THRESHOLD = "0.01";
	
	/**
	 * Generates SPARQL, to find all properties of all instances of all subclasses of inqle:Data, which have the
	 * provided nativeOwlClassUri as a inqle:subject
	 * That is, this finds properties of data rows having a particular inqle:subject.
	 * @param nativeOwlClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindDataInstanceProperties(String nativeOwlClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?URI ?Label ?Comment ?ClassURI \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				". ?ClassURI rdfs:subClassOf inqle:Data \n" +
				". ?ClassURI inqle:subject <" + nativeOwlClassUri + "> \n" +
				". ?InstanceURI a ?ClassURI \n" +
				". ?InstanceURI ?URI ?propertyValue \n" +
				". OPTIONAL { ?URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY ASC(?Label) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}
	
	/**
	 * Generates SPARQL, to find all properties of all subclasses of inqle:Data, which have the
	 * provided nativeOwlClassUri as a inqle:subject.
	 * That is, this finds properties of data tables or of their rows, having the specified inqle:subject.
	 * @param nativeOwlClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindDataSubclassProperties(String nativeOwlClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?URI ?Label ?Comment ?ClassURI \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				". ?ClassURI rdfs:subClassOf inqle:Data \n" +
				". ?ClassURI inqle:subject <" + nativeOwlClassUri + "> \n" +
				". { { ?ClassURI ?URI ?propertyValue } \n" +
				"UNION\n" +
				". { ?InstanceURI a ?ClassURI \n" +
				". ?InstanceURI ?URI ?propertyValue } \n" +
				"} \n" +
				". OPTIONAL { ?URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY ASC(?Label) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}
	
	/**
	 * Generates SPARQL, to find all properties of all subclasses of inqle:Data, which have the
	 * provided nativeOwlClassUri as a inqle:subject.
	 * That is, this finds properties of data tables, having the specified inqle:subject.
	 * @param nativeOwlClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindInqleDataProperties(String nativeOwlClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?URI ?Label ?Comment ?SubjectURI \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				". ?ClassURI rdfs:subClassOf inqle:Data \n" +
				". ?ClassURI inqle:subject <" + nativeOwlClassUri + "> \n" +
				". ?ClassURI ?URI ?propertyValue \n" +
				". OPTIONAL { ?URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY ASC(?Label) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}
	
	/**
	 * Generates SPARQL, to find all properties of all instances of the specified OWL class
	 * E.g., this finds properties of a particular inqle:DataSubject
	 * @param nativeOwlClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindOwlClassProperties(String nativeOwlClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"SELECT DISTINCT ?Property_URI ?Label ?Comment ?SubjectURI \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				" ?InstanceURI a <" + nativeOwlClassUri + "> \n" +
				". ?InstanceURI ?Property_URI ?value \n" +
				". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY ASC(?Label) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}
	
	/**
	 * Lookup any properties of data tables or of their rows, having an inqle:subject of the owlClassUri
	 * @param owlClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupInqleDataProperties(String owlClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_SUBJECT_DATASET_ROLE_ID));
		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
		String sparql = getSparqlFindInqleDataProperties(owlClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}
	
	/**
	 * Lookup any properties of native OWL classes
	 * @param searchTermForRdfClass the user-entered query term
	 * @param owlClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupNativeOwlProperties(String owlClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_SUBJECT_DATASET_ROLE_ID));
		String sparql = getSparqlFindOwlClassProperties(owlClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}

}
