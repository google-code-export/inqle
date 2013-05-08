package com.beyobe.client.data;

import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.beans.Participant;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public class Teller {

	private static final String SERVER_URL = "http://localhost:8080";

	private static final long LOGIN_TIMEOUT_MILLIS = 1000 * 3;

	private static Logger log = Logger.getLogger(Teller.class.getName());
	private boolean awaitingLoginResponse = false;
	
	public boolean loginUser(String login, String password) {
		String url = SERVER_URL + "?user=" + login + "&password=" + password;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		awaitingLoginResponse = true;
		try {
		  Request request = builder.sendRequest(null, new RequestCallback() {
		    public void onError(Request request, Throwable exception) {
		    	log.warning("unable to connect to server for log in");
		    	awaitingLoginResponse = false;
		    }

		    public void onResponseReceived(Request request, Response response) {
		      if (200 == response.getStatusCode()) {
		    	  Window.alert("Login response received: " + response.getText());
		    	  //TODO process JSON, create Participant, add to App
		    	  JSONValue jv = JSONParser.parseStrict(response.getText());
			      JSONObject pO = (JSONObject)jv;
			      Participant p = new Participant();
			      JSONNumber idV = (JSONNumber)pO.get("id");
			      p.setId(Long.parseLong(idV.toString()));
			      JSONValue nameV = 
			      p.setName()
		    	  awaitingLoginResponse = false;
			        
		    	  awaitingLoginResponse = false;
		      } else {
		        log.warning("Error logging in: " + response.getStatusText());
		      }
		    }       
		  });
		  boolean timedOut = false;
		  long start = System.currentTimeMillis();
		  while(awaitingLoginResponse && ! timedOut) {
			  timedOut = ((System.currentTimeMillis() - start) > LOGIN_TIMEOUT_MILLIS);
		  }
		  return App.isUserLoggedIn();
		  
		} catch (RequestException e) {
		  log.warning("unable to log in user " + login);
		} finally {
			return false;
		}
	}
}
