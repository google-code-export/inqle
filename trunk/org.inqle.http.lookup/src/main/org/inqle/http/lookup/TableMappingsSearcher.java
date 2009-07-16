package org.inqle.http.lookup;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.QueryCriteriaFactory;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jena.util.DatafileUtil;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;

import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;

public class TableMappingsSearcher {

	private static final Logger log = Logger.getLogger(TableMappingsSearcher.class);
	
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
//					sparql += ". ?URI rdfs:subClassOf rdfs:Class \n";
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
	 * Query the scheman datamodels for subjects
	 * @param searchTerm
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchSchemaDatamodelsForSubjects(String searchTerm, int limit, int offset) {
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
	 * Query the scheman datamodels for subjects
	 * @param searchTerm
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlSearchPreferredOntologyDatamodelsForSubjects(String searchTerm, int limit, int offset) {
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

	public static String lookupMappings(String headerText, int countSearchResults, int offset) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(getSparqlFindMatchingMappings(headerText, countSearchResults, offset));
		queryCriteria.addDatamodel(DataMapping.MAPPING_DATASET_ROLE_ID);
		String matchingMappingsXml = Queryer.selectXml(queryCriteria);
		return matchingMappingsXml;
	}
}
