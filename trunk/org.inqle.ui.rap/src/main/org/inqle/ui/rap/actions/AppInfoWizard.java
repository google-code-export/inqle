/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.pages.ConnectionPage;
import org.inqle.ui.rap.pages.EmbeddedDBPage;
import org.inqle.ui.rap.pages.RadiosPage;
import org.inqle.ui.rap.pages.SingleTextPage;

/**
 * @author David Donohue
 * Feb 20, 2008
 */
public class AppInfoWizard extends Wizard {

	private static final String[] OPTIONS_EMBEDDED_OR_NOT = {
		"Embedded H2 Database (recommended)",
		"External Database"
	};

	private static final int EMBEDDED_H2_DATABASE = 0;

	private static final String H2_DB_URL_BASE = "jdbc:h2:";

	private static final String H2_DB_CLASS = "org.h2.Driver";

	private static final String H2_DB_TYPE = "H2";
	
	private AppInfo appInfo = new AppInfo();
	private Shell shell;
	private Connection metarepositoryConnection;
	private Dataset metarepositoryDataset;
	
	private RadiosPage embeddedOrExternalMetarepositoryDBPage;
	private EmbeddedDBPage embeddedMetarepositoryDBPage;
	private ConnectionPage metarepositoryConnectionPage;
	private SingleTextPage metarepositoryDatasetPage;
	
	private Connection firstDataConnection;
	private Dataset firstDataDataset;
	
	private RadiosPage embeddedOrExternalFirstDataDBPage;
	private EmbeddedDBPage embeddedFirstDataDBPage;
	private ConnectionPage firstDataConnectionPage;
	private SingleTextPage firstDataDatasetPage;
	
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
		
		embeddedOrExternalMetarepositoryDBPage = new RadiosPage("We will create the internal database used by your INQLE server.", "Select whether to use an embedded database to use for the internal database.");
		embeddedOrExternalMetarepositoryDBPage.setRadioOptionTexts(Arrays.asList(OPTIONS_EMBEDDED_OR_NOT));
		addPage(embeddedOrExternalMetarepositoryDBPage);
		log.info("added embeddedOrExternalMetarepositoryDBPage");
		
		embeddedMetarepositoryDBPage = new EmbeddedDBPage("Internal INQLE Database", "Specify connection info for the embedded H2 database, which will contain internal INQLE information.");
		addPage(embeddedMetarepositoryDBPage);
		log.info("added embeddedMetarepositoryDBPage");
		
		metarepositoryDataset = (Dataset)appInfo.getMetarepositoryDataset();
		if (metarepositoryDataset == null) {
			metarepositoryDataset = new Dataset();
			//metarepositoryDataset.setModelName("Metarepository");
			metarepositoryDataset.setId("Metarepository");
		}
		metarepositoryConnection = appInfo.getMetarepositoryConnection();
		if (metarepositoryConnection == null) {
			metarepositoryConnection = new Connection();
			metarepositoryDataset.setConnectionId(metarepositoryConnection.getId());
		}
//		Connection metarepositoryConnection = metarepositoryRdbModel.getConnection();
		metarepositoryConnectionPage = new ConnectionPage(
				"Specify database connection info for your INQLE server", 
				metarepositoryConnection, 
				shell
		);
		addPage(metarepositoryConnectionPage);
		
		metarepositoryDatasetPage = new SingleTextPage(
				metarepositoryDataset, 
				"id", 
				"Enter a name for your Metarepository Model.", 
				null
		);
		addPage(metarepositoryDatasetPage);
		log.info("added metarepositoryDatasetPage");
		
		//add form elements for the first dataset
		embeddedOrExternalFirstDataDBPage = new RadiosPage("Next, we will create a database for storing your data.", "Select whether to use an embedded database, in which to store your data.");
		embeddedOrExternalFirstDataDBPage.setRadioOptionTexts(Arrays.asList(OPTIONS_EMBEDDED_OR_NOT));
		addPage(embeddedOrExternalFirstDataDBPage);
		log.info("added embeddedOrExternalFirstDataDBPage");
		
		embeddedFirstDataDBPage = new EmbeddedDBPage("Database for storing your data", "Specify connection info for the embedded database, in which to store your data.");
		addPage(embeddedFirstDataDBPage);
		log.info("added embeddedFirstDataDBPage");
		
		firstDataDataset = new Dataset();
		firstDataDataset.setId("FirstDataset");
		
		firstDataConnection = new Connection();
		firstDataDataset.setConnectionId(firstDataConnection.getId());
		
//		Connection firstDataConnection = firstDataRdbModel.getConnection();
		firstDataConnectionPage = new ConnectionPage(
				"Specify database connection info for your for first dataset", 
				firstDataConnection, 
				shell
		);
		addPage(firstDataConnectionPage);
		
