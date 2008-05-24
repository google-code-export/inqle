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
import org.inqle.data.rdf.jena.RDBModel;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.ModelWizard.RDBModelInfoPage;
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
	private RDBModel metarepositoryModel;
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
		
		metarepositoryModel = (RDBModel)appInfo.getRepositoryNamedModel();
		if (metarepositoryModel == null) {
			metarepositoryModel = new RDBModel();
			metarepositoryModel.setModelName("Metarepository");
		}
		metarepositoryConnection = metarepositoryModel.getConnection();
		if (metarepositoryConnection == null) {
			metarepositoryConnection = new Connection();
			metarepositoryModel.setConnection(metarepositoryConnection);
		}
//		Connection metarepositoryConnection = metarepositoryRdbModel.getConnection();
		ConnectionPage metarepositoryPage = new ConnectionPage(
				"Specify database connection info for your INQLE server", 
				metarepositoryConnection, 
				shell
		);
		addPage(metarepositoryPage);
		
		SingleTextPage metarepositoryModelInfoPage = new SingleTextPage(
				metarepositoryModel, 
				"modelName", 
				"Enter any name for your Metarepository Model", 
				null
		);
		addPage(metarepositoryModelInfoPage);
	}

	@Override
	public boolean performFinish() {
		//focus away from current item on current page, ensuring that databinding happens
		getContainer().getCurrentPage().getControl().forceFocus();
		
		metarepositoryModel.setConnection(metarepositoryConnection);
		appInfo.setRepositoryNamedModel(metarepositoryModel);
		log.info("Persisting new AppInfo to " + Persister.getAppInfoFilePath() + "\n" + JenabeanWriter.toString(appInfo));
		try {
			Persister.persistToFile(appInfo, Persister.getAppInfoFilePath(), true);
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
