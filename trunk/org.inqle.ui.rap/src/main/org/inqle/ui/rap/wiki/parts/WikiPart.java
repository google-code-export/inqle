package org.inqle.ui.rap.wiki.parts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jena.UserDatamodel;
import org.inqle.http.lookup.PublishDatamodelServlet;
import org.inqle.ui.rap.ApplicationActionBarAdvisor;
import org.inqle.ui.rap.IDisposableViewer;
import org.inqle.ui.rap.Part;
import org.inqle.ui.rap.actions.AddReasonerStatementsAction;
import org.inqle.ui.rap.actions.DatamodelWizardAction;
import org.inqle.ui.rap.actions.DeleteModelAction;
import org.inqle.ui.rap.actions.EmptyModelAction;
import org.inqle.ui.rap.actions.FileDataImporterAction;
import org.inqle.ui.rap.actions.LoadRdfFileAction;
import org.inqle.ui.rap.actions.NewBrowserAction;
import org.inqle.ui.rap.actions.OpenNamedModelViewAction;
import org.inqle.ui.rap.views.DatamodelView;
import org.inqle.ui.rap.views.DatasetViewer;
import org.inqle.ui.rap.views.ObjectViewer;

public class WikiPart extends Part {

	private static final String ICON_PATH_EXTERNAL_DATASET = "org/inqle/ui/rap/images/wiki.gif";
	private Datamodel datamodel;
	
	private static final Logger log = Logger.getLogger(WikiPart.class);
	
	public WikiPart(Datamodel datamodel) {
		this.datamodel = datamodel;
	}
	public String getModelName() {
		return datamodel.getName();
	}
	
	@Override
	public String getName() {
		return getModelName();
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH_EXTERNAL_DATASET;
	}
	public Datamodel getDataset() {
		return this.datamodel;
	}
	
	@Override
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();		//"Edit this datamodel" action
		
//		IExtensionSpec extensionSpec = ExtensionFactory.getExtensionSpec(ApplicationActionBarAdvisor.VIEWS, DatamodelView.ID);
//		
//		OpenNamedModelViewAction openNamedModelViewAction = new OpenNamedModelViewAction(
//			workbenchWindow, 
//			extensionSpec.getAttribute(ApplicationActionBarAdvisor.NAME), 
//			extensionSpec.getAttribute(ApplicationActionBarAdvisor.ID), 
//			extensionSpec.getPluginId(), 
//			extensionSpec.getAttribute(ApplicationActionBarAdvisor.ICON));
//		openNamedModelViewAction.setDescription("Browse data in this datamodel.");
//		openNamedModelViewAction.setDatamodel(datamodel);
//		actions.add(openNamedModelViewAction);
//  	
//		
//		//browse this model action
//		String url = PublishDatamodelServlet.PATH + "?" + PublishDatamodelServlet.PARAM_EXPORT_FROM_DATAMODEL + "=" + datamodel.getId();
//		NewBrowserAction newBrowserAction = new NewBrowserAction(
//			url,
//			"View RDF Page",
//			ApplicationActionBarAdvisor.PLUGIN_ID,
//			null,
//			"_blank"
//		);
//		newBrowserAction.setDescription("View this model as an RDF file, in a separate browser.  Only works for smaller models.");
//		actions.add(newBrowserAction);
		
		return actions;
	}
	
	@Override
	public Object getObject() {
		return datamodel;
	}
	
	@Override
	public IDisposableViewer getViewer(Composite composite) {
//		return new WikiPageViewer(composite, datamodel, new DatamodelPage(datamodel));
		log.info("Creating WikiPageViewer w/ datamodel: " + datamodel);
		return new WikiPageViewer(composite, datamodel, null);
	}
}
