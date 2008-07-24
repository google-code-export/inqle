package org.inqle.http.lookup;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;

public class OwlInstanceLookup {

	private static final Logger log = Logger.getLogger(OwlInstanceLookup.class);
	private static final String MINIMUM_SCORE_THRESHOLD = "0.01";
	
	public static String getSparqlSearchRdfClasses(String searchRdfClass, String owlClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX dc: <" + RDF.DC + ">\n" + 
				"PREFIX pf: <" + RDF.PF + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT ?URI ?Label ?Comment ?Score \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				"(?URI ?Score) pf:textMatch ( '" + searchRdfClass + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
				//". ?URI a owl:Class \n" +
				". ?URI a <" + owlClassUri + "> \n" +
				". OPTIONAL { ?URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY DESC(?Score) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}

	/**
	 * Lookup any resource, of the provided OWL class URI, which matches the provided search term.
	 * @param searchTermForRdfClass the user-entered query term
	 * @param owlClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookup(String searchTermForRdfClass, String owlClassUri, String internalDatasetRoleId, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(internalDatasetRoleId));
		IndexLARQ textIndex =  persister.getIndex(internalDatasetRoleId);
//		Iterator<?> searchResultI = textIndex.search(searchTermForRdfClass);
//		log.info("Searched index for '" + searchTermForRdfClass + "'...");
//		while(searchResultI.hasNext()) {
//			HitLARQ hit = (HitLARQ)searchResultI.next();
//			log.info("Found result: " + hit.getNode() + "; score=" + hit.getScore());
//		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		queryCriteria.setQuery(getSparqlSearchRdfClasses(searchTermForRdfClass, owlClassUri, countSearchResults, offset));
		String matchingClassesXml = Queryer.selectXml(queryCriteria);
		log.info("Queried and got these matching results:\n" + matchingClassesXml);
		return matchingClassesXml;
	}

}
