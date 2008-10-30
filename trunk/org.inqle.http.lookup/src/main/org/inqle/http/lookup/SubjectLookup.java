package org.inqle.http.lookup;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.QueryCriteriaFactory;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jena.util.DatafileUtil;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;

public class SubjectLookup {

	private static final Logger log = Logger.getLogger(SubjectLookup.class);
	private static final String MINIMUM_SCORE_THRESHOLD = "0.01";
	
	/**
	 * Generate SPARQL for finding subclasses of owlClassUri,
	 * matching the searchTerm.  If owlClassUri is null, search all classes
	 * @param searchTerm
	 * @param superClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchRdfSubclasses(String searchTerm, String superClassUri, int limit, int offset) {
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
				if (superClassUri != null) {
					sparql += ". ?URI rdfs:subClassOf <" + superClassUri + "> \n";
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
	 * Generate SPARQL for finding subclasses of owlClassUri,
	 * matching the searchTerm.  If owlClassUri is null, search all classes
	 * @param searchTerm
	 * @param superClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchRdfsAndSpecifiedSubclasses(String searchTerm, String superClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX pf: <" + RDF.PF + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?URI ?Label ?Comment \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
				". { \n" +
				"{ ?URI rdfs:subClassOf <" + superClassUri + "> } \n" +
				"UNION \n" +
				"{ ?URI rdfs:subClassOf rdfs:Class } \n" +
				"} \n" +
				". OPTIONAL { ?URI rdfs:label ?Label }\n" +
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
//	public static String getSparqlSearchSchemaModelForSubjects(String searchTerm, int limit, int offset) {
//		String sparql = 
//			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
//			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
//			"PREFIX owl: <" + RDF.OWL + ">\n" + 
//			"PREFIX pf: <" + RDF.PF + ">\n" + 
//			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
//			"PREFIX skos: <" + RDF.SKOS + ">\n" +
//			"SELECT DISTINCT ?URI ?Label ?Comment \n" +
//			"WHERE {" +
////			"{ GRAPH ?g {\n" +
//			"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
//		". FILTER ( isURI(?URI) ) \n";	
//		sparql += ". OPTIONAL {?URI rdfs:subPropertyOf ?superProperty }\n" +
//				". FILTER ( ! bound(?superProperty) ) \n";	
//		sparql += ". OPTIONAL { ?URI rdfs:label ?Label }\n" +
//			". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
//			". OPTIONAL { ?URI skos:definition ?Comment } \n" +
//			"} ORDER BY DESC(?Score) \n" +
//			"LIMIT " + limit + " OFFSET " + offset;
//		return sparql;
//	}
	
	/**
	 * Query the scheman datasets for subjects
	 * @param searchTerm
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchSchemaDatasetsForSubjects(String searchTerm, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"PREFIX skos: <" + RDF.SKOS + ">\n" +
			"SELECT DISTINCT ?URI ?Label ?Comment \n" +
			"{ GRAPH ?g {\n" +
			"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
			". FILTER ( isURI(?URI) ) \n" +	
			". OPTIONAL {?URI rdfs:subPropertyOf ?superProperty }\n" +
			". FILTER ( ! bound(?superProperty) ) \n" +	
			". OPTIONAL { ?URI skos:prefLabel ?Label }\n" +
			". OPTIONAL { ?URI rdfs:label ?Label }\n" +
			". OPTIONAL { ?URI skos:definition ?Comment } \n" +
			". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
			"} } ORDER BY DESC(?Score) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
	
	/**
	 * Query the scheman datasets for subjects
	 * @param searchTerm
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchPreferredOntologyDatasetsForSubjects(String searchTerm, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"PREFIX skos: <" + RDF.SKOS + ">\n" +
			"PREFIX umbel: <" + RDF.UMBEL + ">\n" +
			"SELECT DISTINCT ?URI ?Label ?Comment \n" +
			"{ GRAPH ?g {\n" +
			"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
			". ?URI a umbel:SubjectConcept \n" +
//			". OPTIONAL { ?URI skos:prefLabel ?Label }\n" +
			". OPTIONAL { ?URI umbel:hasSemset ?semset \n" +
			"    . ?semset skos:prefLabel ?Label }\n" +
			". OPTIONAL { ?URI rdfs:label ?Label }\n" +
			". OPTIONAL { ?URI skos:definition ?Comment } \n" +
			". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
			"} } ORDER BY DESC(?Score) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
	
	/**
	 * Lookup subclasses of inqle:Subject in the rg.inqle.datasets.dataSubject
	 * internal dataset
	 * @param searchTermForRdfClass
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupSubjectsInSubjectsDataset (
		String searchTermForRdfClass, 
		int countSearchResults, 
		int offset) {
		return lookupSubclassesInInternalDataset(
				searchTermForRdfClass,
				RDF.SUBJECT,
				Data.DATA_SUBJECT_DATASET_ROLE_ID, 
				countSearchResults, 
				offset);
	}
	/**
	 *  Lookup in the specified internal dataset any resource, of the provided OWL class URI, which matches the provided search term.
	 * @param searchTermForRdfClass
	 * @param superClassUri the OWL superclass of the subjects to find.  Leave null to search RDFS classes (i.e. subclasses of rdfs:Class)
	 * @param internalDatasetRoleId the role of the internal dataset to search
	 * @param countSearchResults
	 * @param offset usually = 0
	 * @return
	 */
	public static String lookupSubclassesInInternalDataset (
			String searchTermForRdfClass, 
			String superClassUri, 
			String internalDatasetRoleId, 
			int countSearchResults, 
			int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(internalDatasetRoleId));
		IndexLARQ textIndex =  persister.getIndex(internalDatasetRoleId);
		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
		log.info("Searched " + internalDatasetRoleId + " index for '" + searchTermForRdfClass + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.info("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		String sparql = getSparqlSearchRdfSubclasses(searchTermForRdfClass, superClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		log.info("lookupSubclassesInInternalDataset(): Queried and got these matching results:\n" + matchingClassesXml);
		return matchingClassesXml;
	}

	/**
	 * Search the specified internal dataset for subclasses of rdfs:Class UNION 
	 * the specified class URI
	 * 
	 * @param searchTermForRdfClass
	 * @param superClassUri
	 * @param internalDatasetRoleId
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupRdfsAndSpecifiedSubclassesInInternalDataset (
			String searchTermForRdfClass, 
			String superClassUri, 
			String internalDatasetRoleId, 
			int countSearchResults, 
			int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(internalDatasetRoleId));
		IndexLARQ textIndex =  persister.getIndex(internalDatasetRoleId);
		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
		log.info("Searched " + internalDatasetRoleId + " index for '" + searchTermForRdfClass + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.info("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		String sparql = getSparqlSearchRdfsAndSpecifiedSubclasses(searchTermForRdfClass, superClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		log.info("lookupSubclassesInInternalDataset(): Queried and got these matching results:\n" + matchingClassesXml);
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
//	public static String lookupSubclassesInSchemaFiles(String searchTermForRdfClass, int countSearchResults, int offset) {
//		Persister persister = Persister.getInstance();
//		QueryCriteria queryCriteria = new QueryCriteria();
//		//add any internal RDF schemas
////		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
//		log.info("Get/Create index of Model...");
//		IndexLARQ textIndex =  persister.getSchemaFilesSubjectIndex();
//		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
//		log.info("Searched SchemaFiles index for '" + searchTermForRdfClass + "'...");
//		while(searchResultI.hasNext()) {
//			HitLARQ hit = (HitLARQ)searchResultI.next();
//			log.info("Found result: " + hit.getNode() + "; score=" + hit.getScore());
//		}
//		if (textIndex != null) {
//			queryCriteria.setTextIndex(textIndex);
//		}
//		
//		log.info("Get/Create OntModel...");
//		queryCriteria.setSingleModel(persister.getSchemaFilesOntModel());
//		
//		String sparql = getSparqlSearchSchemaModelForSubjects(searchTermForRdfClass, countSearchResults, offset);
//		log.info("Querying w/ this sparql:\n" + sparql);
//		queryCriteria.setQuery(sparql);
//		String matchingClassesXml = Queryer.selectXml(queryCriteria);
//		//log.info("Queried and got these matching results:\n" + matchingClassesXml);
//		return matchingClassesXml;
//	}
	
	
	/**
	 * Lookup any resource, of the provided OWL class URI, which matches the provided search term.
	 * @param searchTermForRdfClass the user-entered query term
	 * @param owlClassUri the URI of the superclass 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupSubclassesInSchemaDatasets(String searchTermForRdfClass, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
//		QueryCriteria queryCriteria = new QueryCriteria();
		QueryCriteria queryCriteria = QueryCriteriaFactory.createQueryCriteriaForDatasetFunction(Persister.EXTENSION_DATASET_FUNCTION_SCHEMAS);
		//add any internal RDF schemas
//		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
		log.trace("Get/Create index of Model...");
//		IndexLARQ textIndex =  persister.getSchemaFilesSubjectIndex();
		IndexLARQ textIndex =  persister.getIndex(Persister.EXTENSION_DATASET_FUNCTION_SCHEMAS);
		if (textIndex==null) return null;
		
		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
		log.trace("Searched Schema Datasets index for '" + searchTermForRdfClass + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.trace("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		
		log.trace("Get/Create OntModel...");
//		queryCriteria.setSingleModel(persister.getSchemaFilesOntModel());
		
		String sparql = getSparqlSearchSchemaDatasetsForSubjects(searchTermForRdfClass, countSearchResults, offset);
		log.trace("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		log.trace("Queried Schema Datasets and got these matching results:\n" + matchingClassesXml);
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
	public static String lookupPreferredOntologySubjectsInSchemaDatasets(String searchTermForRdfClass, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
//		QueryCriteria queryCriteria = new QueryCriteria();
		QueryCriteria queryCriteria = QueryCriteriaFactory.createQueryCriteriaForDatasetFunction(Persister.EXTENSION_DATASET_FUNCTION_SCHEMAS);
		//add any internal RDF schemas
//		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
		log.trace("Get/Create index of Model...");
//		IndexLARQ textIndex =  persister.getSchemaFilesSubjectIndex();
		IndexLARQ textIndex =  persister.getIndex(Persister.EXTENSION_DATASET_FUNCTION_SCHEMAS);
		if (textIndex==null) return null;
		
		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
		log.trace("Searched Schema Datasets index for '" + searchTermForRdfClass + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.trace("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		
		log.info("Get/Create OntModel...");
//		queryCriteria.setSingleModel(persister.getSchemaFilesOntModel());
		
		String sparql = getSparqlSearchPreferredOntologyDatasetsForSubjects(searchTermForRdfClass, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		log.info("Queried Schema Datasets and got these matching results:\n" + matchingClassesXml);
		Model matchingClassesModel = Queryer.selectRdf(queryCriteria);
		log.info("Here are results as Jena model:\n" + JenabeanWriter.modelToString(matchingClassesModel));
		return matchingClassesXml;
	}
}
