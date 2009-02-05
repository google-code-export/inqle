package org.inqle.data.rdf.jena.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcStep;
import org.inqle.data.rdf.jenabean.Finder;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.cache.SubjectArcsCache;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Retrieves collections of Arcs.  When possible, retrieves from the cache.
 * Cache objects are of class SubjectArcsClass.  A distinct cache object is
 * stored, per datamodel, per subject class, per depth, per type.
 * 
 * At present, we will only support caching of filtered valued arcs.
 * iltered valued arcs are arcs, excluding those that
	 * use common predicates like rdf:type, and ending with a literal value.
 * @author David Donohue
 * Dec 16, 2008
 */
public class ArcLister {

	private static final String FILTERED_VALUED_ARCS = "Filtered_Valued_Arcs";
	private static Logger log = Logger.getLogger(ArcLister.class);
	
	/**
	 * Get a random collection of filtered valued Arcs for the provided 
	 * of datamodel & subject class & depth.
	 * @param datamodelId
	 * @param subjectClassUri
	 * @param depth
	 * @param size - the size of the collection to return
	 * @param arcsToExclude
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Arc> getRandomFilteredValuedArcs(String datamodelId, String subjectClassUri, int depth, int size, Collection<Arc> arcsToExclude) {
		Collection<Arc> availableArcs = getFilteredValuedArcs(datamodelId, subjectClassUri, depth);
		if (availableArcs == null) return null;
		
		Collection<Arc> randomArcs = (Collection<Arc>)RandomListChooser.chooseRandomItemsAdditively(availableArcs, arcsToExclude, size);
		return randomArcs;
	}
	
	/**
	 * Get a random collection of filtered valued Arcs for the provided 
	 * collection of datamodels & subject class & depth.
	 * @param datamodelId
	 * @param subjectClassUri
	 * @param depth
	 * @param size - the size of the collection to return
	 * @param arcsToExclude
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Arc> getRandomFilteredValuedArcs(Collection<String> datamodelIds, String subjectClassUri, int depth, int size, Collection<Arc> arcsToExclude) {
		Collection<Arc> availableArcs = getFilteredValuedArcs(datamodelIds, subjectClassUri, depth);
		if (availableArcs == null) return null;
		
		Collection<Arc> randomArcs = (Collection<Arc>)RandomListChooser.chooseRandomItemsAdditively(availableArcs, arcsToExclude, size);
		return randomArcs;
	}
	
	/**
	 * Get the collection of filtered valued Arcs for the provided datamodel 
	 * & subject class & depth. Filtered valued arcs are arcs, excluding those that
	 * use common predicates like rdf:type, and ending with a literal value.
	 * First try to retrieve from cache.  If not present, query the result and cache it
	 * @param datamodelId
	 * @param subjectClassUri
	 * @return
	 */
	public static Collection<Arc> getFilteredValuedArcs(String datamodelId, String subjectClassUri, int depth) {
		Collection<Arc> arcs = getArcsFromCache(datamodelId, subjectClassUri, depth, FILTERED_VALUED_ARCS);
		if (arcs != null) return arcs;
		
		//not in cache: query then cache it
		arcs = queryGetFilteredValuedArcs(datamodelId, subjectClassUri, depth);
		cacheArcs(datamodelId, subjectClassUri, depth, FILTERED_VALUED_ARCS, arcs);
//		log.info("Retrieved arcs from cache:" + arcs);
		return arcs;
	}
	
	/**
	 * Get the collection of filtered valued Arcs for the provided collection
	 * of datamodels & subject class & depth. 
	 * Filtered valued arcs are arcs, excluding those that
	 * use common predicates like rdf:type, and ending with a literal value.
	 * First try to retrieve from cache.  If not present, query the result and cache it
	 * @param datamodelId
	 * @param subjectClassUri
	 * @return
	 */
	public static Collection<Arc> getFilteredValuedArcs(Collection<String> datamodelIds, String subjectClassUri, int depth) {
		Collection<Arc> masterCollection = new ArrayList<Arc>();
		for (String datamodelId: datamodelIds) {
			masterCollection.addAll(getFilteredValuedArcs(datamodelId, subjectClassUri, depth));
		}
		return masterCollection;
	}
	
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

