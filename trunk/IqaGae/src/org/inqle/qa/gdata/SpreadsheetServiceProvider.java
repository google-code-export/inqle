package org.inqle.qa.gdata;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.AppConstants;
import org.inqle.qa.gae.AppConfig;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SpreadsheetServiceProvider implements Provider<SpreadsheetService>{

	private final Logger logger;
	private final Properties properties;
	
	@Inject
	public SpreadsheetServiceProvider(@AppConfig Properties properties, Logger logger) {
		this.properties = properties;
		this.logger = logger;
	}
	
	@Override
	public SpreadsheetService get() {
		//set the HTTP proxy
		if (properties.getProperty("http.proxyHost") != null) System.setProperty("http.proxyHost", properties.getProperty("http.proxyHost"));
		if (properties.getProperty("http.proxyPort") != null) System.setProperty("http.proxyPort", properties.getProperty("http.proxyPort"));
		if (properties.getProperty("https.proxyHost") != null) System.setProperty("https.proxyHost", properties.getProperty("https.proxyHost"));
		if (properties.getProperty("https.proxyPort") != null) System.setProperty("https.proxyPort", properties.getProperty("https.proxyPort"));
		
		//get the service
		SpreadsheetService service = new SpreadsheetService(AppConstants.APP_ID + "-" + AppConstants.APP_VERSION);
		String userName = properties.getProperty(AppConstants.PROP_GOOGLE_SPREADSHEET_ACCOUNT);
		String password = properties.getProperty(AppConstants.PROP_GOOGLE_SPREADSHEET_PASSWORD);
		try {
			service.setUserCredentials(userName, password);
		} catch (AuthenticationException e) {
			logger.log(Level.WARNING, "Unable to authenticate to the Google Spreadsheet Service using userName=" + userName, e);
		}
		return service;
	}

}