		firstDataDatasetPage = new SingleTextPage(
				firstDataDataset, 
				"id", 
				"Enter a unique name for your first dataset, e.g. dataset1", 
				null
		);
		addPage(firstDataDatasetPage);
		log.info("added firstDataDatasetPage");
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		log.info("getNextPage(" + page + ")");
		if (page == embeddedOrExternalMetarepositoryDBPage) {
			if (embeddedOrExternalMetarepositoryDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
				return embeddedMetarepositoryDBPage;
			} else {
				return metarepositoryConnectionPage;
			}
		}
		
		if (page == embeddedMetarepositoryDBPage || page == metarepositoryConnectionPage) {
			return metarepositoryDatasetPage;
		}

		if (page == embeddedOrExternalFirstDataDBPage) {
			if (embeddedOrExternalFirstDataDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
				return embeddedFirstDataDBPage;
			} else {
				return firstDataConnectionPage;
			}
		}
		
		if (page == embeddedFirstDataDBPage || page == firstDataConnectionPage) {
			return firstDataDatasetPage;
		}
		return super.getNextPage(page);
	}

	@Override
	public boolean canFinish() {
		//getContainer().getCurrentPage().getControl().forceFocus();
		//TODO test that prefix & subjectclass are URIs
		try {
			//First data dataset forms
			if (embeddedOrExternalFirstDataDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
				//embedded database:
				if (embeddedFirstDataDBPage.getDbName().length()==0) return false;
				if (embeddedFirstDataDBPage.getDbLogin().length()==0) return false;
				if (embeddedFirstDataDBPage.getDbPassword().length()==0) return false;
			} else {
				//external database:
				if (firstDataConnectionPage.getDbType().length()==0) return false;
				if (firstDataConnectionPage.getDbURL().length()==0) return false;
				if (firstDataConnectionPage.getDbClass().length()==0) return false;
			}
			
			//Metarepository forms
			if (embeddedOrExternalMetarepositoryDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
				//embedded database:
				if (embeddedMetarepositoryDBPage.getDbName().length()==0) return false;
				if (embeddedMetarepositoryDBPage.getDbLogin().length()==0) return false;
				if (embeddedMetarepositoryDBPage.getDbPassword().length()==0) return false;
			} else {
				//external database:
				if (metarepositoryConnectionPage.getDbType().length()==0) return false;
				if (metarepositoryConnectionPage.getDbURL().length()==0) return false;
				if (metarepositoryConnectionPage.getDbClass().length()==0) return false;
			}
			
			
		} catch (Exception e) {
			log.error("Error validating setup wizard", e);
			return false;
		}
		return true;
	}
	
	
	
	@Override
	public boolean performFinish() {
		//focus away from current item on current page, ensuring that databinding happens
		getContainer().getCurrentPage().getControl().forceFocus();
		
		if (embeddedOrExternalMetarepositoryDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
			metarepositoryConnection.setDbURL(H2_DB_URL_BASE + embeddedMetarepositoryDBPage.getDbName());
			metarepositoryConnection.setDbClass(H2_DB_CLASS);
			metarepositoryConnection.setDbType(H2_DB_TYPE);
			metarepositoryConnection.setDbUser(embeddedMetarepositoryDBPage.getDbLogin());
			metarepositoryConnection.setDbPassword(embeddedMetarepositoryDBPage.getDbPassword());
		}
		metarepositoryDataset.setConnectionId(metarepositoryConnection.getId());
		appInfo.setMetarepositoryDataset(metarepositoryDataset);
		appInfo.setMetarepositoryConnection(metarepositoryConnection);
		log.info("Persisting new AppInfo to " + Persister.getAppInfoFilePath() + "\n" + JenabeanWriter.toString(appInfo));
		try {
			Persister.persistToFile(appInfo, Persister.getAppInfoFilePath(), true);
//			Persister persister = Persister.getInstance();
//			persister.createNewDBConnection(metarepositoryConnection);
			DBConnector connector = new DBConnector(metarepositoryConnection);
			int status = connector.tryToCreateSDBStore();
			log.info("Tried to create new SDB store for Metarepository, with status=" + status);
		} catch (Exception e) {
			log.error("Unable to save AppInfo to " + Persister.getAppInfoFilePath(), e);
		}
		
		//next create the first data dataset
		if (embeddedOrExternalFirstDataDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
			firstDataConnection.setDbURL(H2_DB_URL_BASE + embeddedFirstDataDBPage.getDbName());
			firstDataConnection.setDbClass(H2_DB_CLASS);
			firstDataConnection.setDbType(H2_DB_TYPE);
			firstDataConnection.setDbUser(embeddedFirstDataDBPage.getDbLogin());
			firstDataConnection.setDbPassword(embeddedFirstDataDBPage.getDbPassword());
		}
		firstDataDataset.setConnectionId(firstDataConnection.getId());
		
		try {
			DBConnector connector = new DBConnector(firstDataConnection);
			int status = connector.tryToCreateSDBStore();
			log.info("Created data store for first dataset " + firstDataDataset + ": Status=" + status);
			Persister persister = Persister.getInstance();
			persister.persist(firstDataConnection, persister.getMetarepositoryModel());
			persister.persist(firstDataDataset, persister.getMetarepositoryModel());
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
