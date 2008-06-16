package org.inqle.ui.rap.tree.parts;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.RDBModel;
import org.inqle.ui.rap.Part;
import org.inqle.ui.rap.actions.DeleteModelAction;
import org.inqle.ui.rap.actions.LoadCsvFileAction;
import org.inqle.ui.rap.actions.LoadRdfFileAction;
import org.inqle.ui.rap.actions.ModelWizardAction;

public class ModelPart extends Part {

	private static final String ICON_PATH = "org/inqle/ui/rap/images/table.gif";
	private RDBModel rdbModel;
	
	public ModelPart(RDBModel rdbModel) {
		this.rdbModel = rdbModel;
		//this.persister = persister;
	}
	public String getModelName() {
		return rdbModel.getId();
	}
	
	@Override
	public String getName() {
		return getModelName();
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}
	public RDBModel getRdbModel() {
		return this.rdbModel;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		//"Edit this dataset" action
		ModelWizardAction editModelWizardAction = new ModelWizardAction(ModelWizardAction.MODE_EDIT, "Edit this dataset...", (DatabasePart)this.getParent(), workbenchWindow);
		//editModelWizardAction.setModelPart(this);
		editModelWizardAction.setRdbModel(rdbModel);
		manager.add(editModelWizardAction);
		
		//"Load RDF File" action
		LoadRdfFileAction loadRdfFileAction = new LoadRdfFileAction("Load data from RDF File...", this, workbenchWindow);
		manager.add(loadRdfFileAction);
		
		LoadCsvFileAction loadCsvFileAction = new LoadCsvFileAction("Load data from Delimited Text (CSV) File...", this, workbenchWindow);
		manager.add(loadCsvFileAction);
		
		//Delete action
		DeleteModelAction deleteDatabaseAction = new DeleteModelAction("Delete", this, workbenchWindow);
		manager.add(deleteDatabaseAction);
	}
	
	@Override
	public Object getObject() {
		return rdbModel;
	}
}
