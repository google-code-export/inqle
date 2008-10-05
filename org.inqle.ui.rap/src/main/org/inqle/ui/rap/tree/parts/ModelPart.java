package org.inqle.ui.rap.tree.parts;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.ui.rap.Part;
import org.inqle.ui.rap.actions.DeleteModelAction;
import org.inqle.ui.rap.actions.FileDataImporterAction;
import org.inqle.ui.rap.actions.LoadCsvFileAction;
import org.inqle.ui.rap.actions.LoadRdfFileAction;
import org.inqle.ui.rap.actions.DatasetWizardAction;

public class ModelPart extends Part {

	private static final String ICON_PATH_EXTERNAL_DATASET = "org/inqle/ui/rap/images/table.gif";
	private static final String ICON_PATH_ONTOLOGY_DATASET = "org/inqle/ui/rap/images/ontology.gif";
	private Dataset dataset;
	
	public ModelPart(Dataset dataset) {
		this.dataset = dataset;
		//this.persister = persister;
	}
	public String getModelName() {
		return dataset.getId();
	}
	
	@Override
	public String getName() {
		return getModelName();
	}
	
	@Override
	public String getIconPath() {
//		if (dataset instanceof ExternalDataset) {
			return ICON_PATH_EXTERNAL_DATASET;
//		} else if (dataset instanceof OntologyDataset) {
//			return ICON_PATH_ONTOLOGY_DATASET;
//		} else {
//			return null;
//		}
	}
	public Dataset getDataset() {
		return this.dataset;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		//"Edit this dataset" action
		DatasetWizardAction editModelWizardAction = new DatasetWizardAction(DatasetWizardAction.MODE_EDIT, "Edit this dataset...", (DatabasePart)this.getParent(), workbenchWindow);
		//editModelWizardAction.setModelPart(this);
		editModelWizardAction.setDataset(dataset);
		manager.add(editModelWizardAction);
		
		//"Load RDF File" action
		LoadRdfFileAction loadRdfFileAction = new LoadRdfFileAction("Load data from RDF File...", this, workbenchWindow);
		manager.add(loadRdfFileAction);
		
//		Legacy CSV loader
//		LoadCsvFileAction loadCsvFileAction = new LoadCsvFileAction("Load data from Delimited Text (CSV) File...", this, workbenchWindow);
//		manager.add(loadCsvFileAction);
		
		FileDataImporterAction fileDataImporterAction = new FileDataImporterAction("Load data from a file...", this, workbenchWindow);
		manager.add(fileDataImporterAction);
		
		//Delete action
		DeleteModelAction deleteDatabaseAction = new DeleteModelAction("Delete", this, workbenchWindow);
		manager.add(deleteDatabaseAction);
	}
	
	@Override
	public Object getObject() {
		return dataset;
	}
}
