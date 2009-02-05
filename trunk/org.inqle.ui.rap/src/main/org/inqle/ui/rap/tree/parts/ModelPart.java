package org.inqle.ui.rap.tree.parts;

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
import org.inqle.ui.rap.ApplicationActionBarAdvisor;
import org.inqle.ui.rap.IDisposableViewer;
import org.inqle.ui.rap.Part;
import org.inqle.ui.rap.actions.AddReasonerStatementsAction;
import org.inqle.ui.rap.actions.DatamodelWizardAction;
import org.inqle.ui.rap.actions.DeleteModelAction;
import org.inqle.ui.rap.actions.EmptyModelAction;
import org.inqle.ui.rap.actions.FileDataImporterAction;
import org.inqle.ui.rap.actions.LoadRdfFileAction;
import org.inqle.ui.rap.actions.OpenNamedModelViewAction;
import org.inqle.ui.rap.views.DatamodelView;
import org.inqle.ui.rap.views.DatasetViewer;

public class ModelPart extends Part {

	private static final String ICON_PATH_EXTERNAL_DATASET = "org/inqle/ui/rap/images/table.gif";
//	private static final String EXTENSION_VIEW_BROWSE_DATA = "org.inqle.rap.ui.views.browseData";
//	private static final String ICON_PATH_ONTOLOGY_DATASET = "org/inqle/ui/rap/images/ontology.gif";
	private Datamodel datamodel;
	
	private static final Logger log = Logger.getLogger(ModelPart.class);
	
	public ModelPart(Datamodel datamodel) {
		this.datamodel = datamodel;
		//this.persister = persister;
	}
	public String getModelName() {
		return datamodel.getId();
	}
	
	@Override
	public String getName() {
		return getModelName();
	}
	
	@Override
	public String getIconPath() {
//		if (datamodel instanceof UserDatamodel) {
			return ICON_PATH_EXTERNAL_DATASET;
//		} else if (datamodel instanceof OntologyDataset) {
//			return ICON_PATH_ONTOLOGY_DATASET;
//		} else {
//			return null;
//		}
	}
	public Datamodel getDataset() {
		return this.datamodel;
	}
	
	@Override
//public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();		//"Edit this datamodel" action
		
		IExtensionSpec extensionSpec = ExtensionFactory.getExtensionSpec(ApplicationActionBarAdvisor.VIEWS, DatamodelView.ID);
		
//		log.info("Displaying action: " + extensionSpec);
		OpenNamedModelViewAction openNamedModelViewAction = new OpenNamedModelViewAction(
  			workbenchWindow, 
  			extensionSpec.getAttribute(ApplicationActionBarAdvisor.NAME), 
  			extensionSpec.getAttribute(ApplicationActionBarAdvisor.ID), 
  			extensionSpec.getPluginId(), 
  			extensionSpec.getAttribute(ApplicationActionBarAdvisor.ICON));
		openNamedModelViewAction.setDescription("Browse data in this datamodel.");
		openNamedModelViewAction.setDatamodel(datamodel);
		actions.add(openNamedModelViewAction);
  	
		if (! (datamodel instanceof SystemDatamodel)) {
			DatamodelWizardAction editModelWizardAction = new DatamodelWizardAction(DatamodelWizardAction.MODE_EDIT, "Edit this datamodel...", (DatabasePart)this.getParent(), workbenchWindow);
			//editModelWizardAction.setModelPart(this);
			editModelWizardAction.setDatamodel(datamodel);
			actions.add(editModelWizardAction);
			
			//"Load RDF File" action
			LoadRdfFileAction loadRdfFileAction = new LoadRdfFileAction("Load data from RDF file...", this, workbenchWindow);
			actions.add(loadRdfFileAction);
			
	//		Legacy CSV loader
	//		LoadCsvFileAction loadCsvFileAction = new LoadCsvFileAction("Load data from Delimited Text (CSV) File...", this, workbenchWindow);
	//		actions.add(loadCsvFileAction);
			
			FileDataImporterAction fileDataImporterAction = new FileDataImporterAction("Load data from delimited text (CSV) file...", this, workbenchWindow);
			actions.add(fileDataImporterAction);
			
			//"Add inferred statements" action
			AddReasonerStatementsAction addReasonerStatementsAction = new AddReasonerStatementsAction("Add inferred statements...", this, workbenchWindow);
			actions.add(addReasonerStatementsAction);
			
			//Clear datamodel action
			EmptyModelAction emptyDatasetAction = new EmptyModelAction("Empty data", this, workbenchWindow);
			actions.add(emptyDatasetAction);
			
			//Delete action
			//TODO add this functionality back some day (removed due to difficulty doing this in TDB)
//			DeleteModelAction deleteDatasetAction = new DeleteModelAction("Delete this datamodel", this, workbenchWindow);
//			actions.add(deleteDatasetAction);
		}
		return actions;
	}
	
	@Override
	public Object getObject() {
		return datamodel;
	}
	
	public IDisposableViewer getViewer(Composite composite) {
		return new DatasetViewer(composite, getObject());
	}
}
