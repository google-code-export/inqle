package org.inqle.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {

	private static final String DEFAULT_DATE_MASK = "yyyy-MM-dd HH:mm:ss z";

	public static String getDateString(int timestamp, String mask) {
		long time_ms = 1000 * timestamp;
		Date date = new Date(time_ms);
		String dateStr = "";
		SimpleDateFormat df = new SimpleDateFormat(mask);
		dateStr = df.format(date);
		return dateStr;
	}
	
	public static String getDateString(Date date) {
		return getDateString(date, DEFAULT_DATE_MASK);
	}
	
	public static String getDateString(Date date, String mask) {
		String dateStr = "";
		SimpleDateFormat df = new SimpleDateFormat(mask);
		dateStr = df.format(date);
		return dateStr;
	}
	
	public static String getDateString(int timestamp) {
		return DateFormatter.getDateString(timestamp, DEFAULT_DATE_MASK);
	}
	
	/**
	 * Parses date in format YYYY-MM-DD into a long value, the number of seconds since 1970.
	 * @param starting_date
	 * @return long representation of the date, in seconds since 1970-01-01
	 */
	public static int getDateInt(String starting_date) {
		int defaultVal = -1;
		if (starting_date==null || starting_date.length() < 7) return defaultVal;

		String[] dateElements = starting_date.split("-");
		int year = Integer.parseInt(dateElements[0]);
		int month = Integer.parseInt(dateElements[1]);
		int date = Integer.parseInt(dateElements[2]);
		
		//create a Calendar object for computing dates
		Calendar calendar = Calendar.getInstance();
		calendar.setLenient(true);
		calendar.set(year, month-1, date);
		long numMilisecsSince1970 = calendar.getTime().getTime();
		int numSecsSince1970 = (int)(numMilisecsSince1970/1000);
		return numSecsSince1970;
	}
}
