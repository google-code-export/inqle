package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.http.lookup.util.HttpParameterParser;
import org.inqle.rdf.RDF;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class ExportServlet extends HttpServlet {

	/**
	 * Search the INQLE server for various things (subjects used, properties matching a subject, etc.
	 */
	private static final long serialVersionUID = -1109248678599705057L;

//	public static final String PARAM_EXPORT_FROM_DATABASE = "db";
	public static final String PARAM_EXPORT_FROM_DATAMODEL = "model";
	public static final String PARAM_URI_LIST = "uris";
	public static final String PARAM_RDF_FORMAT = "format";

	private static final int MAX_MODEL_SIZE = 10000;
	private static final int MAX_NUMBER_OF_SUBJECTS = 100;

	public static final String PATH = "export";
	
	private static Logger log = Logger.getLogger(ExportServlet.class);

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
//		String siteId = HttpParameterParser.getParam(request, InqleInfo.PARAM_SITE_ID);
//		if (siteId == null || siteId.length()==0) {
//			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: received no value for request parameter:" + InqleInfo.PARAM_SITE_ID);
//			return;
//		}
		
//		String db = HttpParameterParser.getParam(request, PARAM_EXPORT_FROM_DATABASE);		
		String datamodelid = HttpParameterParser.getParam(request, PARAM_EXPORT_FROM_DATAMODEL);
		String uris = HttpParameterParser.getParam(request, PARAM_URI_LIST);
		String format = HttpParameterParser.getParam(request, PARAM_RDF_FORMAT);
		if (format==null || format.length()==0) {
			format = JenabeanWriter.DEFAULT_RDF_LANG;
		}
		
		if (datamodelid == null || uris == null) {
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: did not receive all required parameters.\n" +
					"Must receive these parameters:\n" + PARAM_EXPORT_FROM_DATAMODEL + "\n" + PARAM_URI_LIST + "\n");
			return;
		}
		String[] uriArray = uris.split(",");
		
		if (uriArray.length==0) {
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: no subjects.\n" +
					"Must receive the URI of at least 1 subject to export.");
			return;
		}
		
		if (uriArray.length > MAX_NUMBER_OF_SUBJECTS) {
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: too many subjects.  Cannot export more than " + MAX_NUMBER_OF_SUBJECTS + " at a time.");
			return;
		}
		
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodelid);
		AppInfo appInfo = persister.getAppInfo();
		String baseUri = RDF.INQLE;
		try {
			baseUri = appInfo.getSite().getUriPrefix().getNamespaceUri();
		} catch (Exception e) {
			//leave as default
		}

		Model modelToExport  = ModelFactory.createDefaultModel();
		
		for (String uri: uriArray) {
			log.info("Exporting URI: " + uri);
			Resource subject = ResourceFactory.createResource(uri);
			if (subject==null || !subject.isResource()) {
				log.info("Unable to export resource: " + uri);
				continue;
			}
			StmtIterator stmtsI = model.listStatements(subject, (Property)null, (RDFNode)null);
			modelToExport.add(stmtsI);
			if (modelToExport.size() > MAX_MODEL_SIZE) {
				log.info("Export exceeds maximum size of " + MAX_MODEL_SIZE);
				modelToExport.close();
				respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: too many statements.  Cannot export more than " + MAX_MODEL_SIZE + " statements.");
				return;
			}
		}
		
		modelToExport.write(out, format, baseUri);
		log.info("Exported " + modelToExport.size() + " statements.");
		modelToExport.close();
	
	}
	
//	private void respondOK(String message) {
//		response.setStatus(HttpURLConnection.HTTP_OK);
//		out.println(message);
//	}
	
	private void respondIrregularity(int status, String message) {
//		log.info(message);
		response.setStatus(status);
		out.println(message);
	}
}
