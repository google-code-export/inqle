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
	private Dataset metarepositoryDataset;
	private Connection metarepositoryConnection;

	private RadiosPage embeddedOrExternalDBPage;

	private ConnectionPage metarepositoryPage;

	private EmbeddedDBPage embeddedDBPage;

	private SingleTextPage metarepositoryModelInfoPage;
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
		
		
		embeddedOrExternalDBPage = new RadiosPage("Select type of database to use");
		embeddedOrExternalDBPage.setRadioOptionTexts(Arrays.asList(OPTIONS_EMBEDDED_OR_NOT));
		addPage(embeddedOrExternalDBPage);
		log.info("added embeddedOrExternalDBPage");
		
		embeddedDBPage = new EmbeddedDBPage("Specify connection info for your embedded H2 database.");
		addPage(embeddedDBPage);
		log.info("added embeddedDBPage");
		
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
		metarepositoryPage = new ConnectionPage(
				"Specify database connection info for your INQLE server", 
				metarepositoryConnection, 
				shell
		);
		addPage(metarepositoryPage);
		
		metarepositoryModelInfoPage = new SingleTextPage(
				metarepositoryDataset, 
				"id", 
				"Enter a unique name for your Metarepository Model, e.g. com.my.domain.metarepositoryModel", 
				null
		);
		addPage(metarepositoryModelInfoPage);
		log.info("added metarepositoryModelInfoPage");
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		log.info("getNextPage(" + page + ")");
		if (page == embeddedOrExternalDBPage) {
			if (embeddedOrExternalDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
				return embeddedDBPage;
			} else {
				return metarepositoryPage;
			}
		}
		
		if (page == embeddedDBPage || page == metarepositoryPage) {
			return metarepositoryModelInfoPage;
		}
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish() {
		//focus away from current item on current page, ensuring that databinding happens
		getContainer().getCurrentPage().getControl().forceFocus();
		
		if (embeddedOrExternalDBPage.getSelectedIndex() == EMBEDDED_H2_DATABASE) {
			metarepositoryConnection.setDbURL(H2_DB_URL_BASE + embeddedDBPage.getDbName());
			metarepositoryConnection.setDbClass(H2_DB_CLASS);
			metarepositoryConnection.setDbType(H2_DB_TYPE);
			metarepositoryConnection.setDbUser(embeddedDBPage.getDbLogin());
			metarepositoryConnection.setDbPassword(embeddedDBPage.getDbPassword());
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
