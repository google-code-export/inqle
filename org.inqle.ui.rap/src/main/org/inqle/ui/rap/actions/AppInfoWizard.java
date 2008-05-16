/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.RDBModel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.pages.ConnectionPage;
import org.inqle.ui.rap.pages.SingleTextPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 20, 2008
 */
public class AppInfoWizard extends DynaWizard {

	private AppInfo appInfo;

	public AppInfoWizard(Model saveToModel, Persister persister, Shell shell) {
		super(saveToModel, persister, shell);
	}

	@Override
	public void addPages() {
		SingleTextPage siteUrlPage = new SingleTextPage(appInfo, "serverBaseUrl", "Server URL", null);
		siteUrlPage.setLabelText("Enter base URL of this INQLE server");
		addPage(siteUrlPage);
		
		RDBModel metarepositoryRdbModel = (RDBModel)appInfo.getRepositoryNamedModel();
		Connection metarepositoryConnection = metarepositoryRdbModel.getConnection();
		ConnectionPage metarepositoryPage = new ConnectionPage("Metarepository Database Connection Info", metarepositoryConnection, shell);
		addPage(metarepositoryPage);
	}

//	@Override
//	public Object getBean() {
//		return appInfo;
//	}

	public void setBean(AppInfo appInfo) {
		this.appInfo = appInfo;
		
	}
}
