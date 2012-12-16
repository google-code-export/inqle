package org.inqle.core.util;

public class CSVFormatter {

	public static String qualify(String str) {
		if (str == null) return "";
		//if no quotes, return the original string
		if (str.indexOf("\"") < 0) return str;
		
		//otherwise qualify
		String qualified = "\"" + escapeQuotes(str) + "\"";
		return qualified;
	}
	
	public static String escapeQuotes(String str) {
		if (str == null) return "";
		String escaped = str.replaceAll("\"", "'");
		return escaped;
	}
}
