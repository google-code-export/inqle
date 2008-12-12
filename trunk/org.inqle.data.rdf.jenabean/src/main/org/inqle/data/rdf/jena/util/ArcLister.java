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
	 * @param depth the number of steps from the subject to traverse
	 * @return
	 */
	public static String getSparqlSelectArcs(String subjectClassUri, int depth) {
//		String sparql = "SELECT DISTINCT ?subject ";
		String sparql = "SELECT DISTINCT ";
		for (int i=1; i<=depth; i++) {
			sparql += "?pred" + i + " ";
		}
		sparql += "\n{ GRAPH ?anyGraph { \n" +
			"?subject a <" + subjectClassUri + "> \n";
		if (depth >= 1) {
			sparql +=	". ?subject ?pred1 ?obj1 \n";
		}
		
		String nextSubj = "?obj1";
		for (int i=2; i<=depth; i++) {
			String thisObj = "?obj" + i;
			sparql += ". OPTIONAL { FILTER( bound(" + nextSubj + ") ) \n" + 
				". " + nextSubj + " ?pred" + i + " " + thisObj + "} \n";
			nextSubj = thisObj;
		}
		sparql += "} }";
		return sparql;
	}
	
	/**
	 * Get SPARQL for finding Arc properties of the given resource subject.
	 * The list of Arcs is filtered, to exclude arcs containing common predicates (rdf:type, etc.)  
	 * This query looks for Arcs with maximum of 'depth' steps.
	 * @param subjectClassUri
	 * @param depth the number of steps from the subject to traverse
	 * @return
	 */
	public static String getSparqlSelectFilteredArcs(String subjectClassUri, int depth) {
		String sparql = "SELECT DISTINCT ";
		for (int i=1; i<=depth; i++) {
			sparql += "?pred" + i + " ";
		}
		sparql += "\n{ GRAPH ?anyGraph { \n" +
			"?subject a <" + subjectClassUri + "> \n";
		if (depth >= 1) {
//			sparql +=	". ?subject ?pred1 ?obj1 \n";
			sparql +=	". ?subject ?pred1 ?obj1 . "
				+ Queryer.getSparqlClauseFilterCommonPredicates("?pred1");
		}
		
		String nextSubj = "?obj1";
		for (int i=2; i<=depth; i++) {
			String thisObj = "?obj" + i;
			sparql += "\n. OPTIONAL { FILTER( bound(" + nextSubj + ") ) \n" + 
			  ". " + Queryer.getSparqlClauseFilterCommonPredicates("?pred" + i) +
				"\n. " + nextSubj + " ?pred" + i + " " + thisObj + "} ";
			nextSubj = thisObj;
		}
		sparql += "} }";
		return sparql;
	}
	
//	/**
//	 * Get SPARQL for finding Arc properties of the given resource subject. Select
//	 * only Arcs that terminate with a literal value 
//	 * This query looks for Arcs with maximum of 3 steps.
//	 * @param subjectClassUri
//	 * @param depth the number of steps from the subject to traverse
//	 * @return
//	 */
//	public static String getSparqlSelectValuedArcs(String subjectClassUri, int depth) {
//		String sparql = "SELECT DISTINCT ";
//		for (int i=1; i<=depth; i++) {
//			sparql += "?pred" + i + " ";
//		}
//		sparql += "\n{ GRAPH ?anyGraph { \n" +
//			"?subject a <" + subjectClassUri + "> \n";
//		String statements = "";
//		String nextSubj = "?subject";
//		for (int i=1; i<=depth; i++) {
//			String thisObj = "?obj" + i;
//			statements += "\n . " + nextSubj + " ?pred" + i + " " + thisObj;
//			sparql += ". OPTIONAL { " +
//					"FILTER( isLiteral(" + thisObj + ") ) " + 
//					statements + " } \n";
//			nextSubj = thisObj;
//		}
//		sparql += "} }";
//		return sparql;
//	}
	
