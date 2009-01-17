package org.inqle.data.rdf.jena.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Finder;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.cache.SubjectClassCache;

import com.hp.hpl.jena.query.ResultSetRewindable;

public class SubjectClassLister {

	private static Logger log = Logger.getLogger(SubjectClassLister.class);
	
	/**
	 * From the collection of the URIs of uncommon RDF subject classes, 
	 * randomly select a number of values.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param datasetId the dataset to query
	 * @param size the size of the collection to return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<String> getRandomUncommonSubjectClasses(String datasetId, int size, Collection<String> subjectClassesToExclude) {
		Collection<String> availableSubjectClasses = getUncommonSubjectClasses(datasetId);
		if (availableSubjectClasses == null) return null;
		
		Collection<String> randomSubjectClasses = (Collection<String>)RandomListChooser.chooseRandomItemsAdditively(availableSubjectClasses, subjectClassesToExclude, size);
		return randomSubjectClasses;
	}
	
	/**
	 * From the collection of the URIs of uncommon RDF subject classes, 
	 * randomly select a number of values.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param datasetId the dataset to query
	 * @param size the size of the collection to return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<String> getRandomUncommonSubjectClasses(Collection<String> datasetIds, int size, Collection<String> subjectClassesToExclude) {
		Collection<String> availableSubjectClasses = getUncommonSubjectClasses(datasetIds);
		if (availableSubjectClasses == null) return null;
		
		Collection<String> randomSubjectClasses = (Collection<String>)RandomListChooser.chooseRandomItemsAdditively(availableSubjectClasses, subjectClassesToExclude, size);
		return randomSubjectClasses;
	}
	
	/**
	 * Get a collection of the URIs of uncommon RDF subject classes.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param datasetId the dataset to query
	 * @return
	 */
	public static Collection<String> getUncommonSubjectClasses(String datasetId) {
		Collection<String> subjectClasses = getSubjectClassesFromCache(datasetId);
		if (subjectClasses != null) return subjectClasses;
		
		//not in cache: query then cache it
		subjectClasses = queryGetUncommonSubjectClasses(datasetId);
		//log.info("Queried, got subjectClasses=" + subjectClasses);
		cacheSubjectClasses(datasetId, subjectClasses);
		return subjectClasses;
	}
	
	/**
	 * Get a collection of the URIs of uncommon RDF subject classes.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param datasetIds the datasets to query
	 * @return
	 */
	public static List<String> getUncommonSubjectClasses(Collection<String> datasetIds) {
		List<String> masterCollection = new ArrayList<String>();
		for (String datasetId: datasetIds) {
			masterCollection.addAll(getUncommonSubjectClasses(datasetId));
		}
		return masterCollection;
	}

	public static final String SPARQL_SELECT_CLASSES = "SELECT DISTINCT " +
			"?classUri { GRAPH ?anyGraph { \n " +
			"?subject a ?classUri } } \n";

	public static final String CLASS_URI_VAR = "Type_URI";
	
	private static final String SPARQL_SELECT_CLASSES_TABLE = "PREFIX rdfs: <" + RDF.RDFS + "> \n" +
			"SELECT DISTINCT " +
			"?" + CLASS_URI_VAR + " ?Name ?Description { GRAPH ?anyGraph { \n " +
			"?subject a ?" + CLASS_URI_VAR + " ." +
			"OPTIONAL { ?" + CLASS_URI_VAR + " rdfs:label ?Name} . \n" +
			"OPTIONAL { ?" + CLASS_URI_VAR + " rdfs:comment ?Description} . \n" +
			"} } \n";
	
	public static String getSparqlSelectUncommonClassesTable() {
		String s = "PREFIX rdfs: <" + RDF.RDFS + "> \n" +
		"SELECT DISTINCT " +
		"?" + CLASS_URI_VAR + " ?Name ?Description { GRAPH ?anyGraph { \n " +
		"?subject a ?" + CLASS_URI_VAR + " ." +
		"OPTIONAL { ?" + CLASS_URI_VAR + " rdfs:label ?Name} . \n" +
		"OPTIONAL { ?" + CLASS_URI_VAR + " rdfs:comment ?Description} . \n" +
		 	Queryer.getSparqlClauseFilterCommonClasses("?" + CLASS_URI_VAR) +
		"\n} } \n";
		
		return s;
	}

