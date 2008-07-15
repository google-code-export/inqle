package org.inqle.http.lookup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.http.lookup.util.HttpParameterParser;

public class LookupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4242876578599704824L;

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
		String siteId = HttpParameterParser.getParam(request, InqleInfo.SITE_ID_PARAM);
		if (siteId == null || siteId.length()==0) {
			respondIrregularity(HttpURLConnection.HTTP_BAD_REQUEST, "Irregularity: received no value for request parameter:" + InqleInfo.SITE_ID_PARAM);
			return;
		}
		
	}
	
	private void respondIrregularity(int status, String message) {
		log.info("Received valid RDF but no statements.");
		response.setStatus(status);
		out.println(message);
	}
}
