package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.RdfTableWriter;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.DataMapping;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.util.HttpParameterParser;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;

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
		String searchTermForRdfClass = HttpParameterParser.getParam(request, InqleInfo.PARAM_SEARCH_DATA_SUBJECT);
		if (searchTermForRdfClass != null) {
			
			String matchingClassesXml = OwlInstanceLookup.lookup(searchTermForRdfClass, RDF.DATA_SUBJECT, Data.DATA_SUBJECT_DATASET_ROLE_ID, countResults, startIndex);
			respondOK(matchingClassesXml);
		}
	}
	
	private void respondOK(String message) {
		log.info(message);
		response.setStatus(HttpURLConnection.HTTP_OK);
		out.println(message);
	}
	
	private void respondIrregularity(int status, String message) {
		log.info(message);
		response.setStatus(status);
		out.println(message);
	}
}
