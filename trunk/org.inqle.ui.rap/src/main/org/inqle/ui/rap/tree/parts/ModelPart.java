package org.inqle.ui.rap.tree.parts;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.ui.rap.DatasetViewer;
import org.inqle.ui.rap.IDisposableViewer;
import org.inqle.ui.rap.Part;
import org.inqle.ui.rap.actions.AddReasonerStatementsAction;
import org.inqle.ui.rap.actions.DatasetWizardAction;
import org.inqle.ui.rap.actions.DeleteModelAction;
import org.inqle.ui.rap.actions.EmptyModelAction;
import org.inqle.ui.rap.actions.FileDataImporterAction;
import org.inqle.ui.rap.actions.LoadRdfFileAction;

public class ModelPart extends Part {

	private static final String ICON_PATH_EXTERNAL_DATASET = "org/inqle/ui/rap/images/table.gif";
//	private static final String ICON_PATH_ONTOLOGY_DATASET = "org/inqle/ui/rap/images/ontology.gif";
	private ExternalDataset dataset;
	
	public ModelPart(ExternalDataset dataset) {
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
	public ExternalDataset getDataset() {
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
		
		FileDataImporterAction fileDataImporterAction = new FileDataImporterAction("Load data from delimited text (CSV) file...", this, workbenchWindow);
		manager.add(fileDataImporterAction);
		
		//"Add inferred statements" action
		AddReasonerStatementsAction addReasonerStatementsAction = new AddReasonerStatementsAction("Add inferred statements...", this, workbenchWindow);
		manager.add(addReasonerStatementsAction);
		
		//Clear dataset action
		EmptyModelAction emptyDatasetAction = new EmptyModelAction("Empty data", this, workbenchWindow);
		manager.add(emptyDatasetAction);
		
		//Delete action
		DeleteModelAction deleteDatasetAction = new DeleteModelAction("Delete this dataset", this, workbenchWindow);
		manager.add(deleteDatasetAction);
	}
	
	@Override
	public Object getObject() {
		return dataset;
	}
	
	public IDisposableViewer getViewer(Composite composite) {
		return new DatasetViewer(composite, getObject());
	}
}
