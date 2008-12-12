package org.inqle.data.rdf.jena.util;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;

public class SubjectClassLister {

	private static Logger log = Logger.getLogger(SubjectClassLister.class);
	
	public static final String SPARQL_SELECT_CLASSES = "SELECT DISTINCT " +
			"?classUri { GRAPH ?anyGraph { \n " +
			"?subject a ?classUri } } \n";
	

	public static List<String> listAllSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES);
		return Queryer.selectUriList(queryCriteria);
	}
	
	public static List<String> listUncommonSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(getSparqlSelectUncommonClasses());
		log.info("listUncommonSubjectClasses() querying with: " + getSparqlSelectUncommonClasses());
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

}
