/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.SDBDatabase;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.UserDatamodel;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.InternalConnection;
import org.inqle.data.rdf.jena.LocalFolderDatabase;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jena.sdb.SDBConnector;
import org.inqle.data.rdf.jena.uri.NamespaceMapping;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.Site;
import org.inqle.data.rdf.jenabean.UserAccount;
import org.inqle.ui.rap.pages.ConnectionPage;
import org.inqle.ui.rap.pages.EmbeddedDBPage;
import org.inqle.ui.rap.pages.InfoPage;
import org.inqle.ui.rap.pages.RadiosPage;
import org.inqle.ui.rap.pages.ServerInfoPage;
import org.inqle.ui.rap.pages.SingleTextPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 20, 2008
 */
public class AppInfoWizard extends Wizard {

//	private static final String[] OPTIONS_EMBEDDED_OR_NOT = {
//		"Embedded H2 Database (recommended)",
//		"External Database"
//	};
//
//	private static final int EMBEDDED_H2_DATABASE = 0;
//
//	private static final String H2_DB_URL_BASE = "jdbc:h2:";
//
//	private static final String H2_DB_CLASS = "org.h2.Driver";
//
//	private static final String H2_DB_TYPE = "H2";

	
//	private static final String DEFAULT_INTERNAL_DB_NAME = "inqle_internal";
//	private static final String DEFAULT_INTERNAL_DB_USER_NAME = "inqle";
//	private static final String DEFAULT_CACHE_DB_NAME = "inqle_cache";
//	private static final String DEFAULT_CACHE_DB_USER_NAME = "inqle";
//	private static final String DEFAULT_FIRSTDATA_DB_NAME = "inqle_data1";
//	private static final String DEFAULT_FIRSTDATA_DB_USER_NAME = "inqle";
	
	private static final String FIRSTDATA_DATAMODEL_ID = "data1";
	
	private AppInfo appInfo = new AppInfo();
	private Shell shell;
//	private IDatabase metarepositoryConnection;
//	private SystemDatamodel metarepositoryDataset;
//	
//	private IDatabase firstDataDatabase;
//	private UserDatamodel firstDataDataset;
	
	private ServerInfoPage serverInfoPage;
	private UserAccountPage userAccountPage;
	
//	private RadiosPage embeddedOrExternalFirstDataDBPage;
//	private EmbeddedDBPage embeddedFirstDataDBPage;
//	private ConnectionPage firstDataConnectionPage;
//	private SingleTextPage firstDataDatasetPage;
//	private RadiosPage embeddedOrExternalCacheDBPage;
//	private EmbeddedDBPage embeddedCacheDBPage;
//	private InternalConnection cacheConnection;
//	private ConnectionPage cacheConnectionPage;
//	private RadiosPage embeddedOrExternalMetarepositoryDBPage;
//	private EmbeddedDBPage embeddedMetarepositoryDBPage;
//	private ConnectionPage metarepositoryConnectionPage;
//	private SingleTextPage metarepositoryDatasetPage;
	
	
	public AppInfoWizard(Shell parentShell) {
		this.shell = parentShell;
	}
	
