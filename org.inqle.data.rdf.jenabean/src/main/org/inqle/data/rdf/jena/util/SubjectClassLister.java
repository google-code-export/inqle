package org.inqle.data.rdf.jena.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.JavaHasher;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Finder;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.cache.SubjectClassCache;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

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
	

	public static List<String> queryGetAllSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES);
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static List<String> queryGetUncommonSubjectClasses(String datasetId) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(datasetId);
		queryCriteria.setQuery(getSparqlSelectUncommonClasses());
		log.info("queryGetUncommonSubjectClasses() querying with: " + getSparqlSelectUncommonClasses());
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static List<String> queryGetUncommonSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(getSparqlSelectUncommonClasses());
		log.info("queryGetUncommonSubjectClasses() querying with: " + getSparqlSelectUncommonClasses());
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
		SubjectClassCache subjectClassCache = (SubjectClassCache)persister.reconstitute(SubjectClassCache.class, cacheId, true);
		if (subjectClassCache == null) return null;
		Collection<String> subjectClassUris = new ArrayList<String>();
		Collection<Resource> subjects =  subjectClassCache.getSubjectClasses();
		for (Resource subject: subjects) {
			subjectClassUris.add(subject.toString());
		}
		return subjectClassUris;
	}

	private static String getSubjectClassCacheId(String datasetId) {
		return JavaHasher.hashSha256(datasetId);
	}
	
	private static void cacheSubjectClasses(String datasetId, Collection<String> subjectClasses) {
		String cacheId = getSubjectClassCacheId(datasetId);
		Persister persister = Persister.getInstance();
		SubjectClassCache subjectClassCache = (SubjectClassCache)persister.reconstitute(SubjectClassCache.class, cacheId, true);
		if (subjectClassCache == null) {
			subjectClassCache = new SubjectClassCache();
			subjectClassCache.setId(cacheId);
			subjectClassCache.setDatasetId(datasetId);
		}
		List<Resource> subjectClassResources = new ArrayList<Resource>();
		for (String subjectClass: subjectClasses) {
			Resource subjectClassResource = ResourceFactory.createResource(subjectClass);
			subjectClassResources.add(subjectClassResource);
		}
		subjectClassCache.setSubjectClasses(subjectClassResources);
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
