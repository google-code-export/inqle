package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.util.HttpParameterParser;

public class LookupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4242876578599704824L;

	private static final int COUNT_SEARCH_RESULTS = 10;

	private static final int MAX_COUNT_RESULTS = 100;

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
		
		String startIndexString = HttpParameterParser.getParam(request, InqleInfo.PARAM_SEARCH_START_INDEX);
		int startIndex = 0;
		try {
			startIndex = Integer.parseInt(startIndexString);
		} catch (NumberFormatException e) {
			//leave default
		}
		
		String countResultsString = HttpParameterParser.getParam(request, InqleInfo.PARAM_SEARCH_COUNT_RESULTS);
		int countResults = COUNT_SEARCH_RESULTS;
		try {
			countResults = Integer.parseInt(countResultsString);
		} catch (NumberFormatException e) {
			//leave default
		}
		if (countResults > MAX_COUNT_RESULTS) {
			countResults = COUNT_SEARCH_RESULTS;
		}
		
		//lookup Class URI, of classes which are subclasses of DataSubject
		String searchTermForDataSubjectClass = HttpParameterParser.getParam(request, InqleInfo.PARAM_SEARCH_DATA_SUBJECT);
		if (searchTermForDataSubjectClass != null) {
			
//			//this looks up only sublcasses of DataSubject
//			String matchingClassesXml = SubjectLookup.lookupSubclasses(searchTermForDataSubjectClass, RDF.SUBJECT, Data.DATA_SUBJECT_DATASET_ROLE_ID, countResults, startIndex);
			//this looks up all RDF classes
			String matchingClassesXml = SubjectLookup.lookupSubclasses(searchTermForDataSubjectClass, null, Data.DATA_SUBJECT_DATASET_ROLE_ID, countResults, startIndex);

			respondOK(matchingClassesXml);
			return;
		}
		
		//lookup Class URI
		String searchTermForRdfClass = HttpParameterParser.getParam(request, InqleInfo.PARAM_SEARCH_RDF_CLASS);
		if (searchTermForRdfClass != null) {
			
			//String matchingClassesXml = SubjectLookup.lookupSubclasses(searchTermForRdfClass, null, Data.DATA_SUBJECT_DATASET_ROLE_ID, countResults, startIndex);
			String matchingClassesXml = SubjectLookup.lookupSubclassesInSchemaFiles(searchTermForRdfClass, countResults, startIndex);
			respondOK(matchingClassesXml);
			return;
		}
		
		String dataAndSubjectPropertiesForClass = HttpParameterParser.getParam(request, InqleInfo.PARAM_DATA_AND_SUBJECT_PROPERTIES_OF_SUBJECT);
		if (dataAndSubjectPropertiesForClass != null) {
			
			String matchingPropertiesXml = PropertyLookup.lookupAllDataProperties(
					dataAndSubjectPropertiesForClass, 
					countResults, 
					startIndex);
			respondOK(matchingPropertiesXml);
			return;
		}
		
		String propertiesForClassFromSchemaFiles = HttpParameterParser.getParam(request, InqleInfo.PARAM_PROPERTIES_OF_SUBJECT_FROM_SCHEMA_FILES);
		if (propertiesForClassFromSchemaFiles != null) {
			
			String matchingPropertiesXml = PropertyLookup.lookupPropertiesInSchemaFiles(
					propertiesForClassFromSchemaFiles, 
					countResults, 
					startIndex);
			respondOK(matchingPropertiesXml);
			return;
		}
	}
	
	private void respondOK(String message) {
//		log.info(message);
		response.setStatus(HttpURLConnection.HTTP_OK);
		out.println(message);
	}
	
	private void respondIrregularity(int status, String message) {
//		log.info(message);
		response.setStatus(status);
		out.println(message);
	}
}