	private static Logger log = Logger.getLogger(AppInfoWizard.class);
	@Override
	public void addPages() {
		log.info("addPages()");
//		SingleTextPage siteUrlPage = new SingleTextPage(appInfo, "serverBaseUrl", "Server URL", null);
//		siteUrlPage.setLabelText("Enter base URL of this INQLE server");
//		addPage(siteUrlPage);
		//Persister persister = Persister.getInstance();
		
		InfoPage firstPage = new InfoPage(
				"INQLE Set-up Wizard",
				"Welcome to INQLE!",
				"This wizard will help you set up your INQLE server.");
		addPage(firstPage);
		
		serverInfoPage = new ServerInfoPage();
		addPage(serverInfoPage);
//		log.info("added serverInfoPage");
		
		userAccountPage = new UserAccountPage();
		addPage(userAccountPage);
		
//		//Add pages for capturing the internal database connection and metarepository dataset
//		embeddedOrExternalMetarepositoryDBPage = new RadiosPage("We will create the internal database used by your INQLE server.", "Select whether to use an embedded database to use for the internal database.");
//		embeddedOrExternalMetarepositoryDBPage.setRadioOptionTexts(Arrays.asList(OPTIONS_EMBEDDED_OR_NOT));
//		addPage(embeddedOrExternalMetarepositoryDBPage);
////		log.info("added embeddedOrExternalMetarepositoryDBPage");
//		
//		embeddedMetarepositoryDBPage = new EmbeddedDBPage("Internal INQLE Database", "Specify connection info for the embedded H2 database, which will contain internal INQLE information.");
//		embeddedMetarepositoryDBPage.setDefaultDBName(DEFAULT_INTERNAL_DB_NAME);
//		embeddedMetarepositoryDBPage.setDefaultUserName(DEFAULT_INTERNAL_DB_USER_NAME);
//		addPage(embeddedMetarepositoryDBPage);
//		log.info("added embeddedMetarepositoryDBPage");
		
//		metarepositoryDataset = appInfo.getMetarepositoryDataset();
//		if (metarepositoryDataset == null) {
//			metarepositoryDataset = new SystemDatamodel();
//			//metarepositoryDataset.setModelName("Metarepository");
//			metarepositoryDataset.setId(DEFAULT_METAREPOSITORY_ID);
//		}
//		metarepositoryConnection = appInfo.getInternalConnection();
//		if (metarepositoryConnection == null) {
//			metarepositoryConnection = new SDBDatabase();
//			metarepositoryConnection.setRandomId();
//			metarepositoryDataset.setConnectionId(metarepositoryConnection.getId());
//		}
////		SDBDatabase metarepositoryConnection = metarepositoryRdbModel.getConnection();
//		metarepositoryConnectionPage = new ConnectionPage(
//				"Specify database connection info for your INQLE server", 
//				metarepositoryConnection, 
//				shell
//		);
//		addPage(metarepositoryConnectionPage);
		
//		metarepositoryDatasetPage = new SingleTextPage(
//				metarepositoryDataset, 
//				"id", 
//				"Enter a name for your Metarepository Model.", 
//				null
//		);
//		addPage(metarepositoryDatasetPage);
//		log.info("added metarepositoryDatasetPage");
		
//		embeddedOrExternalCacheDBPage = new RadiosPage("We will create the CACHE database used by your INQLE server.", "Select whether to use an embedded database to use for the INQLE CACHE database.");
//		embeddedOrExternalCacheDBPage.setRadioOptionTexts(Arrays.asList(OPTIONS_EMBEDDED_OR_NOT));
//		addPage(embeddedOrExternalCacheDBPage);
		
//		embeddedCacheDBPage = new EmbeddedDBPage("INQLE Cache Database", "Specify connection info for the embedded H2 database, which will contain INQLE caching information.");
//		embeddedCacheDBPage.setDefaultDBName(DEFAULT_CACHE_DB_NAME);
//		embeddedCacheDBPage.setDefaultUserName(DEFAULT_CACHE_DB_USER_NAME);
//		addPage(embeddedCacheDBPage);
		
//		cacheConnection = new InternalConnection();
//		cacheConnection.setConnectionRole(Persister.CACHE_CONNECTION);
//		cacheConnection.setRandomId();
		
//		SDBDatabase metarepositoryConnection = metarepositoryRdbModel.getConnection();
//		cacheConnectionPage = new ConnectionPage(
//				"Specify database connection info for cached data", 
//				cacheConnection, 
//				shell
//		);
//		addPage(cacheConnectionPage);
		
//		//add form elements for the first external dataset
//		embeddedOrExternalFirstDataDBPage = new RadiosPage("Next, we will create a database for storing your data.", "Select whether to use an embedded database, in which to store your data.");
//		embeddedOrExternalFirstDataDBPage.setRadioOptionTexts(Arrays.asList(OPTIONS_EMBEDDED_OR_NOT));
//		addPage(embeddedOrExternalFirstDataDBPage);
//		log.info("added embeddedOrExternalFirstDataDBPage");
		
//		embeddedFirstDataDBPage = new EmbeddedDBPage("Database for storing your data", "Specify connection info for the embedded database, in which to store your data.");
//		embeddedFirstDataDBPage.setDefaultDBName(DEFAULT_FIRSTDATA_DB_NAME);
//		embeddedFirstDataDBPage.setDefaultUserName(DEFAULT_FIRSTDATA_DB_USER_NAME);
//		addPage(embeddedFirstDataDBPage);
//		log.info("added embeddedFirstDataDBPage");
		
//		firstDataDatabase = new SDBDatabase();
//		firstDataDatabase.setRandomId();

		
//		SDBDatabase firstDataDatabase = firstDataRdbModel.getConnection();
//		firstDataConnectionPage = new ConnectionPage(
//				"Specify database connection info for your for first dataset", 
//				firstDataDatabase, 
//				shell
//		);
//		addPage(firstDataConnectionPage);
		
//		firstDataDatasetPage = new SingleTextPage(
//				firstDataDataset, 
//				"id", 
//				"Enter a unique name for your first dataset, e.g. " + DEFAULT_FIRSTDATA_DATASET_ID, 
//				null
//		);
//		addPage(firstDataDatasetPage);
//		log.info("added firstDataDatasetPage");
	}

//	@Override
//	public IWizardPage getNextPage(IWizardPage page) {
//		log.trace("getNextPage(" + page + ")");
//		if (page == embeddedOrExternalMetarepositoryDBPage) {
//			if (embeddedOrExternalMetarepositoryDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
//				return embeddedMetarepositoryDBPage;
//			} else {
//				return metarepositoryConnectionPage;
//			}
//		}
//		
//		if (page == embeddedMetarepositoryDBPage || page == metarepositoryConnectionPage) {
//			return metarepositoryDatasetPage;
//		}
//
//		if (page == embeddedOrExternalCacheDBPage) {
//			if (embeddedOrExternalCacheDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
//				return embeddedCacheDBPage;
//			} else {
//				return cacheConnectionPage;
//			}
//		}
//		
//		if (page == embeddedCacheDBPage || page == cacheConnectionPage) {
//			return embeddedOrExternalFirstDataDBPage;
//		}
//		
//		if (page == embeddedOrExternalFirstDataDBPage) {
//			if (embeddedOrExternalFirstDataDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
//				return embeddedFirstDataDBPage;
//			} else {
//				return firstDataConnectionPage;
//			}
//		}
//		
//		if (page == embeddedFirstDataDBPage || page == firstDataConnectionPage) {
//			return firstDataDatasetPage;
//		}
//		return super.getNextPage(page);
//	}

