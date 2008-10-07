package org.inqle.data.rdf.jena;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;

public class QueryCriteriaFactory {

	private static Logger log = Logger.getLogger(QueryCriteriaFactory.class);
	
	public static QueryCriteria createQueryCriteriaForDatasetFunction(String datasetFunctionId) {
		QueryCriteria returnQueryCriteria = new QueryCriteria();
		
		//query out the IDs of the ExternalDatasets of this function
		
		Persister persister = Persister.getInstance();
		AppInfo appInfo = persister.getAppInfo();
		QueryCriteria queryCriteria = new QueryCriteria();
		String sparql = getSparqlToFindDatasetsOfFunction(datasetFunctionId);
		queryCriteria.setQuery(sparql);
		log.info("SPARQL=" + sparql);
		queryCriteria.addNamedModel(appInfo.getMetarepositoryDataset());
		//RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		List<String> datasetIds = Queryer.selectSimpleList(queryCriteria, "datasetId");
		
		returnQueryCriteria.addNamedModelIds(datasetIds);
		returnQueryCriteria.setTextIndex(persister.getIndex(datasetFunctionId));
		log.info("Returning QueryCriteria for function " + datasetFunctionId + ", with these models:" + datasetIds);
		return returnQueryCriteria;
	}

	private static String getSparqlToFindDatasetsOfFunction(String datasetFunctionId) {
		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
		" PREFIX xsd: <" + RDF.XSD + "> \n " + 
		" SELECT ?datasetId \n " +
		" { \n " +
		" GRAPH ?g { \n " +
		" ?datasetUri a inqle:ExternalDataset \n " +
		" . ?datasetUri inqle:datasetFunctions \"" + datasetFunctionId + "\"^^xsd:string \n" +
		" } }\n";
		return sparql;
	}
}
