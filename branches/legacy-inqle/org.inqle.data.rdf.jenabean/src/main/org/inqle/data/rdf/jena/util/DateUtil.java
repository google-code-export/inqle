package org.inqle.data.rdf.jena.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;

public class DateUtil {

	private static Logger log = Logger.getLogger(DateUtil.class);
	
	private static final String[] DATE_FORMATS_TO_TRY = {
		"M/d/y",
		"yyyy/MM/dd HH:mm:ss", 
		"yyyy-MM-dd HH:mm:ss", 
		"yyyy.MM.dd HH:mm:ss",
		"yyyy/MM/dd", 
		"yyyy-MM-dd", 
		"yyyy.MM.dd"
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
			log.info("Parsed date: " + putativeDateString + " using mask: " + dateMask);
			Calendar calendarVal = Calendar.getInstance();
			calendarVal.setTime(parsedDate);
			xsdDate = new XSDDateTime(calendarVal);
			if (xsdDate != null) {
				break;
			}
		}
		return xsdDate;
	}
	
	public static Date tryToParseDateObject(String putativeDateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		Date parsedDate = null;
		for (String dateMask: DATE_FORMATS_TO_TRY) {
			dateFormat.applyPattern(dateMask);
			
			try {
				parsedDate = dateFormat.parse(putativeDateString);
			} catch (ParseException e) {}
			if (parsedDate!=null) break;
		}
		return parsedDate;
	}
	
}
