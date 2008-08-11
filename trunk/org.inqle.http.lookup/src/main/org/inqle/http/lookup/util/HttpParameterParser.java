package org.inqle.http.lookup.util;

import javax.servlet.http.HttpServletRequest;

import org.inqle.core.util.InqleInfo;

public class HttpParameterParser {

	public static String getParam(HttpServletRequest request, String paramName) {
		String val = request.getParameter(paramName);
		if (val==null) {
			val = (String)request.getAttribute(InqleInfo.PARAM_REGISTER_RDF);
		}
		return val;
	}

	public static String getUrl(HttpServletRequest request) {
		String url = "http://" + request.getServerName();
		return url;
	}

}
