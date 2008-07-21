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
		
		String searchTermForRdfClass = HttpParameterParser.getParam(request, InqleInfo.PARAM_SEARCH_RDF_CLASS);
		if (searchTermForRdfClass != null) {
			Persister persister = Persister.getInstance();
			QueryCriteria queryCriteria = new QueryCriteria();
			queryCriteria.addNamedModel(persister.getInternalDataset(Data.OWL_CLASS_DATASET_ROLE_ID));
			IndexLARQ textIndex =  persister.getIndex(Data.OWL_CLASS_DATASET_ROLE_ID);
			queryCriteria.setTextIndex(textIndex);
			queryCriteria.setQuery(getSparqlSearchRdfClasses(searchTermForRdfClass, COUNT_SEARCH_RESULTS, 1));
			String matchingClassesXml = Queryer.selectXml(queryCriteria);
			log.info("Queried and got these matching results:\n" + matchingClassesXml);
			
//			Model matchingClassesModel = Queryer.selectRdf(queryCriteria);
//			log.info("Queried and got these matching RDF results:\n" + JenabeanWriter.modelToString(matchingClassesModel));
//			RdfTable matchingClasses = Queryer.selectRdfTable(queryCriteria);
//			log.info("Queried and got these matching results:\n" + RdfTableWriter.dataTableToString(matchingClasses));
			respondOK(matchingClassesXml);
		}
	}
	
	private String getSparqlSearchRdfClasses(String searchRdfClass, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX dc: <" + RDF.DC + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?classUri ?classLabel ?classComment ?score \n" +
//			"SELECT ?classUri ?score \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"(?classUri ?score) pf:textMatch '+" + searchRdfClass + "' \n" +
			". ?classUri a owl:Class \n" +
			". OPTIONAL { ?classUri rdfs:label ?classLabel }\n" +
			". OPTIONAL { ?classUri rdfs:comment ?classComment } \n" +
			"} } ORDER BY DESC(?score) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
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