	/**
	 * A 3rd method to get SPARQL for finding Arc properties of the given resource subject.  
	 * This query looks for Arcs with maximum of 3 steps.
	 * @param subjectClassUri
	 * @param depth the number of steps from the subject to traverse
	 * @return
	 */
	public static String getSparqlSelectValuedArcs(String subjectClassUri, int depth) {
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
				sparql += " || ";
			}
			String thisObj = "?obj" + i;
			sparql += " isLiteral(" + thisObj + ") ";
		}
		
		sparql += "\n ) } }";
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
	
	/**
	 * Get a list of all arcs matching the list of collections
	 * @param datamodelIdList
	 * @param subjectClassUri
	 * @param depth
	 * @return
	 */
	public static List<Arc> queryGetAllArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectArcs(subjectClassUri, depth);
		//log.info("Retrieving Arcs using this query: " + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodelIds(datamodelIdList);
		queryCriteria.setQuery(sparql);
		
		return queryGetArcs(queryCriteria);
	}
	
	public static List<Arc> queryGetFilteredArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectFilteredArcs(subjectClassUri, depth);
		log.info("queryGetFilteredArcs(): retrieving Arcs using this query: " + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodelIds(datamodelIdList);
		queryCriteria.setQuery(sparql);
		
		return queryGetArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of arcs, which terminate with a Literal value
	 * @param datamodelIdList a list of datamodel IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @return a list of selected arcs, terminating with a literal value
	 */
	public static List<Arc> queryGetValuedArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectValuedArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodelIds(datamodelIdList);
		log.info("queryGetValuedArcs() using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		return queryGetArcs(queryCriteria);
	}
	
	/**
	 * Generate a list of all filtered arcs, which terminate with a Literal value
	 * @param datamodelIdList a list of datamodel IDs to query
	 * @param subjectClassUri the URI of the subject class, from which to walk
	 * @param depth max number of steps to take from the subject
	 * @param numberToSelect number of Arcs to select
	 * @return a list of selected arcs, terminating with a literal value
	 */
	public static List<Arc> queryGetFilteredValuedArcs(String datamodelId, String subjectClassUri, int depth) {
		String sparql = getSparqlSelectFilteredValuedArcs(subjectClassUri, depth);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodel(datamodelId);
		log.info("queryGetFilteredValuedArcs() using SPARQL:" + sparql);
		queryCriteria.setQuery(sparql);
		
		List<Arc> arcs = queryGetArcs(queryCriteria);
		
		return arcs;
	}

	/**
	 * Conducts a query and converts the results into a List of Arc objects
	 * Assumes that the predicates will be named "pred1", "pred2", and "pred3"
	 * @param queryCriteria
	 * @return the List of Arc
	 * 
	 * TODO dynamically handle predicates
	 */
	public static List<Arc> queryGetArcs(QueryCriteria queryCriteria) {
		RdfTable results = Queryer.selectRdfTable(queryCriteria);
		//log.info("Queried and received results: " + RdfTableWriter.dataTableToString(results));
		if (results==null || results.countResults()==0) return null;
		List<QuerySolution> resultsList = results.getResultList();
		List<Arc> arcs = new ArrayList<Arc>();
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
			arcs.add(arc);
		}
		
		return arcs;
	}

	/**
	 * Store the collection of Arcs in the appropriate cache
	 * @param datamodelId
	 * @param subjectClassUri
	 * @param arcs
	 */
	public static void cacheArcs(String datamodelId, String subjectClassUri, int depth, String type, Collection<Arc> arcs) {
		//first retrieve the appropriate arc
		String arcCacheId = getArcCacheId(datamodelId, subjectClassUri, depth, type);
		Persister persister = Persister.getInstance();
		SubjectArcsCache arcsCache = null;
		try {
			arcsCache = (SubjectArcsCache)persister.reconstitute(SubjectArcsCache.class, arcCacheId, true);
		} catch (Exception e) {
			//unable to reconstitute.  Assume this cache object has not been created
		}
		if (arcsCache == null) {
			arcsCache = new SubjectArcsCache();
			arcsCache.setId(arcCacheId);
			arcsCache.setDatamodelId(datamodelId);
			arcsCache.setSubjectClass(URI.create(subjectClassUri));
			arcsCache.setDepth(depth);
			arcsCache.setType(FILTERED_VALUED_ARCS);
		}
		arcsCache.setArcs(arcs);
		log.info("Caching list of " + arcs.size() + " arcs for datamodelId=" + datamodelId + "; subjectClassUri=" + subjectClassUri + ".");
		persister.persist(arcsCache);
	}

	public static Collection<Arc> getArcsFromCache(String datamodelId, String subjectClassUri, int depth, String type) {
		String arcCacheId = getArcCacheId(datamodelId, subjectClassUri, depth, type);
		Persister persister = Persister.getInstance();
		SubjectArcsCache arcsCache = (SubjectArcsCache)persister.reconstitute(SubjectArcsCache.class, arcCacheId, true);
		if (arcsCache == null) return null;
//		log.info("Retrieved ArcsCache from cache:" + JenabeanWriter.toString(arcsCache));
		log.info("Retrieved Arcs from cache");
		return arcsCache.getArcs();
	}
	
	/**
	 * Generate the ID of the SubjectArcsCache object, given the ID of the datamodel and
	 * the URI of the subject class
	 * @param datamodelId
	 * @param subjectClassUri
	 * @param type 
	 * @param depth 
	 * @return
	 */
	public static String getArcCacheId(String datamodelId, String subjectClassUri, int depth, String type) {
		String cacheId = "ArcCacheId_" + datamodelId + "_" + subjectClassUri + "_" + depth + "_" + type;
//		log.info("getArcCacheId(" + datamodelId + ")=" + cacheId);
		return cacheId;
	}
	
	/**
	 * Remove all SubjectArcCache objects, which have the provided datamodelId
	 * @param datamodelId
	 */
	@SuppressWarnings("unchecked")
	public static void invalidateCache(String datamodelId) {
		Persister persister = Persister.getInstance();
		Datamodel targetDatamodel = persister.getTargetDatamodel(SubjectArcsCache.class);
		Collection<SubjectArcsCache> arcCacheObjectsToRemove = (Collection<SubjectArcsCache>)Finder.listJenabeansWithStringValue(targetDatamodel, SubjectArcsCache.class, RDF.INQLE + "datamodelId", datamodelId);
		for (SubjectArcsCache arcCacheObject: arcCacheObjectsToRemove) {
			persister.remove(arcCacheObject);
		}
	}
}

