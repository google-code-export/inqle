package org.inqle.data.rdf.jenabean.cache;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.util.ArcLister;
import org.inqle.data.rdf.jena.util.SubjectClassLister;

public class CacheTool {

	private static Logger log = Logger.getLogger(CacheTool.class);
	
	public static void invalidateDataCache(String datasetId) {
		log.info("IIIIIIIIIIIIIIIIIIII Invalidating cache for dataset: " + datasetId);
		SubjectClassLister.invalidateCache(datasetId);
		ArcLister.invalidateCache(datasetId);
	}

}
