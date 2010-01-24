package org.inqle.data.rdf.jena.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jenabean.Finder;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.cache.SubjectClassCache;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.rdf.RDF;

import com.hp.hpl.jena.query.ResultSetRewindable;

public class SubjectClassLister {

	private static Logger log = Logger.getLogger(SubjectClassLister.class);
	
	/**
	 * From the collection of the URIs of uncommon RDF subject classes, 
	 * randomly select a number of values.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param cacheDatabaseId the ID of the database in which the cache is stored
	 * @param datamodelId the datamodel to query
	 * @param size the size of the collection to return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<String> getRandomUncommonSubjectClasses(String cacheDatabaseId, String datamodelId, int size, Collection<String> subjectClassesToExclude) {
		Collection<String> availableSubjectClasses = getUncommonSubjectClasses(cacheDatabaseId, datamodelId);
		if (availableSubjectClasses == null) return null;
		
		Collection<String> randomSubjectClasses = (Collection<String>)RandomListChooser.chooseRandomItemsAdditively(availableSubjectClasses, subjectClassesToExclude, size);
		return randomSubjectClasses;
	}
	
	/**
	 * From the collection of the URIs of uncommon RDF subject classes, 
	 * randomly select a number of values.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param cacheDatabaseId the ID of the database in which the cache is stored
	 * @param datamodelId the datamodel to query
	 * @param size the size of the collection to return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<String> getRandomUncommonSubjectClasses(String cacheDatabaseId, Collection<String> datamodelIds, int size, Collection<String> subjectClassesToExclude) {
		Collection<String> availableSubjectClasses = getUncommonSubjectClasses(cacheDatabaseId, datamodelIds);
		if (availableSubjectClasses == null) return null;
		
		Collection<String> randomSubjectClasses = (Collection<String>)RandomListChooser.chooseRandomItemsAdditively(availableSubjectClasses, subjectClassesToExclude, size);
		return randomSubjectClasses;
	}
	
	/**
	 * Get a collection of the URIs of uncommon RDF subject classes.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param cacheDatabaseId the ID of the database in which the cache is stored
	 * @param datamodelId the datamodel to query
	 * @return
	 */
	public static Collection<String> getUncommonSubjectClasses(String cacheDatabaseId, String datamodelId) {
		Collection<String> subjectClasses = getSubjectClassesFromCache(datamodelId);
		if (subjectClasses != null) return subjectClasses;
		
		//not in cache: query then cache it
		subjectClasses = queryGetUncommonSubjectClasses(datamodelId);
		log.info("Queried, got subjectClasses=" + subjectClasses);
		cacheSubjectClasses(cacheDatabaseId, datamodelId, subjectClasses);
		return subjectClasses;
	}
	
	/**
	 * Get a collection of the URIs of uncommon RDF subject classes.  Uncommon
	 * subject classes include any RDFS/OWL class, excluding common ones
	 * like rdf:Property
	 * @param cacheDatabaseId the ID of the database in which the cache is stored
	 * @param datamodelIds the datamodels to query
	 * @return
	 */
	public static List<String> getUncommonSubjectClasses(String cacheDatabaseId, Collection<String> datamodelIds) {
		List<String> masterCollection = new ArrayList<String>();
		for (String datamodelId: datamodelIds) {
			masterCollection.addAll(getUncommonSubjectClasses(cacheDatabaseId, datamodelId));
		}
		return masterCollection;
	}

	public static final String SPARQL_SELECT_CLASSES = "SELECT DISTINCT " +
			"?classUri { GRAPH ?anyGraph { \n " +
			"?subject a ?classUri } } \n";

//	public static final String ResultSetTable.URI_VARIABLE = "Type_URI";
	
	private static final String SPARQL_SELECT_CLASSES_TABLE = "PREFIX rdfs: <" + RDF.RDFS + "> \n" +
			"SELECT DISTINCT " +
			"?" + InqleInfo.URI_VARIABLE + " ?Name ?Description { GRAPH ?anyGraph { \n " +
			"?subject a ?" + InqleInfo.URI_VARIABLE + " ." +
			"OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " rdfs:label ?Name} . \n" +
			"OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " rdfs:comment ?Description} . \n" +
			"} } \n";
	
	public static String getSparqlSelectUncommonClassesTable() {
		String s = "PREFIX rdfs: <" + RDF.RDFS + "> \n" +
		"SELECT DISTINCT " +
		"?" + InqleInfo.URI_VARIABLE + " ?Name ?Description { GRAPH ?anyGraph { \n " +
		"?subject a ?" + InqleInfo.URI_VARIABLE + " ." +
		"OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " rdfs:label ?Name} . \n" +
		"OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " rdfs:comment ?Description} . \n" +
