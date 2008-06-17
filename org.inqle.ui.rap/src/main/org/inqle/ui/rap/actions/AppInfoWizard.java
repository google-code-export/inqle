/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.DatasetWizard.DatasetInfoPage;
import org.inqle.ui.rap.pages.ConnectionPage;
import org.inqle.ui.rap.pages.SingleTextPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 20, 2008
 */
public class AppInfoWizard extends Wizard {

	private AppInfo appInfo = new AppInfo();
	private Shell shell;
	private Dataset metarepositoryDataset;
	private Connection metarepositoryConnection;
	public AppInfoWizard(Shell parentShell) {
		this.shell = parentShell;
	}
	
	private static Logger log = Logger.getLogger(AppInfoWizard.class);
	@Override
	public void addPages() {
//		SingleTextPage siteUrlPage = new SingleTextPage(appInfo, "serverBaseUrl", "Server URL", null);
//		siteUrlPage.setLabelText("Enter base URL of this INQLE server");
//		addPage(siteUrlPage);
		Persister persister = Persister.getInstance();
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
		ConnectionPage metarepositoryPage = new ConnectionPage(
				"Specify database connection info for your INQLE server", 
				metarepositoryConnection, 
				shell
		);
		addPage(metarepositoryPage);
		
		SingleTextPage metarepositoryModelInfoPage = new SingleTextPage(
				metarepositoryDataset, 
				"id", 
				"Enter a unique name for your Metarepository Model, e.g. com.my.domain.metarepositoryModel", 
				null
		);
		addPage(metarepositoryModelInfoPage);
	}

	@Override
	public boolean performFinish() {
		//focus away from current item on current page, ensuring that databinding happens
		getContainer().getCurrentPage().getControl().forceFocus();
		
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
