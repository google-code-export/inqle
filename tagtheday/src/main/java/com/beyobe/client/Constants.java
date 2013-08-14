package com.beyobe.client;

import com.google.gwt.i18n.client.DateTimeFormat;


public class Constants {
	public static final String CLIENT = "beyobe.ttd.1";
	
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:SS";
	public static final String BASEURL_BEYOBE_SERVICE = "http://127.0.0.1:8080/beyobe/service/";
	public static final int TIMEOUT_LOGIN = 4000;
	public static final int TIMEOUT_PARCEL_CLIENT = 10000;
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
	public static final int TIMEOUT_SIGNUP = 10000;
	public static final String SERVERACTION_SIGNUP = "signup";
	public static final String SERVERACTION_TESTUSERNAME = "testUsername";
	public static final int MINIMUM_PASSWORD_LENGTH = 1;
	public static final String SERVERACTION_SEARCH_QUESTIONS = "searchForQuestions";
	public static final String SERVERACTION_UNSUBSCRIBE = "unsubscribe";
	
}
