package com.beyobe.client;

import com.google.gwt.i18n.client.DateTimeFormat;


public class Constants {
	public static final String CLIENT = "tagtheday";
	public static final String CLIENT_VERSION = "20130824-1";
	
	//LOCAL
	public static final String URL_BEYOBE_SERVICE = "http://127.0.0.1:8080/beyobe/service/";
	public static final String URL_BEYOBE_SIGNUP = "http://vlad.imcserver.ro/dpd/user/register";
//	public static final String URL_BEYOBE_SIGNUP = "http://localhost:8082/user/register";
	
	//UHURU
//	public static final String URL_BEYOBE_SERVICE = "http://qds-beyobe.uhurucloud.com/service/";
//	public static final String URL_BEYOBE_SIGNUP = "http://vlad.imcserver.ro/dpd/user/register";
	
	//STATIC
//	public static final String URL_BEYOBE_SERVICE = "http://service.beyobe.com/service/";
//	public static final String URL_BEYOBE_SIGNUP = "http://vlad.imcserver.ro/dpd/user/register";
	
	public static final Integer DEFAULT_TEXTFIELD_MAX_LENGTH = 50;
	public static final Integer DEFAULT_MEMOFIELD_MAX_LENGTH = 2000;
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:SS";
	
	public static final int MAXLENGTH_BUTTONLABEL = 14;
	public static final int MAXLENGTH_BUTTONTITLE = 40;
	
	public static final int TIMEOUT_LOGIN = 4000;
	public static final int TIMEOUT_PARCEL_CLIENT = 10000;
	public static DateTimeFormat DAY_FORMATTER = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	public static final int STATUS_ALREADY_RUNNING = 0;
	public static final int STATUS_COMPLETED = 1;
	public static final int STATUS_FAILED = -1;
	public static final int STATUS_TIMED_OUT = -2;
	public static final int STATUS_ERROR_CONNECTING = -3;
	public static final String SERVERACTION_LOGIN = "loginDrupal";
	public static final String SERVERACTION_STORE_QUESTION = "storeQuestion";
	public static final String SERVERACTION_STORE_DATUM = "storeDatum";
//	public static final int TIMEOUT_SIGNUP = 10000;
	public static final String SERVERACTION_SIGNUP = "signup";
	public static final String SERVERACTION_TESTUSERNAME = "testUsername";
	public static final int MINIMUM_PASSWORD_LENGTH = 1;
	public static final String SERVERACTION_SEARCH_QUESTIONS = "searchForQuestions";
	public static final String SERVERACTION_UNSUBSCRIBE = "unsubscribe";
	public static final String SERVERACTION_SAVE_UNSAVED = "saveUnsaved";

	

}
