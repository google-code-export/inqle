package com.beyobe.client;

import com.google.gwt.i18n.client.DateTimeFormat;


public class Constants {

	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:SS";
	public static final String BASEURL_BEYOBE_SERVICE = "http://localhost:8080/beyobe/service/";
	public static final int TIMEOUT_LOGIN = 4000;
	public static final int TIMEOUT_DATUM_CLIENT = 10000;
	public static DateTimeFormat DAY_FORMATTER = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	public static final int STATUS_ALREADY_RUNNING = 0;
	public static final int STATUS_COMPLETED = 1;
	public static final int STATUS_FAILED = -1;
	public static final int STATUS_TIMED_OUT = -2;
	public static final int STATUS_ERROR_CONNECTING = -3;
	public static final String SERVERACTION_LOGIN = "login";
	public static final String SERVERACTION_STORE_QUESTION = "storeQuestion";
	public static final String SERVERACTION_STORE_DATUM = "storeDatum";
	public static final Integer DEFAULT_TEXTFIELD_MAX_LENGTH = 50;
	public static final Integer DEFAULT_MEMOFIELD_MAX_LENGTH = 2000;
	
}
