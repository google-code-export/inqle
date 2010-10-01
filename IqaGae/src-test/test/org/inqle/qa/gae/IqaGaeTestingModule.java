package test.org.inqle.qa.gae;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.inqle.qa.AppConstants;
import org.inqle.qa.QuestionBroker;
import org.inqle.qa.GenericLocalizedObjectFactory;
import org.inqle.qa.Queryer;
import org.inqle.qa.RuleFactory;
import org.inqle.qa.gae.AppConfig;
import org.inqle.qa.gae.GaeQuestionBroker;
import org.inqle.qa.gae.GaeGenericLocalizedObjectFactory;
import org.inqle.qa.gae.GaeQueryer;
import org.inqle.qa.gae.GaeQuestionRuleApplier;
import org.inqle.qa.gae.GaeRuleApplier;
import org.inqle.qa.gae.GaeRuleFactory;
import org.inqle.qa.RuleApplier;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;
import org.inqle.qa.gdata.SpreadsheetServiceProvider;
import org.inqle.qa.gdata.gae.GaeGdataSpreadsheetImporter;

import org.inqle.qa.QuestionRuleApplier;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;


public class IqaGaeTestingModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Queryer.class).to(GaeQueryer.class);
		bind(GdataSpreadsheetImporter.class).to(GaeGdataSpreadsheetImporter.class);
		bind(SpreadsheetService.class).toProvider(SpreadsheetServiceProvider.class);
		bind(QuestionBroker.class).to(GaeQuestionBroker.class);
		bind(GenericLocalizedObjectFactory.class).to(GaeGenericLocalizedObjectFactory.class);
		bind(RuleApplier.class).to(GaeRuleApplier.class);
		bind(QuestionRuleApplier.class).to(GaeQuestionRuleApplier.class);
		bind(RuleFactory.class).to(GaeRuleFactory.class);
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
	Properties getAppConfig(Logger log) {
		Properties properties = new Properties();
		String realPath = System.getProperty("INQLE_QA_PATH");
		String fullFileName = realPath + "/" + AppConstants.CONFIGURATION_FOLDER + "/" + AppConstants.TEST_PROPERTIES_FILE;
		log.info("Loading application properties.  fullFileName=" + fullFileName);
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
}
