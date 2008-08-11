/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.ui.rap.tree.parts.DatabasePart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DatasetWizardAction extends Action {
	public static final int MODE_NEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_CLONE = 2;
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private int mode = MODE_NEW;
	//private ModelPart modelPart = null;
	private DatabasePart databasePart;
	private ExternalDataset dataset;
	
	private static final Logger log = Logger.getLogger(DatasetWizardAction.class);
	
	public DatasetWizardAction(int mode, String menuText, DatabasePart databasePart, IWorkbenchWindow window) {
		this.mode = mode;
		this.menuText = menuText;
		this.databasePart = databasePart;
		this.window = window;
		log.trace("Created DatasetWizardAction");
		//this.persister = persister;
	}
	
//	public ModelPart getModelPart() {
//		return modelPart;
//	}

//	public void setModelPart(ModelPart modelPart) {
//		this.modelPart = modelPart;
//		this.rdbModel = modelPart.getRdbModel();
//	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		try {
			DatasetWizard wizard = new DatasetWizard(mode, dataset, databasePart);
			log.trace("Created DatasetWizard");
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.open();
			log.trace("Opened WizardDialog");
		} catch (Exception e) {
			log.error("Error running DatasetWizard", e);
		}
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(ExternalDataset dataset) {
		this.dataset = dataset;
	}
}
