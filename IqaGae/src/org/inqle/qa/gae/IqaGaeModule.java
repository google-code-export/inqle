package org.inqle.qa.gae;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;

import org.inqle.qa.AppConstants;
import org.inqle.qa.AskableQuestionFactory;
import org.inqle.qa.GenericLocalizedObjectFactory;
import org.inqle.qa.Queryer;
import org.inqle.qa.RuleApplier;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;
import org.inqle.qa.gdata.SpreadsheetServiceProvider;
import org.inqle.qa.gdata.gae.GaeGdataSpreadsheetImporter;

import org.inqle.qa.QuestionRuleApplier;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class IqaGaeModule extends AbstractModule implements Module {

	@Override
	protected void configure() {
		bind(Queryer.class).to(GaeQueryer.class);
		bind(GdataSpreadsheetImporter.class).to(GaeGdataSpreadsheetImporter.class);
		bind(SpreadsheetService.class).toProvider(SpreadsheetServiceProvider.class);
		bind(AskableQuestionFactory.class).to(GaeAskableQuestionFactory.class);
		bind(GenericLocalizedObjectFactory.class).to(GaeGenericLocalizedObjectFactory.class);
		bind(RuleApplier.class).to(GaeRuleApplier.class);
		bind(QuestionRuleApplier.class).to(GaeQuestionRuleApplier.class);
	}
	
	@Provides
	@Singleton
	EntityManagerFactory provideEmf() {
		EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
		return emfInstance;
	}
	
	@Provides
	@Singleton
	PersistenceManagerFactory providePmf() {
		PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		return pmfInstance;
	}
	
	@Provides
	@Singleton
	DatastoreService provideDatastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}


	@Provides
	@Singleton
	@AppConfig
	Properties getAppConfig(ServletContext servletContext) {
		Properties properties = new Properties();
		String realPath = servletContext.getRealPath("");
		String fullFileName = realPath + "/" + AppConstants.CONFIGURATION_FOLDER + "/" + AppConstants.PROPERTIES_FILE;
//		System.out.println("Loading application properties.  fullFileName=" + fullFileName);
		try {
			FileInputStream fis = new FileInputStream(fullFileName);
			properties.load(fis); 
		} catch (IOException e) {
			System.out.println("Unable to find configuration file: " + fullFileName);
			return null;
		}
		if (properties==null) {
			System.out.println("Properties is null");
			return null;
		}
		return properties;
	}
	
//	@Provides
//	SpreadsheetService provideSpreadSheetService(@AppConfig Properties properties) {
//		SpreadsheetService service = new SpreadsheetService(AppConstants.APP_ID + "-" + AppConstants.APP_VERSION);
//		try {
//			service.setUserCredentials(
//				properties.getProperty(AppConstants.PROP_GOOGLE_SPREADSHEET_ACCOUNT), 
//				properties.getProperty(AppConstants.PROP_GOOGLE_SPREADSHEET_PASSWORD));
//		} catch (AuthenticationException e) {
//			e.printStackTrace();
//		}
//		return service;
//	}
	
}
