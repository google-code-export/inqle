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
public class AddReasonerStatementsAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private ModelPart modelPart = null;
	
	private static final Logger log = Logger.getLogger(AddReasonerStatementsAction.class);
	
	public AddReasonerStatementsAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
		this.menuText = menuText;
		this.modelPart = modelPart;
		this.window = window;
		//this.persister = persister;
//		this.connection  = ((DatabasePart)modelPart.getParent()).getConnection();
	}
	
	public ModelPart getModelPart() {
		return modelPart;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		//LoadRdfFileWizard wizard = new LoadRdfFileWizard(modelPart, connection);
		try {
			Persister persister = Persister.getInstance();
			Model modelToImportInto = persister.getIndexableModel(modelPart.getDataset());
			AddReasonerStatementsWizard wizard = new AddReasonerStatementsWizard(modelToImportInto, window.getShell());
			wizard.setDataset(modelPart.getDataset());
			DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
			//LoadDataWizardDialog dialog = new LoadDataWizardDialog(window.getShell(), wizard);
			//UploadDialog dialog = new UploadDialog(window.getShell(), "Upload File", true);

			dialog.open();
		} catch (Exception e) {
			log.error("Error running AddReasonerStatementsAction", e);
		}
	}
}
