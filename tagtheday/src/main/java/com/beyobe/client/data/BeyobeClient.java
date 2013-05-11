package com.beyobe.client.data;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.TagdayPlace;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class BeyobeClient {
	public static final int STATUS_ALREADY_RUNNING = 0;
	public static final int STATUS_COMPLETED = 1;
	public static final int STATUS_FAILED = -1;
	public static final int STATUS_TIMED_OUT = -2;
	private static final int STATUS_ERROR_CONNECTING = -3;
	public static final long MAX_COUNTER = 100000;
	private int status;

	private static Logger log = Logger.getLogger(BeyobeClient.class.getName());
//	private boolean awaitingLoginResponse = false;
	
	// A keeper of the timer instance in case we need to cancel it
	private Timer timeoutTimer = null;

	// An indicator when the computation should quit
	private boolean abortFlag = false;
	
	public int getUserParcel(String userName, String password) {
		
		String url = Constants.BASEURL_BEYOBE_SERVER + "/datum?userName=" + userName + "&password=" + password;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

		// Check to make sure the timer isn't already running.
	    if (timeoutTimer != null) {
	        Window.alert("A request is already running!");
	        return STATUS_ALREADY_RUNNING;
	    }
	    status = STATUS_ALREADY_RUNNING;

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
			Request request = builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
			    	log.warning("unable to connect to server for log in");
			    	cancelTimer();
			    	status = STATUS_TIMED_OUT;
			    }
	
			    public void onResponseReceived(Request request, Response response) {
//			    	Window.alert("Response received");
			    	
			    	cancelTimer();
			        
				    if (!abortFlag && 200 == response.getStatusCode()) {
						App.dataBus.refreshDataFromJson(response.getText());
						log.info("Received data and loaded: " + response.getText());
				    	abortFlag = true;
				    	App.placeController.goTo(new TagdayPlace());
				    } else {
				    	log.warning("Error logging in: " + response.getStatusText());
				    	status = STATUS_FAILED;
				    	log.info("Set login status to: " + status);
				    	abortFlag = true;
				    }
			    } 
		  });
		  
		} catch (RequestException e) {
			log.log(Level.WARNING, "unable to get data", e);
			e.printStackTrace();
			status = STATUS_ERROR_CONNECTING;
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
