package org.inqle.http.lookup;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jena.util.DatafileUtil;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;

public class SubjectLookup {

	private static final Logger log = Logger.getLogger(SubjectLookup.class);
	private static final String MINIMUM_SCORE_THRESHOLD = "0.01";
	
	/**
	 * Generate SPARQL for finding subclasses of owlClassUri,
	 * matching the searchTerm.  If owlClassUri is null, search all classes
	 * @param searchTerm
	 * @param owlClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchRdfSubclasses(String searchTerm, String owlClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX pf: <" + RDF.PF + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?URI ?Label ?Comment \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n";
//				". ?URI rdf:type rdfs:Class \n";
				if (owlClassUri != null) {
					sparql += ". ?URI rdfs:subClassOf <" + owlClassUri + "> \n";
				} else {
//					sparql += ". ?URI rdf:type rdfs:Class \n";
					sparql += ". ?URI rdfs:subClassOf rdfs:Class \n";
				}
				sparql += ". OPTIONAL { ?URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY DESC(?Score) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}

	/**
	 * This is hardcoded to query on the default graph (model) only
	 * TODO handle named graphs for this query
	 * @param searchTerm
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchSkosSubjects(String searchTerm, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"PREFIX skos: <" + RDF.SKOS + ">\n" +
			"SELECT DISTINCT ?URI ?Label ?Comment \n" +
			"WHERE {" +
//			"{ GRAPH ?g {\n" +
			"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
		". FILTER ( isURI(?URI) ) \n";	
		sparql += ". OPTIONAL {?URI rdfs:subPropertyOf ?superProperty }\n" +
				". FILTER ( ! bound(?superProperty) ) \n";	
		sparql += ". OPTIONAL { ?URI rdfs:label ?Label }\n" +
			". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
			". OPTIONAL { ?URI skos:definition ?Comment } \n" +
			"} ORDER BY DESC(?Score) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
	
	/**
	 * Lookup any resource, of the provided OWL class URI, which matches the provided search term.
	 * @param searchTermForRdfClass the user-entered query term
	 * @param owlClassUri the URI of the superclass 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupSubclasses(
			String searchTermForRdfClass, 
			String owlClassUri, 
			String internalDatasetRoleId, 
			int countSearchResults, 
			int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(internalDatasetRoleId));
		IndexLARQ textIndex =  persister.getIndex(internalDatasetRoleId);
		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
		log.trace("Searched " + internalDatasetRoleId + " index for '" + searchTermForRdfClass + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.trace("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		String sparql = getSparqlSearchRdfSubclasses(searchTermForRdfClass, owlClassUri, countSearchResults, offset);
		log.trace("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		//log.info("Queried and got these matching results:\n" + matchingClassesXml);
		return matchingClassesXml;
	}

	/**
	 * Lookup any resource, of the provided OWL class URI, which matches the provided search term.
	 * @param searchTermForRdfClass the user-entered query term
	 * @param owlClassUri the URI of the superclass 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupSubclassesInSchemaFiles(String searchTermForRdfClass, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		//add any internal RDF schemas
//		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
		log.trace("Get/Create index of Model...");
		IndexLARQ textIndex =  persister.getSchemaFilesSubjectIndex();
		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
		log.trace("Searched SchemaFiles index for '" + searchTermForRdfClass + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.trace("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		
		log.trace("Get/Create OntModel...");
		queryCriteria.setSingleModel(persister.getSchemaFilesOntModel());
		
		String sparql = getSparqlSearchSkosSubjects(searchTermForRdfClass, countSearchResults, offset);
		log.trace("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		//log.info("Queried and got these matching results:\n" + matchingClassesXml);
		return matchingClassesXml;
	}
}
