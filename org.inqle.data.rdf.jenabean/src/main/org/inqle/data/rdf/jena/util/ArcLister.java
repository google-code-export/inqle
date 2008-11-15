package org.inqle.data.rdf.jena.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.RdfTableWriter;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcStep;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Resource;

public class ArcLister {

	private static Logger log = Logger.getLogger(ArcLister.class);
	
	/**
	 * Get SPARQL for finding Arc properties of the given resource subject.  
	 * This query looks for Arcs with maximum of 3 steps.
	 * @param subjectClassUri
	 * @return
	 */
	public static String getSparqlSelectArcs(String subjectClassUri) {
		String sparql = "SELECT DISTINCT ?pred1 ?pred2 ?pred3 \n" +
			"{ GRAPH ?anyGraph {" +
			"?subject a <" + subjectClassUri + "> . \n" +
			"?subject ?pred1 ?obj1 . \n" +
			"OPTIONAL { \n" +
			"?obj1 ?pred2 ?obj2 \n" +
			"} . \n" +
			"OPTIONAL { \n" +
			"?obj2 ?pred3 ?obj3 \n" +
			"}" +
			"} }";
		return sparql;
	}
	

	public static List<Arc> listArcs(Collection<String> datasetIdList, String subjectClassUri) {
		String sparql = getSparqlSelectArcs(subjectClassUri);
		log.info("Retrieving Arcs using this query: " + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	public static List<Arc> listRandomArcs(Collection<String> datasetIdList, String subjectClassUri, int numberToSelect) {
		String baseSparql = getSparqlSelectArcs(subjectClassUri);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		String sparql = Queryer.decorateSparql(baseSparql, true, 0, numberToSelect);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Conducts a query and converts the results into a List of Arc objects
	 * Assumes that the predicates will be named "pred1", "pred2", and "pred3"
	 * @param queryCriteria
	 * @return the List of Arc
	 * 
	 * TODO dynamically handle predicates
	 */
	public static List<Arc> listArcs(QueryCriteria queryCriteria) {
		RdfTable results = Queryer.selectRdfTable(queryCriteria);
		log.info("Received results: " + RdfTableWriter.dataTableToString(results));
		if (results==null || results.countResults()==0) return null;
		List<QuerySolution> resultsList = results.getResultList();
		List<Arc> arcList = new ArrayList<Arc>();
		for (QuerySolution querySolution: resultsList) {
			Arc arc = new Arc();
			Resource pred1 = querySolution.getResource("pred1");
			if (pred1 != null && UriMapper.isUri(pred1.toString())) {
//				arc.addArcStep(pred1.toString());
				arc.addArcStep(new ArcStep(pred1.toString()));
			}
			Resource pred2 = querySolution.getResource("pred2");
			if (pred2 != null && UriMapper.isUri(pred2.toString())) {
				arc.addArcStep(new ArcStep(pred2.toString()));
			}
			Resource pred3 = querySolution.getResource("pred3");
			if (pred3 != null && UriMapper.isUri(pred3.toString())) {
				arc.addArcStep(new ArcStep(pred3.toString()));
			}
			arcList.add(arc);
		}
		return arcList;
	}

}
