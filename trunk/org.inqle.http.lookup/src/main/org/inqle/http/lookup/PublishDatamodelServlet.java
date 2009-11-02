package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.util.HttpParameterParser;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * This servlet publishes a datamodel as RDF
 * @author David Donohue
 * Feb 25, 2009
 */
public class PublishDatamodelServlet extends HttpServlet {

	/**
	 * Search the INQLE server for various things (subjects used, properties matching a subject, etc.
	 */
	private static final long serialVersionUID = -4146002278599701246L;

	public static final String PARAM_EXPORT_FROM_DATAMODEL = "model";
	public static final String PARAM_RDF_FORMAT = "format";

	private static final int MAX_MODEL_SIZE = 40000;

	public static final String PATH = "datamodel";
	
	private static Logger log = Logger.getLogger(PublishDatamodelServlet.class);

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
			
		String datamodelid = HttpParameterParser.getParam(request, PARAM_EXPORT_FROM_DATAMODEL);
		String format = HttpParameterParser.getParam(request, PARAM_RDF_FORMAT);
		if (format==null || format.length()==0) {
			format = JenabeanWriter.DEFAULT_RDF_LANG;
		}
		
		if (datamodelid == null) {
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: did not receive all required parameters.\n" +
					"Must receive these parameters:\n" + PARAM_EXPORT_FROM_DATAMODEL);
			return;
		}
		
		Persister persister = Persister.getInstance();
		Model modelToExport = persister.getModel(datamodelid);
		AppInfo appInfo = persister.getAppInfo();
		String baseUri = RDF.INQLE;
		try {
			baseUri = appInfo.getSite().getUriPrefix().getNamespaceUri();
		} catch (Exception e) {
			//leave as default
		}
		
		
		if (modelToExport.size() > MAX_MODEL_SIZE) {
			log.info("Export exceeds maximum size of " + MAX_MODEL_SIZE);
			modelToExport.close();
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: too many statements.  Cannot export more than " + MAX_MODEL_SIZE + " statements.");
			return;
		}
		
		modelToExport.write(out, format, baseUri);
		log.info("Published model " + datamodelid + ", of " + modelToExport.size() + " statements, in RDF format " + format);
	}
	
	private void respondIrregularity(int status, String message) {
		response.setStatus(status);
		out.println(message);
	}
}
