package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.RdfTableWriter;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.DataMapping;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.util.HttpParameterParser;

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
		log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRReceived Get request: " + request);
		doWork(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRReceived Post request: " + request);
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
			queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
			//todo add model which contains classes and their labels & comment fields
			queryCriteria.setQuery(getSparqlSearchRdfClasses(searchTermForRdfClass, COUNT_SEARCH_RESULTS, 1));
			RdfTable matchingClasses = Queryer.selectRdfTable(queryCriteria);
			log.info("Queried and got these matching results:\n" + RdfTableWriter.dataTableToString(matchingClasses));
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
			"{\n" +
			"GRAPH ?g {\n" +
			"?classUri a owl:Class\n" +
			". OPTIONAL { ?classUri rdfs:classLabel ?classLabel }\n" +
			". OPTIONAL { ?classUri rdfs:comment ?classComment } \n" +
			". (?stringLiteral ?score ) pf:textMatch '*" + searchRdfClass + "*' \n" +
			". ?classUri ?p ?stringLiteral \n" +
			"} } ORDER BY DESC(?score) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}

	private void respondIrregularity(int status, String message) {
		log.info(message);
		response.setStatus(status);
		out.println(message);
	}
}