///**
//* Generate a list of randomly selected arcs, which terminate with a Literal value
//* @param datamodelIdList a list of datamodel IDs to query
//* @param subjectClassUri the URI of the subject class, from which to walk
//* @param depth max number of steps to take from the subject
//* @param numberToSelect number of Arcs to select
//* @return a list of selected arcs, terminating with a literal value
//*/
//public static List<Arc> listFilteredRandomValuedArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth, int numberToSelect) {
//String baseSparql = getSparqlSelectFilteredValuedArcs(subjectClassUri, depth);
//QueryCriteria queryCriteria = new QueryCriteria();
//queryCriteria.addNamedModelIds(datamodelIdList);
//String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
//log.info("listFilteredRandomValuedArcs() using SPARQL:" + sparql);
//queryCriteria.setQuery(sparql);
//
//return listArcs(queryCriteria);
//}



///**
//* Generate a list of randomly selected arcs
//* @param datamodelIdList a list of datamodel IDs to query
//* @param subjectClassUri the URI of the subject class, from which to walk
//* @param depth max number of steps to take from the subject
//* @param numberToSelect number of Arcs to select
//* @return a list of selected arcs
//*/
//public static List<Arc> listFilteredRandomArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth, int numberToSelect) {
//String baseSparql = getSparqlSelectFilteredArcs(subjectClassUri, depth);
//QueryCriteria queryCriteria = new QueryCriteria();
//queryCriteria.addNamedModelIds(datamodelIdList);
//String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
//log.info("listFilteredRandomArcs() using SPARQL:" + sparql);
//queryCriteria.setQuery(sparql);
//
//return listArcs(queryCriteria);
//}