	@Override
	public boolean canFinish() {
		//getContainer().getCurrentPage().getControl().forceFocus();
		//TODO test that prefix & subjectclass are URIs
		try {
			//First data dataset forms
//			if (embeddedOrExternalFirstDataDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
//				//embedded database:
//				if (embeddedFirstDataDBPage.getDbName().length()==0) return false;
//				if (embeddedFirstDataDBPage.getDbLogin().length()==0) return false;
//				if (embeddedFirstDataDBPage.getDbPassword().length()==0) return false;
//			} else {
//				//external database:
//				if (firstDataConnectionPage.getDbType().length()==0) return false;
//				if (firstDataConnectionPage.getDbURL().length()==0) return false;
//				if (firstDataConnectionPage.getDbClass().length()==0) return false;
//			}
//			
//			//Cache forms
//			if (embeddedOrExternalCacheDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
//				//embedded database:
//				if (embeddedCacheDBPage.getDbName().length()==0) return false;
//				if (embeddedCacheDBPage.getDbLogin().length()==0) return false;
//				if (embeddedCacheDBPage.getDbPassword().length()==0) return false;
//			} else {
//				//external database:
//				if (cacheConnectionPage.getDbType().length()==0) return false;
//				if (cacheConnectionPage.getDbURL().length()==0) return false;
//				if (cacheConnectionPage.getDbClass().length()==0) return false;
//			}
//			
//			//Metarepository forms
//			if (embeddedOrExternalMetarepositoryDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
//				//embedded database:
//				if (embeddedMetarepositoryDBPage.getDbName().length()==0) return false;
//				if (embeddedMetarepositoryDBPage.getDbLogin().length()==0) return false;
//				if (embeddedMetarepositoryDBPage.getDbPassword().length()==0) return false;
//			} else {
//				//external database:
//				if (metarepositoryConnectionPage.getDbType().length()==0) return false;
//				if (metarepositoryConnectionPage.getDbURL().length()==0) return false;
//				if (metarepositoryConnectionPage.getDbClass().length()==0) return false;
//			}
			
//			if (! userAccountPage.canFlipToNextPage()) return false;
			
			//Server Info form
			if (serverInfoPage.getSiteName()==null || serverInfoPage.getSiteName().length()==0) return false;
			if (serverInfoPage.getOwnerEmail()==null) return false;
      
		} catch (Exception e) {
			log.error("Error validating setup wizard", e);
			return false;
		}
		return true;
	}
	
	
	
