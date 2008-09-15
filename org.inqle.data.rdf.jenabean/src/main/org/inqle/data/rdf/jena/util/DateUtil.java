package org.inqle.data.rdf.jena.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;

public class DateUtil {

	private static final String[] DATE_FORMATS_TO_TRY = {
		"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss",
		"yyyy/MM/dd", "yyyy-MM-dd", "yyyy.MM.dd"
	};
	
	public static XSDDateTime tryToParseDate(String putativeDateString) {
		XSDDateTime xsdDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		for (String dateMask: DATE_FORMATS_TO_TRY) {
			dateFormat.applyPattern(dateMask);
			Date parsedDate = null;
			try {
				parsedDate = dateFormat.parse(putativeDateString);
			} catch (ParseException e) {}
			if (parsedDate==null) continue;
			
			Calendar calendarVal = Calendar.getInstance();
			calendarVal.setTime(parsedDate);
			xsdDate = new XSDDateTime(calendarVal);
			if (xsdDate != null) {
				break;
			}
		}
		return xsdDate;
	}
	
}
