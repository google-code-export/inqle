package org.inqle.util;

import java.io.UnsupportedEncodingException;

public class EncodingUtil {

	public static String toUtf8(String str) {
		if (str==null) return null;
		String converted = str;
		try {
      // Convert from Unicode to UTF-8
      byte[] utf8 = converted.getBytes("UTF-8");
  
      // Convert from UTF-8 to Unicode
      converted = new String(utf8, "UTF-8");
	  } catch (UnsupportedEncodingException e) {
	  }
	  return converted;
	}
}
