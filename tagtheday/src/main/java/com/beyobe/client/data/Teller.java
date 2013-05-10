package com.beyobe.client.data;

import java.util.Date;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.Participant;
import com.beyobe.client.views.LoginView;
import com.beyobe.client.views.LoginView.Presenter;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class Teller {

	public static final int STATUS_ALREADY_RUNNING = 0;
	public static final int STATUS_LOGIN_COMPLETED = 1;
	public static final int STATUS_LOGIN_FAILED = -1;
	public static final int STATUS_LOGIN_TIMED_OUT = -2;
	private static final int STATUS_LOGIN_ERROR_CONNECTING = -3;
	private static final long MAX_COUNTER = 10000;
	private int loginStatus;

	private static Logger log = Logger.getLogger(Teller.class.getName());
//	private boolean awaitingLoginResponse = false;
	
	// A keeper of the timer instance in case we need to cancel it
	private Timer timeoutTimer = null;

	// An indicator when the computation should quit
	private boolean abortFlag = false;
	 
	private LoginView.Presenter loginPresenter;
	
	public int loginUser(final LoginView.Presenter loginPresenter, String login, String password) {
		this.loginPresenter = loginPresenter;
		String url = Constants.BASEURL_BEYOBE_SERVER + "/login?user=" + login + "&password=" + password;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		
		
		
		
		
		// Check to make sure the timer isn't already running.
	    if (timeoutTimer != null) {
	        Window.alert("Command is already running!");
	        return STATUS_ALREADY_RUNNING;
	    }

	    // Create a timer to abort if the RPC takes too long
	    timeoutTimer = new Timer() {
	      public void run() {
	        Window.alert("Timeout expired.");
	        abortFlag = true;
	        timeoutTimer = null;
	      }
	    };

	    // (re)Initialize the abort flag and start the timer.
	    abortFlag = false;
	    timeoutTimer.schedule(Constants.TIMEOUT_LOGIN); // timeout is in milliseconds
		
		try {
//			Window.alert("Sending request");
			Request request = builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
			    	log.warning("unable to connect to server for log in");
			    	cancelTimer();
			    	loginStatus = STATUS_LOGIN_TIMED_OUT;
			    }
	
			    public void onResponseReceived(Request request, Response response) {
//			    	Window.alert("Response received");
			    	
			    	cancelTimer();
			        
				    if (!abortFlag && 200 == response.getStatusCode()) {
//			    		Window.alert("Login response received: " + response.getText());
			    		JSONValue jv = JSONParser.parseStrict(response.getText());
						JSONObject pO = (JSONObject)jv;
						Participant p = new Participant();
						JSONNumber idV = (JSONNumber)pO.get("id");
						p.setId(Long.parseLong(idV.toString()));
						p.setName(pO.get("name").toString());
						String createdStr = pO.get("created").toString();
						Date created = DateTimeFormat.getFormat(Constants.FORMAT_DATE_TIME).parse(createdStr);
						p.setCreated(created);
						p.setLang(pO.get("lang").toString());
						App.participant = p;
						loginStatus = STATUS_LOGIN_COMPLETED;
						log.info("Set login status to: " + loginStatus);
						abortFlag = true;
						loginPresenter.goTo(new TagdayPlace());
				    } else {
				    	log.warning("Error logging in: " + response.getStatusText());
				    	loginStatus = STATUS_LOGIN_FAILED;
				    	log.info("Set login status to: " + loginStatus);
				    	abortFlag = true;
				    }
			    } 
		  });
		  
		} catch (RequestException e) {
			log.warning("unable to log in user " + login);
			e.printStackTrace();
			loginStatus = STATUS_LOGIN_ERROR_CONNECTING;
			abortFlag = true;
		}
		log.info("wait until request comes back or timer times out");
		long counter = 0;
		while (! abortFlag) {
			//increment a counter in case our timer fails us
			counter++;
			if(counter > MAX_COUNTER) abortFlag = true;
		}
		log.info("Returning " + loginStatus);
		return loginStatus;
	}
	
	// Stop the timeout timer if it is running
	private void cancelTimer() {
		if (timeoutTimer != null) {
		   timeoutTimer.cancel();
		   timeoutTimer = null;
		}
	}
}
