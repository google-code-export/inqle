/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.LocalFolderDatabase;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jena.PurposefulDatamodel;
import org.inqle.data.rdf.jena.uri.NamespaceMapping;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.Site;
import org.inqle.data.rdf.jenabean.UserAccount;
import org.inqle.ui.rap.pages.InfoPage;
import org.inqle.ui.rap.pages.ServerInfoPage;

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
	
	private static final String FIRSTDATA_DATAMODEL_NAME = "data1";

	private static final String FIRSTDATA_DATABASE_ID = "db1";
	
	private AppInfo appInfo = new AppInfo();
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
//		this.shell = parentShell;
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
	}

	@Override
	public boolean canFinish() {
		//getContainer().getCurrentPage().getControl().forceFocus();
		//TODO test that prefix & subjectclass are URIs
		try {			
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
		
		//create necessary databases & datamodels
		
//		SDBConnector connector = new SDBConnector(metarepositoryConnection);
//		int status = connector.createDatabase();
//		log.info("Tried to create new SDB store for Metarepository, with status=" + status);
		LocalFolderDatabase coreDatabase = new LocalFolderDatabase();
		coreDatabase.setId(Persister.CORE_DATABASE_ID);
//		SystemDatamodel metarepositoryDatamodel = new SystemDatamodel();
//		metarepositoryDatamodel.setId(Persister.METAREPOSITORY_DATAMODEL);
//		metarepositoryDatamodel.setDatabaseId(Persister.CORE_DATABASE_ID);
		
		//create the system database and the metarepository model (which contains data about datamodels)
		try {
			IDBConnector connector = DBConnectorFactory.getDBConnector(coreDatabase.getId());
			boolean success = connector.createDatabase();
			log.info("Created database: " + Persister.CORE_DATABASE_ID + ": Success?" + success);
//			Model metarepositoryModel = persister.getMetarepositoryModel(Persister.CORE_DATABASE_ID);
//			persister.persist(systemDatabase, metarepositoryModel);
//			persister.persist(coreDatabase, Persister.getTargetDatamodelId(LocalFolderDatabase.class, coreDatabase.getId()));
//			persister.persist(metarepositoryDatamodel, Persister.getTargetDatamodelId(SystemDatamodel.class, coreDatabase.getId()));
//			log.info("CREATED core database.");
		} catch (Exception e) {
			log.error("Error creating/storing database: " + Persister.CORE_DATABASE_ID + " and dataset: " + Persister.METAREPOSITORY_DATAMODEL, e);
			//TODO show error to user
			return false;
		}
		
		//create the user database and first user datamodel
		PurposefulDatamodel firstDataDatamodel = new PurposefulDatamodel();
		firstDataDatamodel.setName(FIRSTDATA_DATAMODEL_NAME);
		IDatabase userDatabase = new LocalFolderDatabase();
		userDatabase.setId(FIRSTDATA_DATABASE_ID);
		firstDataDatamodel.setDatabaseId(userDatabase.getId());
		firstDataDatamodel.addDatamodelPurpose(Persister.EXTENSION_DATAMODEL_PURPOSES_MINABLE_DATA);
		try {
			persister.createNewDatabase(userDatabase);
			persister.createDatabaseBackedModel(firstDataDatamodel);
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
