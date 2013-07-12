package com.beyobe.client.data;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.Parcel;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class ParcelClient {
	
	public static final long MAX_COUNTER = 100000;
	private int status;

	private static Logger log = Logger.getLogger(ParcelClient.class.getName());
//	private boolean awaitingLoginResponse = false;
	
	// A keeper of the timer instance in case we need to cancel it
	private Timer timeoutTimer = null;

	// An indicator when the computation should quit
	private boolean abortFlag = false;
	
	public int sendParcel(Parcel parcel, String action) {
		parcel.setSessionToken(App.sessionToken);
		AutoBean<Parcel> parcelAutoBean = AutoBeanUtils.getAutoBean(parcel);
		String jsonString = AutoBeanCodex.encode(parcelAutoBean).getPayload();
		String url = Constants.BASEURL_BEYOBE_SERVICE + action;
//		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
		builder.setHeader("Content-Type", "application/json");
//		builder.setHeader("Access-Control-Allow-Methods",
//	            "PUT, GET, POST, DELETE, OPTIONS");
//	    builder.setHeader("Access-Control-Allow-Headers", "Content-Type");
//	    builder.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8888");
		builder.setRequestData(jsonString);
		// Check to make sure the timer isn't already running.
	    if (timeoutTimer != null) {
//	        Window.alert("Please wait until prior process finishes then try again.");
	        return Constants.STATUS_ALREADY_RUNNING;
	    }
	    status = Constants.STATUS_ALREADY_RUNNING;

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
	    timeoutTimer.schedule(Constants.TIMEOUT_DATUM_CLIENT); // timeout is in milliseconds
		
		try {
//			Window.alert("Sending request");
			Request request = builder.sendRequest(jsonString, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
			    	log.warning("unable to connect to server for log in");
			    	cancelTimer();
			    	status = Constants.STATUS_TIMED_OUT;
			    }
	
			    public void onResponseReceived(Request request, Response response) {
//			    	Window.alert("Response received");
			    	
			    	cancelTimer();
			        
				    if (!abortFlag && 200 == response.getStatusCode()) {
						try {
							App.dataBus.refreshDataFromJson(response.getText());
						} catch (Exception e) {
							log.log(Level.SEVERE, "Error refreshing DataBus with new data", e);
						}
						log.info("Received data and loaded: " + response.getText());
				    	abortFlag = true;
				    } else {
				    	log.warning("Error communicating with server: " + response.getText() + "\n" + response.getHeadersAsString());
				    	status = Constants.STATUS_FAILED;
				    	log.info("Set login status to: " + status);
				    	abortFlag = true;
				    }
			    } 
		  });
		log.info("Builder sent request:" + builder.getRequestData());
		  
		} catch (RequestException e) {
			log.log(Level.WARNING, "unable to get data", e);
			e.printStackTrace();
			status = Constants.STATUS_ERROR_CONNECTING;
			abortFlag = true;
		}
		return status;
	}
	
	// Stop the timeout timer if it is running
	private void cancelTimer() {
		if (timeoutTimer != null) {
		   timeoutTimer.cancel();
		   timeoutTimer = null;
		}
	}

	public int getStatus() {
		return status;
	}
}
