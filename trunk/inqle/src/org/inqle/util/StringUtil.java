package org.inqle.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.mindprod.entities.StripEntities;

public class StringUtil {

	private static final String[] DATE_FORMATS_TO_TRY = {
		"yyyy-MM-dd",
		"MM/dd/yyyy",
		"d MMMM yyyy",
		"MMM yyyy",
		"MMMM yyyy",
		"MMM.d.yyyy",
		"MMM.d",
		"MMMM.d",
		"MMMMd",
		"yyyy",
		
	};

	public static String hashString(String unhashed) {		
		if (unhashed == null) return null;
		String hashed = unhashed.replaceAll("[^\\p{L}\\p{N}]", "");
		hashed = hashed.toLowerCase();
		return hashed;
	}

	public static String stripHtml(String text) {
		if (text==null) return "";
		String utf8Text = EncodingUtil.toUtf8(text);
		String strippedUtf8Text = StripEntities.stripHTMLEntities(utf8Text, " ".charAt(0));
		strippedUtf8Text = StripEntities.stripHTMLTags(strippedUtf8Text);
		return strippedUtf8Text;
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}

	public static Date parseDate(String rawDateStr) {
		if (rawDateStr == null || rawDateStr.trim().length()==0) return new Date();
		
		//first try to parse the entire string
		for (String format: DATE_FORMATS_TO_TRY) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				Date theDate = sdf.parse(rawDateStr);
				if (isBadDate(theDate)) continue;
				return theDate;
			} catch (ParseException e) {
				continue;
			}
		}
		
		//next try to parse individual parts
		String[] rawDateParts = rawDateStr.split("\\s+");
		for (String rawDatePart: rawDateParts) {
			for (String format: DATE_FORMATS_TO_TRY) {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				try {
					Date theDate = sdf.parse(rawDatePart);
					if (isBadDate(theDate)) continue;
					return theDate;
				} catch (ParseException e) {
					continue;
				}
			}
		}
		//as a last resort, return today's date
		return new Date();
	}

	public static boolean isBadDate(Date theDate) {
		Calendar earliestCal = Calendar.getInstance();
		earliestCal.set(Calendar.YEAR, 1700);
		Calendar latestCal = Calendar.getInstance();
		int thisYear = latestCal.get(Calendar.YEAR);
		latestCal.set(Calendar.YEAR, thisYear + 2);
		if (theDate.before(earliestCal.getTime())) return true;
		if (theDate.after(latestCal.getTime())) return true;
		return false;
	}
}