///**
//* Generate a list of randomly selected arcs, which terminate with a Literal value
//* @param datamodelIdList a list of datamodel IDs to query
//* @param subjectClassUri the URI of the subject class, from which to walk
//* @param depth max number of steps to take from the subject
//* @param numberToSelect number of Arcs to select
//* @return a list of selected arcs, terminating with a literal value
//*/
//public static List<Arc> listRandomValuedArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth, int numberToSelect) {
//String baseSparql = getSparqlSelectValuedArcs(subjectClassUri, depth);
//QueryCriteria queryCriteria = new QueryCriteria();
//queryCriteria.addNamedModelIds(datamodelIdList);
//String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
//log.info("listRandomValuedArcs() using SPARQL:" + sparql);
//queryCriteria.setQuery(sparql);
//
//return listArcs(queryCriteria);
//}


///**
//* Generate a list of randomly selected arcs
//* @param datamodelIdList a list of datamodel IDs to query
//* @param subjectClassUri the URI of the subject class, from which to walk
//* @param depth max number of steps to take from the subject
//* @param numberToSelect number of Arcs to select
//* @return a list of selected arcs
//*/
//public static List<Arc> listRandomArcs(Collection<String> datamodelIdList, String subjectClassUri, int depth, int numberToSelect) {
//String baseSparql = getSparqlSelectArcs(subjectClassUri, depth);
//QueryCriteria queryCriteria = new QueryCriteria();
//queryCriteria.addNamedModelIds(datamodelIdList);
//String sparql = Queryer.decorateSparql(baseSparql, "?pred1", 0, numberToSelect);
////log.info("Finding random Arcs using SPARQL:" + sparql);
//queryCriteria.setQuery(sparql);
//
//return listArcs(queryCriteria);
//}


///**
//* Get SPARQL for finding Arc properties of the given resource subject. Select
//* only Arcs that terminate with a literal value 
//* This query looks for Arcs with maximum of 3 steps.
//* @param subjectClassUri
//* @param depth the number of steps from the subject to traverse
//* @return
//*/
//public static String getSparqlSelectValuedArcs(String subjectClassUri, int depth) {
//String sparql = "SELECT DISTINCT ";
//for (int i=1; i<=depth; i++) {
//	sparql += "?pred" + i + " ";
//}
//sparql += "\n{ GRAPH ?anyGraph { \n" +
//	"?subject a <" + subjectClassUri + "> \n";
//String statements = "";
//String nextSubj = "?subject";
//for (int i=1; i<=depth; i++) {
//	String thisObj = "?obj" + i;
//	statements += "\n . " + nextSubj + " ?pred" + i + " " + thisObj;
//	sparql += ". OPTIONAL { " +
//			"FILTER( isLiteral(" + thisObj + ") ) " + 
//			statements + " } \n";
//	nextSubj = thisObj;
//}
//sparql += "} }";
//return sparql;
//}

///**
//* Get SPARQL for finding Arc properties of the given resource subject.  
//* This query looks for Arcs with maximum of 3 steps.
//* @param subjectClassUri
//* @param depth the number of steps from the subject to traverse
//* @return
//*/
//public static String getSparqlSelectValuedArcs2(String subjectClassUri, int depth) {
//String sparql = "SELECT DISTINCT ";
//for (int i=1; i<=depth; i++) {
//	sparql += "?pred" + i + " ";
//}
//sparql += "\n{ GRAPH ?anyGraph { \n" +
//	"?subject a <" + subjectClassUri + "> \n";
//if (depth >= 1) {
//	sparql +=	". ?subject ?pred1 ?obj1 \n" +
//			". { ";
//}
//
//String nextSubj = "?obj1";
//for (int i=2; i<=depth; i++) {
//	if (i > 2) {
//		sparql += " UNION ";
//	}
//	String thisObj = "?obj" + i;
//	sparql += "  { " + nextSubj + " ?pred" + i + " " + thisObj + " \n" +
//			"  . FILTER( bound(" + nextSubj + ") ) ";
//	if (i < depth) {
//		sparql += "  . OPTIONAL { FILTER( isLiteral(" + thisObj + ") ) } \n";
//	} else {
//		sparql += "  . FILTER( isLiteral(" + thisObj + ") ) \n";
//	}
//	sparql += " } \n";
//	nextSubj = thisObj;
//}
//sparql += "} } }";
//return sparql;
//}