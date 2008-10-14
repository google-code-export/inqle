/**
 * 
 */
package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.tree.parts.ModelPart;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class FileDataImporterAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private ModelPart modelPart = null;
	
	private static final Logger log = Logger.getLogger(FileDataImporterAction.class);
	
	public FileDataImporterAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
		this.menuText = menuText;
		this.modelPart = modelPart;
		this.window = window;
		//this.persister = persister;
	}
	
	public ModelPart getModelPart() {
		return modelPart;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		try {
			Model modelToImportInto = Persister.getInstance().getIndexableModel(modelPart.getDataset());
			FileDataImporterWizard wizard = new FileDataImporterWizard(modelToImportInto, window.getShell());
			wizard.setNamedModel(modelPart.getDataset());
			DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
			dialog.open();
		} catch (Exception e) {
			log.error("Error running FileDataImporterWizard", e);
		}
	}
}