//		 	Queryer.getSparqlClauseFilterCommonClasses("?" + InqleInfo.URI_VARIABLE) +
		"\n} } \n";
		
		return s;
	}

	public static List<String> queryGetAllSubjectClasses(Collection<String> datamodelIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodelIds(datamodelIdList);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES);
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static ResultSetRewindable queryGetAllSubjectsRS(String datamodelId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodel(datamodelId);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES_TABLE);
		return Queryer.selectResultSet(queryCriteria);
	}
	
	public static ResultSetRewindable queryGetUncommonSubjectsRS(String datamodelId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodel(datamodelId);
		queryCriteria.setQuery(getSparqlSelectUncommonClassesTable());
		return Queryer.selectResultSet(queryCriteria);
	}


	
	public static List<String> queryGetUncommonSubjectClasses(String datamodelId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodel(datamodelId);
		queryCriteria.setQuery(getSparqlSelectUncommonClasses());
		//log.info("queryGetUncommonSubjectClasses() querying with: " + getSparqlSelectUncommonClasses());
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static List<String> queryGetUncommonSubjectClasses(Collection<String> datamodelIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addDatamodelIds(datamodelIdList);
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
	
	private static Collection<String> getSubjectClassesFromCache(String datamodelId) {
		String cacheId = getSubjectClassCacheId(datamodelId);
		log.info("Getting from cache ID: " + cacheId);
		Persister persister = Persister.getInstance();
		SubjectClassCache subjectClassCache = null;
		try {
			subjectClassCache = (SubjectClassCache)persister.reconstitute(SubjectClassCache.class, cacheId, true);
		} catch (Exception e) {
			//unable to reconstitute; assume it does not exist
		}
		if (subjectClassCache == null) {
			log.info("Unable tofind cache ID: " + cacheId);
			return null;
		}
		log.info("getSubjectClassesFromCache() loaded from cache: " + JenabeanWriter.toString(subjectClassCache)); 
		Collection<String> subjectClassUris = new ArrayList<String>();
		Collection<URI> subjects =  subjectClassCache.getSubjectClasses();
		if (subjects==null) return null;
		for (URI subject: subjects) {
			subjectClassUris.add(subject.toString());
		}
		//log.info("...converted to this collection of URI strings:" + subjectClassUris);
		return subjectClassUris;
	}

	private static String getSubjectClassCacheId(String datamodelId) {
//		String cacheId = JavaHasher.hashSha256(datamodelId);
		String cacheId = "SCCacheId_" + datamodelId;
//		log.info("getSubjectClassCacheId(" + datamodelId + ")=" + cacheId);
		return cacheId;
	}
	
	/**
	 * Store the provided collection containing URIs of the classes to cache
	 * @param cacheDatabaseId the ID of the database in which to cache
	 * @param datamodelId the ID of the datamodel from which to draw the cache info
	 * @param subjectClasses the collection of class URIs to cache
	 */
	private static void cacheSubjectClasses(String cacheDatabaseId, String datamodelId, Collection<String> subjectClasses) {
		String cacheId = getSubjectClassCacheId(datamodelId);
		String cacheDatamodelId = Persister.getTargetDatamodelId(SubjectClassCache.class, cacheDatabaseId);
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
			subjectClassCache.setDatasetId(datamodelId);
		}
		List<URI> subjectClassURIs = new ArrayList<URI>();
		for (String subjectClass: subjectClasses) {
			URI subjectClassURI = URI.create(subjectClass);
			//log.info("Adding to cache: subject class:" + subjectClass + " = URI:" + subjectClassURI.toString());
			subjectClassURIs.add(subjectClassURI);
		}
		subjectClassCache.setSubjectClasses(subjectClassURIs);
		//log.info("Caching list of " + subjectClasses.size() + " subject classes for: datamodelId=" + datamodelId +
//				"\n" + JenabeanWriter.toString(subjectClassCache));
		persister.persist(subjectClassCache, cacheDatamodelId);
		
	}
	
	/**
	 * Remove all SubjectClassCache objects, which have the provided datamodelId
	 * @param cacheDatabaseId the ID of the database, where the cache is stored
	 * @param datamodelId
	 */
	@SuppressWarnings("unchecked")
	public static void invalidateCache(String cacheDatabaseId, String datamodelId) {
		Persister persister = Persister.getInstance();
		String cacheDatamodelId = Persister.getTargetDatamodelId(SubjectClassCache.class, cacheDatabaseId);
		Collection<SubjectClassCache> subjectClassCacheObjectsToRemove = 
			(Collection<SubjectClassCache>)Finder.listJenabeansWithStringValue(
					cacheDatamodelId, 
					SubjectClassCache.class, 
					RDF.INQLE + "datamodelId", 
					datamodelId);
		log.info("Retrieved these cache objects: " + subjectClassCacheObjectsToRemove);
		for (SubjectClassCache cacheObject: subjectClassCacheObjectsToRemove) {
			persister.remove(cacheObject, cacheDatamodelId);
		}
	}
}