//	/**
//	 * Get SPARQL for finding Arc properties of the given resource subject.  
//	 * This query looks for Arcs with maximum of 3 steps.
//	 * @param subjectClassUri
//	 * @param depth the number of steps from the subject to traverse
//	 * @return
//	 */
//	public static String getSparqlSelectValuedArcs2(String subjectClassUri, int depth) {
//		String sparql = "SELECT DISTINCT ";
//		for (int i=1; i<=depth; i++) {
//			sparql += "?pred" + i + " ";
//		}
//		sparql += "\n{ GRAPH ?anyGraph { \n" +
//			"?subject a <" + subjectClassUri + "> \n";
//		if (depth >= 1) {
//			sparql +=	". ?subject ?pred1 ?obj1 \n" +
//					". { ";
//		}
//		
//		String nextSubj = "?obj1";
//		for (int i=2; i<=depth; i++) {
//			if (i > 2) {
//				sparql += " UNION ";
//			}
//			String thisObj = "?obj" + i;
//			sparql += "  { " + nextSubj + " ?pred" + i + " " + thisObj + " \n" +
//					"  . FILTER( bound(" + nextSubj + ") ) ";
//			if (i < depth) {
//				sparql += "  . OPTIONAL { FILTER( isLiteral(" + thisObj + ") ) } \n";
//			} else {
//				sparql += "  . FILTER( isLiteral(" + thisObj + ") ) \n";
//			}
//			sparql += " } \n";
//			nextSubj = thisObj;
//		}
//		sparql += "} } }";
//		return sparql;
//	}

	/**
	 * A 3rd method to get SPARQL for finding Arc properties of the given resource subject.  
	 * This query looks for Arcs with maximum of 3 steps.
	 * @param subjectClassUri
	 * @param depth the number of steps from the subject to traverse
	 * @return
	 */
	public static String getSparqlSelectValuedArcs(String subjectClassUri, int depth) {
//		String sparql = "SELECT DISTINCT ?subject ";
		String sparql = "SELECT DISTINCT ";
		for (int i=1; i<=depth; i++) {
			sparql += "?pred" + i + " ";
		}
		sparql += "\n{ GRAPH ?anyGraph { \n" +
			"?subject a <" + subjectClassUri + "> \n";
		if (depth >= 1) {
			sparql +=	". ?subject ?pred1 ?obj1 \n";
		}
		
		String nextSubj = "?obj1";
		String lastPred = "?pred1";
		for (int i=2; i<=depth; i++) {
			String thisObj = "?obj" + i;
			String thisPred = "?pred" + i;
			sparql += "\n . OPTIONAL { FILTER( bound(" + lastPred + ") ) " + 
				"\n . " + nextSubj + " " + thisPred + " " + thisObj + "} ";
			nextSubj = thisObj;
			lastPred = thisPred;
		}
		
		//add stipulation that the last object is a literal
		sparql += " . FILTER ( \n";
		for (int i=1; i<=depth; i++) {
			if (i > 1) {
//				sparql += " UNION ";
				sparql += " || ";
			}
			String thisObj = "?obj" + i;
//			sparql += " { FILTER( isLiteral(" + thisObj + ") ) } ";
			sparql += " isLiteral(" + thisObj + ") ";
		}
		
		sparql += "\n ) } }";
//		sparql += "\n } }";
		return sparql;
	}
	
	/**
	 * A 3rd method to get SPARQL for finding Arc properties of the given resource subject.  
	 * This query looks for Arcs with maximum of 3 steps.
	 * @param subjectClassUri
	 * @param depth the number of steps from the subject to traverse
	 * @return
	 */
	public static String getSparqlSelectFilteredValuedArcs(String subjectClassUri, int depth) {
//		String sparql = "SELECT DISTINCT ?subject ";
		String sparql = "SELECT DISTINCT ";
		for (int i=1; i<=depth; i++) {
			sparql += "?pred" + i + " ";
		}
		sparql += "\n{ GRAPH ?anyGraph { \n" +
			"?subject a <" + subjectClassUri + "> \n";
		if (depth >= 1) {
			sparql +=	". ?subject ?pred1 ?obj1 . " +
				Queryer.getSparqlClauseFilterCommonPredicates("?pred1");
		}
		
		String nextSubj = "?obj1";
		String lastPred = "?pred1";
		for (int i=2; i<=depth; i++) {
			String thisObj = "?obj" + i;
			String thisPred = "?pred" + i;
			sparql += "\n . OPTIONAL { FILTER( bound(" + lastPred + ") ) " + 
				Queryer.getSparqlClauseFilterCommonPredicates(thisPred) +
				"\n . " + nextSubj + " " + thisPred + " " + thisObj + "} ";
			nextSubj = thisObj;
			lastPred = thisPred;
		}
		
		//add stipulation that the last object is a literal
		sparql += " . FILTER ( \n";
		for (int i=1; i<=depth; i++) {
			if (i > 1) {
//				sparql += " UNION ";
				sparql += " || ";
			}
			String thisObj = "?obj" + i;
//			sparql += " { FILTER( isLiteral(" + thisObj + ") ) } ";
			sparql += " isLiteral(" + thisObj + ") ";
		}
		
		sparql += "\n ) } }";
//		sparql += "\n } }";
		return sparql;
	}
	
	public static List<Arc> listArcs(Collection<String> datasetIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectArcs(subjectClassUri, depth);
		//log.info("Retrieving Arcs using this query: " + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	public static List<Arc> listFilteredArcs(Collection<String> datasetIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectFilteredArcs(subjectClassUri, depth);
		log.info("listFilteredArcs(): retrieving Arcs using this query: " + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of randomly selected arcs
	 * @param datasetIdList a list of dataset IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs
	 */
	public static List<Arc> listRandomArcs(Collection<String> datasetIdList, String subjectClassUri, int depth, int numberToSelect) {
		String baseSparql = getSparqlSelectArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
		//log.info("Finding random Arcs using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of randomly selected arcs, which terminate with a Literal value
	 * @param datasetIdList a list of dataset IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs, terminating with a literal value
	 */
	public static List<Arc> listValuedArcs(Collection<String> datasetIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectValuedArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		log.info("listValuedArcs() using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of randomly selected arcs
	 * @param datasetIdList a list of dataset IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs
	 */
	public static List<Arc> listFilteredRandomArcs(Collection<String> datasetIdList, String subjectClassUri, int depth, int numberToSelect) {
		String baseSparql = getSparqlSelectFilteredArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
		log.info("listFilteredRandomArcs() using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of randomly selected arcs, which terminate with a Literal value
	 * @param datasetIdList a list of dataset IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs, terminating with a literal value
	 */
	public static List<Arc> listRandomValuedArcs(Collection<String> datasetIdList, String subjectClassUri, int depth, int numberToSelect) {
		String baseSparql = getSparqlSelectValuedArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
		log.info("listRandomValuedArcs() using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of all filtered arcs, which terminate with a Literal value
	 * @param datasetIdList a list of dataset IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs, terminating with a literal value
	 */
	public static List<Arc> listFilteredValuedArcs(Collection<String> datasetIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectFilteredValuedArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		log.info("listFilteredValuedArcs() using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		return listArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of randomly selected arcs, which terminate with a Literal value
	 * @param datasetIdList a list of dataset IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs, terminating with a literal value
	 */
	public static List<Arc> listFilteredRandomValuedArcs(Collection<String> datasetIdList, String subjectClassUri, int depth, int numberToSelect) {
		String baseSparql = getSparqlSelectFilteredValuedArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
		log.info("listFilteredRandomValuedArcs() using SPARQL:" + sparql);
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
		log.info("Queried and received results: " + RdfTableWriter.dataTableToString(results));
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
