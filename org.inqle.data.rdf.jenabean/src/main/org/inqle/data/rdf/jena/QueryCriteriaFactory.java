package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;

public class QueryCriteriaFactory {

	private static Logger log = Logger.getLogger(QueryCriteriaFactory.class);
	
	public static QueryCriteria createQueryCriteriaForDatasetFunction(String datasetFunctionId) {
		return createQueryCriteriaForDatasetFunction(datasetFunctionId, null);
	}
	
	/**
	 * Create a QueryCriteria, prepared to search across all datasets of the
	 * specified datasetFunctionId.  If any reasoning is to be done, specify the inferenceRules
	 * as a string value.  Otherwise leave this parameter null.
	 * @param datasetFunctionId
	 * @param inferenceRules
	 * @return
	 */
	public static QueryCriteria createQueryCriteriaForDatasetFunction(String datasetFunctionId, String inferenceRules) {
		QueryCriteria returnQueryCriteria = new QueryCriteria();
		returnQueryCriteria.setInferenceRules(inferenceRules);
		Persister persister = Persister.getInstance();
		returnQueryCriteria.setTextIndex(persister.getIndex(datasetFunctionId));
		//query out the IDs of the ExternalDatasets of this function
		
		
//		AppInfo appInfo = persister.getAppInfo();
//		QueryCriteria queryCriteria = new QueryCriteria();
//		String sparql = getSparqlToFindDatasetsOfFunction(datasetFunctionId);
//		queryCriteria.setQuery(sparql);
//		log.info("SPARQL=" + sparql);
//		queryCriteria.addNamedModel(appInfo.getMetarepositoryDataset());
//		//RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
//		List<String> datasetIds = Queryer.selectSimpleList(queryCriteria, "datasetId");
		
		List<String> datasetIds = new ArrayList<String>();
		Collection<?> allExternalDatasets = persister.reconstituteAll(UserDatamodel.class);
		if (allExternalDatasets == null || allExternalDatasets.size()==0) {
			return returnQueryCriteria;
		}
		for (Object externalDatasetObj: allExternalDatasets) {
			UserDatamodel userDatamodel = (UserDatamodel)externalDatasetObj;
			Collection<String> datasetFunctions = userDatamodel.getDatasetFunctions();
			if (datasetFunctions != null && datasetFunctions.contains(datasetFunctionId)) {
				datasetIds.add(userDatamodel.getId());
			}
		}
		
		returnQueryCriteria.addDatamodelIds(datasetIds);
		
		log.info("Returning QueryCriteria for function " + datasetFunctionId + ", with these models:" + datasetIds);
		return returnQueryCriteria;
	}

//	private static String getSparqlToFindDatasetsOfFunction(String datasetFunctionId) {
//		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
//		" PREFIX xsd: <" + RDF.XSD + "> \n " + 
//		" SELECT ?datasetId \n " +
//		" { \n " +
//		" GRAPH ?g { \n " +
//		" ?datasetUri a inqle:UserDatamodel \n " +
////		" . ?datasetUri inqle:datasetFunctions \"" + datasetFunctionId + "\"^^xsd:string \n" +
//		" } }\n";
//		return sparql;
//	}
}
