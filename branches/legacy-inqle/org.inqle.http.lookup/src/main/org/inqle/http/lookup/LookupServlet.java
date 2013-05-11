package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.util.HttpParameterParser;
import org.w3c.dom.Document;

public class LookupServlet extends HttpServlet {

	/**
	 * Search the INQLE server for various things (subjects used, properties matching a subject, etc.
	 */
	private static final long serialVersionUID = -4242876578599704824L;

	public static final String PARAM_SEARCH_START_INDEX = "start";
	public static final String PARAM_SEARCH_COUNT_RESULTS = "count";
	public static final String PARAM_SEARCH_DATA_AND_PREFERRED_ONTOLOGY_CLASS = "searchDataAndPrefOntClass";
	public static final String PARAM_PROPERTIES_OF_DATA_AND_PREFERRED_ONTOLOGY = "propsDSPrefOnt";
	
	private static final int COUNT_SEARCH_RESULTS = 100;

	private static final int MAX_COUNT_RESULTS = 1000;

	private static final String PARAM_SPARQL_QUERY = "query";

	private static final String PARAM_MODEL_ID = "default-graph-uri";

	

	private static Logger log = Logger.getLogger(LookupServlet.class);

	private HttpServletResponse response;

	private PrintWriter out;
	
	@Override
	public void init() {
		//initialize the Persister
		Persister.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doWork(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doWork(request, response);
	}

	private void doWork(HttpServletRequest request, HttpServletResponse response) {
		this.response = response;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			respondIrregularity(HttpURLConnection.HTTP_INTERNAL_ERROR, "Unable to obtain PrintWriter from HttpServletResponse");
			return;
		}
		
		//every request needs a siteId
		String siteId = HttpParameterParser.getParam(request, InqleInfo.PARAM_SITE_ID);
		if (siteId == null || siteId.length()==0) {
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: received no value for request parameter:" + InqleInfo.PARAM_SITE_ID);
			return;
		}
		
//		log.info("LLLLLLLLLLLLLLLLLLLLLLLLLLL LookupServlet: Received request from site: " + siteId);
		
		String startIndexString = HttpParameterParser.getParam(request, PARAM_SEARCH_START_INDEX);
		int startIndex = 0;
		try {
			startIndex = Integer.parseInt(startIndexString);
		} catch (NumberFormatException e) {
			//leave default
		}
		
		String countResultsString = HttpParameterParser.getParam(request, PARAM_SEARCH_COUNT_RESULTS);
		int countResults = COUNT_SEARCH_RESULTS;
		try {
			countResults = Integer.parseInt(countResultsString);
		} catch (NumberFormatException e) {
			//leave default
		}
		if (countResults > MAX_COUNT_RESULTS) {
			countResults = COUNT_SEARCH_RESULTS;
		}
		
		//Below, do searches and return results
		
		//lookup Class URI of subjects which have been used by other INQLE servers
//		String searchTermForDataSubjectClass = HttpParameterParser.getParam(request, PARAM_SEARCH_DATA_SUBJECT);
//		if (searchTermForDataSubjectClass != null) {
//			
//			//this looks up all RDF classes
//			String matchingClassesXml = SubjectsSearcher.lookupSubclassesInInternalDatamodel(searchTermForDataSubjectClass, null, Data.DATA_SUBJECT_DATASET_ROLE_ID, countResults, startIndex);
//
//			respondOK(matchingClassesXml);
//			return;
//		}
		
//		//lookup Class URI from Schema datamodels
//		String searchTermForRdfClass = HttpParameterParser.getParam(request, PARAM_SEARCH_RDF_CLASS);
//		if (searchTermForRdfClass != null) {
//			
////			String matchingClassesXml = SubjectsSearcher.lookupSubclassesInSchemaDatamodels(searchTermForRdfClass, countResults, startIndex);
//			String matchingClassesXml = SubjectsSearcher.lookupUmbelSubjectsInSchemaDatamodels(searchTermForRdfClass, countResults, startIndex);
//			respondOK(matchingClassesXml);
//			return;
//		}
		
		//lookup Class URI from UMBEL ontologies, loaded in Schema datamodel
//		String searchTermForPreferredOntologyClass = HttpParameterParser.getParam(request, PARAM_SEARCH_PREFERRED_ONTOLOGY_CLASS);
//		if (searchTermForPreferredOntologyClass != null) {
//			
//			String matchingClassesXml = SubjectsSearcher.lookupPreferredOntologySubjectsInSchemaDatamodels(searchTermForPreferredOntologyClass, countResults, startIndex);
//			respondOK(matchingClassesXml);
//			return;
//		}
		
		/*
		 * lookup Class URI from both subjects which have been used by other INQLE servers 
		 * (in a system datamodel) and UMBEL schemas (in Schema datamodel)
		 */
		String searchTermDataUmbelClass = HttpParameterParser.getParam(
				request, PARAM_SEARCH_DATA_AND_PREFERRED_ONTOLOGY_CLASS);
//		log.info("LLLLLLLLLLLLLLLLLLLLLLLLLLLLL LOOKUP searchTermDataUmbelClass=" + searchTermDataUmbelClass);
		if (searchTermDataUmbelClass != null) {
			
			String matchingDataClassesXml = SubjectsSearcher.lookupSubclassesInInternalDatamodel(
					searchTermDataUmbelClass, 
					null, 
					Data.DATAMODEL_ID_FOR_DATA_SUBJECT, 
					countResults, 
					startIndex);
			Document matchingDataClassesDoc = XmlDocumentUtil.getDocument(matchingDataClassesXml);

			String matchingUmbelClassesXml = SubjectsSearcher.lookupPreferredOntologySubjectsInSchemaDatamodels(searchTermDataUmbelClass, countResults, startIndex);
			Document matchingUmbelClassesDoc = XmlDocumentUtil.getDocument(matchingUmbelClassesXml);
			
			Document mergedDocument = SparqlXmlUtil.merge(matchingDataClassesDoc, matchingUmbelClassesDoc);
			String mergedDocumentXml = XmlDocumentUtil.xmlToString(mergedDocument);
			
//			String mergedDocumentXml = XmlDocumentUtil.xmlToString(matchingDataClassesDoc);
			respondOK(mergedDocumentXml);
			
//			String testingXml = XmlDocumentUtil.xmlToString(matchingDataClassesDoc);
			return;
		}
		
//		String dataAndSubjectPropertiesForClass = HttpParameterParser.getParam(request, PARAM_DATA_AND_SUBJECT_PROPERTIES_OF_SUBJECT);
//		if (dataAndSubjectPropertiesForClass != null) {
//			
//			String matchingPropertiesXml = PropertyLookup.lookupAllDataProperties(
//					dataAndSubjectPropertiesForClass, 
//					countResults, 
//					startIndex);
//			respondOK(matchingPropertiesXml);
//			return;
//		}
		
//		String propertiesForClassFromSchemaFiles = HttpParameterParser.getParam(request, PARAM_PROPERTIES_OF_SUBJECT_FROM_SCHEMA_FILES);
//		if (propertiesForClassFromSchemaFiles != null) {
//			
//			String matchingPropertiesXml = PropertyLookup.lookupPropertiesInSchemaDatamodels(
//					propertiesForClassFromSchemaFiles, 
//					countResults, 
//					startIndex);
//			respondOK(matchingPropertiesXml);
//			return;
//		}
	
		/*
		 * lookup Property URIs from both subjects which have been used by other INQLE servers 
		 * (in an system datamodel) and UMBEL schemas (in Schema datamodel)
		 */		
		String propertiesForDataAndSubjectAndPreferredOntology = HttpParameterParser.getParam(
				request, PARAM_PROPERTIES_OF_DATA_AND_PREFERRED_ONTOLOGY);
//		log.info("LOOKUP propertiesForDataAndSubjectAndPreferredOntology=" + propertiesForDataAndSubjectAndPreferredOntology);
		if (propertiesForDataAndSubjectAndPreferredOntology != null) {
			String matchingDSPropertiesXml = PropertyLookup.lookupAllDataProperties(
					propertiesForDataAndSubjectAndPreferredOntology, 
					countResults, 
					startIndex);
			Document matchingDSPropertiesDoc = XmlDocumentUtil.getDocument(matchingDSPropertiesXml);
	
			String matchingPreferredOntologyPropertiesXml = PropertyLookup.lookupPropertiesInPreferredOntologyDatamodels(
					propertiesForDataAndSubjectAndPreferredOntology, 
					countResults, 
					startIndex);
			Document matchingPreferredOntologyPropertiesDoc = XmlDocumentUtil.getDocument(matchingPreferredOntologyPropertiesXml);
			
			Document mergedDocument = SparqlXmlUtil.merge(matchingDSPropertiesDoc, matchingPreferredOntologyPropertiesDoc);
			String mergedDocumentXml = XmlDocumentUtil.xmlToString(mergedDocument);
			respondOK(mergedDocumentXml);
			return;
		}
	
		String sparql = HttpParameterParser.getParam(request, PARAM_SPARQL_QUERY);
		if (sparql != null) {
			String modelId = HttpParameterParser.getParam(request, PARAM_MODEL_ID);
			QueryCriteria queryCriteria = new QueryCriteria();
			queryCriteria.setQuery(sparql);
			queryCriteria.addDatamodel(modelId);
			String resultXml = Queryer.selectXml(queryCriteria);
			respondOK(resultXml);
			return;
		}
	}
	
	private void respondOK(String message) {
		log.info(message);
		response.setStatus(HttpURLConnection.HTTP_OK);
		out.println(message);
	}
	
	private void respondIrregularity(int status, String message) {
		log.warn(message);
		response.setStatus(status);
		out.println(message);
	}
}