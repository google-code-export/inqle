package org.inqle.http.lookup;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class LookupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4242876578599704824L;

	private static Logger log = Logger.getLogger(LookupServlet.class);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRReceived Get request: " + request);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRReceived Post request: " + request);
	}
}
