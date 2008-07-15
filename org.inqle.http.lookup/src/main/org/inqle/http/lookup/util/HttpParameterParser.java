package org.inqle.http.lookup.util;

import javax.servlet.http.HttpServletRequest;

import org.inqle.core.util.InqleInfo;

public class HttpParameterParser {

	public static String getParam(HttpServletRequest request, String paramName) {
		String val = request.getParameter(paramName);
		if (val==null) {
			val = String.valueOf(request.getAttribute(InqleInfo.REGISTER_RDF_PARAM));
		}
		return val;
	}

	public static String getUrl(HttpServletRequest request) {
		String url = "http://" + request.getServerName();
		return url;
	}

}
