package com.beyobe.client;

import com.google.gwt.i18n.client.DateTimeFormat;


public class Constants {

	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:SS";
	public static final String BASEURL_BEYOBE_SERVER = "http://127.0.0.1:8888/";
	public static final int TIMEOUT_LOGIN = 4000;
	public static final int TIMEOUT_DATUM_CLIENT = 10000;
	public static DateTimeFormat DAY_FORMATTER = DateTimeFormat.getFormat("yyyy-MM-dd");
	
}
