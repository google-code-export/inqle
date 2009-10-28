package org.inqle.data.rdf.jenabean.cache;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.util.ArcLister;
import org.inqle.data.rdf.jena.util.SubjectClassLister;

public class CacheTool {

	private static Logger log = Logger.getLogger(CacheTool.class);
	
	/**
	 * Invalidate cached Arcs and cached subject classes
	 * @param cacheDatabaseId the ID of the database where the cache is stored
	 * @param datamodelId the ID of the datamodel from which to draw the cache
	 */
	public static void invalidateDataCache(String cacheDatabaseId, String datamodelId) {
		log.info("IIIIIIIIIIIIIIIIIIII Invalidating cache for datamodel: " + datamodelId);
		SubjectClassLister.invalidateCache(cacheDatabaseId, datamodelId);
		ArcLister.invalidateCache(cacheDatabaseId, datamodelId);
	}

}
