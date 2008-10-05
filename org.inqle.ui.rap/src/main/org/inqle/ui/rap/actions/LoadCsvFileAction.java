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
@Deprecated
public class LoadCsvFileAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private ModelPart modelPart = null;
	private Connection connection = null;
	
	private static final Logger log = Logger.getLogger(LoadCsvFileAction.class);
	
	public LoadCsvFileAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
		this.menuText = menuText;
		this.modelPart = modelPart;
		this.window = window;
		//this.persister = persister;
		this.connection  = ((DatabasePart)modelPart.getParent()).getConnection();
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
			Model modelToImportInto = Persister.getInstance().getModel(modelPart.getDataset());
			LoadCsvFileWizard wizard = new LoadCsvFileWizard(modelToImportInto, window.getShell());
			DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
			dialog.open();
		} catch (Exception e) {
			log.error("Error running LoadCsvFileWizard", e);
		}
	}
	
	
	
	
	
	/**
	 * Create our own WizardDialog subclass so we can intercept any closing
	 * events, and dispose of the uploader widget.  If we fail to do this,
	 * repeated invocations of the uploader will cause a Javascript error
	 * @author David Donohue
	 * Feb 21, 2008
	 */
//	class LoadCsvWizardDialog extends WizardDialog {
//		public LoadCsvWizardDialog(Shell parentShell, LoadCsvFileWizard newWizard) {
//			super(parentShell, newWizard);
//			//this.getButton(WizardDialog.CANCEL).setEnabled(false);
//		}
//		
//		/**
//		 * this disposes of uploader on clicking the wizard's cancel button
//		 */
//		@Override
//		protected void cancelPressed() {
//			closeUploader();
//		}
//		/**
//		 * this disposes of uploader on clicking the wizard's "X" in upper right
//		 */
//		@Override
//		protected boolean canHandleShellCloseEvent() {
//			closeUploader();
//		  return true;
//		}
//		
//		private void closeUploader() {
//			LoadCsvFileWizard wizard = (LoadCsvFileWizard)getWizard();
//		  wizard.closeUploader();
//		  close();
//		}
//	}
}
