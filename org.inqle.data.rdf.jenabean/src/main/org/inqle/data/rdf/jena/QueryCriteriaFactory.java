package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Persister;

public class QueryCriteriaFactory {

	private static Logger log = Logger.getLogger(QueryCriteriaFactory.class);
	
	public static QueryCriteria createQueryCriteriaForDatamodelPurpose(String datamodelPurposeId) {
		return createQueryCriteriaForDatamodelPurpose(datamodelPurposeId, null);
	}
	
	/**
	 * Create a QueryCriteria, prepared to search across all datamodels of the
	 * specified datamodelPurposeId.  If any reasoning is to be done, specify the inferenceRules
	 * as a string value.  Otherwise leave this parameter null.
	 * @param datamodelPurposeId
	 * @param inferenceRules
	 * @return
	 */
	public static QueryCriteria createQueryCriteriaForDatamodelPurpose(String datamodelPurposeId, String inferenceRules) {
		QueryCriteria returnQueryCriteria = new QueryCriteria();
		returnQueryCriteria.setInferenceRules(inferenceRules);
		Persister persister = Persister.getInstance();
		returnQueryCriteria.setTextIndex(persister.getIndex(datamodelPurposeId));
		//query out the IDs of the ExternalDatamodels of this purpose
		
		
//		AppInfo appInfo = persister.getAppInfo();
//		QueryCriteria queryCriteria = new QueryCriteria();
//		String sparql = getSparqlToFindDatamodelsOfPurpose(datamodelPurposeId);
//		queryCriteria.setQuery(sparql);
//		log.info("SPARQL=" + sparql);
//		queryCriteria.addNamedModel(appInfo.getMetarepositoryDatamodel());
//		//RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
//		List<String> datamodelIds = Queryer.selectSimpleList(queryCriteria, "datamodelId");
		
		List<String> datamodelIds = new ArrayList<String>();
		Collection<?> allExternalDatamodels = persister.reconstituteAll(PurposefulDatamodel.class);
		if (allExternalDatamodels == null || allExternalDatamodels.size()==0) {
			return returnQueryCriteria;
		}
		for (Object externalDatamodelObj: allExternalDatamodels) {
			PurposefulDatamodel userDatamodel = (PurposefulDatamodel)externalDatamodelObj;
			Collection<String> datamodelPurposes = userDatamodel.getDatamodelPurposes();
			if (datamodelPurposes != null && datamodelPurposes.contains(datamodelPurposeId)) {
				datamodelIds.add(userDatamodel.getId());
			}
		}
		
		returnQueryCriteria.addDatamodelIds(datamodelIds);
		
		log.info("Returning QueryCriteria for purpose " + datamodelPurposeId + ", with these models:" + datamodelIds);
		return returnQueryCriteria;
	}

//	private static String getSparqlToFindDatamodelsOfPurpose(String datamodelPurposeId) {
//		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
//		" PREFIX xsd: <" + RDF.XSD + "> \n " + 
//		" SELECT ?datamodelId \n " +
//		" { \n " +
//		" GRAPH ?g { \n " +
//		" ?datamodelUri a inqle:UserDatamodel \n " +
////		" . ?datamodelUri inqle:datamodelPurposes \"" + datamodelPurposeId + "\"^^xsd:string \n" +
//		" } }\n";
//		return sparql;
//	}
}