	@Override
	public boolean performFinish() {
		//focus away from current item on current page, ensuring that databinding happens
//		getContainer().getCurrentPage().getControl().forceFocus();
		
		Persister persister = Persister.getInstance();
		
		Site site = new Site();
		site.setRandomId();
		site.setOwnerEmail(serverInfoPage.getOwnerEmail());
		site.setName(serverInfoPage.getSiteName());
		NamespaceMapping uriPrefixMapping = new NamespaceMapping();
		uriPrefixMapping.setNamespaceUri(serverInfoPage.getUriPrefix());
		uriPrefixMapping.setNamespaceAbbrev(serverInfoPage.getUriPrefixAbbrev());
		site.setUriPrefix(uriPrefixMapping);
		appInfo.setSite(site);
		
		UserAccount adminAccount = new UserAccount();
		adminAccount.setUserName(userAccountPage.getUserName());
		adminAccount.setPassword(userAccountPage.getPassword());
		appInfo.addAdminAccount(adminAccount);
		
//		log.info("Persisting new AppInfo to " + Persister.getAppInfoFilePath() + "\n" + JenabeanWriter.toString(appInfo));
		try {
			Persister.persistToFile(appInfo, Persister.getAppInfoFilePath(), true);
//			Persister persister = Persister.getInstance();
//			persister.createNewDBConnection(metarepositoryConnection);
		} catch (Exception e) {
			log.error("Unable to save AppInfo to " + Persister.getAppInfoFilePath(), e);
		}
		
		//create necessary databases & datasets
		
//		SDBConnector connector = new SDBConnector(metarepositoryConnection);
//		int status = connector.createDatabase();
//		log.info("Tried to create new SDB store for Metarepository, with status=" + status);
		IDatabase systemDatabase = new LocalFolderDatabase();
		systemDatabase.setId(InqleInfo.SYSTEM_DATABASE_ROOT);
		SystemDatamodel metarepositoryDatamodel = new SystemDatamodel();
		metarepositoryDatamodel.setId(Persister.METAREPOSITORY_DATAMODEL);
		metarepositoryDatamodel.setDatabaseId(InqleInfo.SYSTEM_DATABASE_ROOT);
		
		try {
			IDBConnector connector = DBConnectorFactory.getDBConnector(systemDatabase);
			int status = connector.createDatabase();
			log.info("Created database: " + InqleInfo.SYSTEM_DATABASE_ROOT + ": Status=" + status);
			Model metarepositoryModel = connector.getModel(Persister.METAREPOSITORY_DATAMODEL);
			persister.persist(systemDatabase);
			persister.persist(metarepositoryDatamodel);
			log.info("CREATED user database and first user datamodel.");
//			persister.createDBModel(metarepositoryDataset);
		} catch (Exception e) {
			log.error("Error creating/storing database: " + InqleInfo.SYSTEM_DATABASE_ROOT + " and dataset: " + Persister.METAREPOSITORY_DATAMODEL, e);
			//TODO show error to user
			return false;
		}
		
		UserDatamodel firstDataDataset = new UserDatamodel();
		firstDataDataset.setId(FIRSTDATA_DATAMODEL_ID);
		IDatabase userDatabase = new LocalFolderDatabase();
		userDatabase.setId(InqleInfo.USER_DATABASE_ROOT);
		firstDataDataset.setDatabaseId(userDatabase.getId());
		firstDataDataset.addDatasetFunction(Persister.EXTENSION_DATASET_FUNCTION_DATA);
		try {
//			IDBConnector connector = DBConnectorFactory.getDBConnector(firstDataDatabase);
//			int status = connector.createDatabase();
//			log.info("Created data store for first dataset " + firstDataDataset + ": Status=" + status);
//			Persister persister = Persister.getInstance();
//			persister.persist(firstDataDatabase);
//			persister.persist(firstDataDataset);
			persister.createNewDatabase(userDatabase);
			persister.createDatabaseBackedModel(firstDataDataset);
			log.info("CREATED user database and first user datamodel.");
		} catch (Exception e) {
			log.error("Error creating/storing first dataset", e);
		}
		return true;
	}

//	@Override
//	public Object getBean() {
//		return appInfo;
//	}

//	public void setBean(AppInfo appInfo) {
//		this.appInfo = appInfo;
//		
//	}
}