	public static List<String> queryGetAllSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES);
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static ResultSetRewindable queryGetAllSubjectsRS(String datasetId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(datasetId);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES_TABLE);
		return Queryer.selectResultSet(queryCriteria);
	}
	
	public static ResultSetRewindable queryGetUncommonSubjectsRS(String datasetId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(datasetId);
		queryCriteria.setQuery(getSparqlSelectUncommonClassesTable());
		return Queryer.selectResultSet(queryCriteria);
	}


	
	public static List<String> queryGetUncommonSubjectClasses(String datasetId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(datasetId);
		queryCriteria.setQuery(getSparqlSelectUncommonClasses());
		//log.info("queryGetUncommonSubjectClasses() querying with: " + getSparqlSelectUncommonClasses());
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static List<String> queryGetUncommonSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(getSparqlSelectUncommonClasses());
		//log.info("queryGetUncommonSubjectClasses() querying with: " + getSparqlSelectUncommonClasses());
		return Queryer.selectUriList(queryCriteria);
	}

	private static String getSparqlSelectUncommonClasses() {
		String s = "SELECT DISTINCT " +
		"?classUri { GRAPH ?anyGraph { \n " +
		"?subject a ?classUri " +
		 	Queryer.getSparqlClauseFilterCommonClasses("?classUri") +
		"\n} } \n";
		
		return s;
	}
	
	private static Collection<String> getSubjectClassesFromCache(String datasetId) {
		String cacheId = getSubjectClassCacheId(datasetId);
		Persister persister = Persister.getInstance();
		SubjectClassCache subjectClassCache = null;
		try {
			subjectClassCache = (SubjectClassCache)persister.reconstitute(SubjectClassCache.class, cacheId, true);
		} catch (Exception e) {
			//unable to reconstitute; assume it does not exist
		}
		if (subjectClassCache == null) return null;
		//log.info("getSubjectClassesFromCache() loaded from cache: " + JenabeanWriter.toString(subjectClassCache)); 
		Collection<String> subjectClassUris = new ArrayList<String>();
		Collection<URI> subjects =  subjectClassCache.getSubjectClasses();
		if (subjects==null) return null;
		for (URI subject: subjects) {
			subjectClassUris.add(subject.toString());
		}
		//log.info("...converted to this collection of URI strings:" + subjectClassUris);
		return subjectClassUris;
	}

	private static String getSubjectClassCacheId(String datasetId) {
//		String cacheId = JavaHasher.hashSha256(datasetId);
		String cacheId = "SCCacheId_" + datasetId;
//		log.info("getSubjectClassCacheId(" + datasetId + ")=" + cacheId);
		return cacheId;
	}
	
	private static void cacheSubjectClasses(String datasetId, Collection<String> subjectClasses) {
		String cacheId = getSubjectClassCacheId(datasetId);
		Persister persister = Persister.getInstance();
		SubjectClassCache subjectClassCache = null;
		try {
			subjectClassCache = (SubjectClassCache)persister.reconstitute(SubjectClassCache.class, cacheId, true);
		} catch (RuntimeException e) {
			//assume this cache object does not exist
		}
		if (subjectClassCache == null) {
			subjectClassCache = new SubjectClassCache();
			subjectClassCache.setId(cacheId);
			subjectClassCache.setDatasetId(datasetId);
		}
		List<URI> subjectClassURIs = new ArrayList<URI>();
		for (String subjectClass: subjectClasses) {
			URI subjectClassURI = URI.create(subjectClass);
			//log.info("Adding to cache: subject class:" + subjectClass + " = URI:" + subjectClassURI.toString());
			subjectClassURIs.add(subjectClassURI);
		}
		subjectClassCache.setSubjectClasses(subjectClassURIs);
		//log.info("Caching list of " + subjectClasses.size() + " subject classes for: datasetId=" + datasetId +
//				"\n" + JenabeanWriter.toString(subjectClassCache));
		persister.persist(subjectClassCache);
		
	}
	
	/**
	 * Remove all SubjectClassCache objects, which have the provided datasetId
	 * @param datasetId
	 */
	@SuppressWarnings("unchecked")
	public static void invalidateCache(String datasetId) {
		Persister persister = Persister.getInstance();
		Dataset targetDataset = persister.getTargetDataset(SubjectClassCache.class);
		Collection<SubjectClassCache> subjectClassCacheObjectsToRemove = (Collection<SubjectClassCache>)Finder.listJenabeansWithStringValue(targetDataset, SubjectClassCache.class, RDF.INQLE + "datasetId", datasetId);
		for (SubjectClassCache cacheObject: subjectClassCacheObjectsToRemove) {
			persister.remove(cacheObject);
		}
	}

}
