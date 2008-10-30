package org.inqle.data.rdf.jena.util;

import java.util.Collection;
import java.util.List;

import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;

public class SubjectClassLister {

	public static final String SPARQL_SELECT_CLASSES = "SELECT DISTINCT " +
			"?classUri { GRAPH ?anyGraph { \n " +
			"?subject a ?classUri } } \n";
	

	public static List<String> listAllSubjectClasses(Collection<String> datasetIdList) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModelIds(datasetIdList);
		queryCriteria.setQuery(SPARQL_SELECT_CLASSES);
		return Queryer.selectUriList(queryCriteria);
	}

}
