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
public class LoadRdfFileAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private ModelPart modelPart = null;
	private Connection connection = null;
	
	private static final Logger log = Logger.getLogger(LoadRdfFileAction.class);
	
	public LoadRdfFileAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
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
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		//LoadRdfFileWizard wizard = new LoadRdfFileWizard(modelPart, connection);
		try {
			Model modelToImportInto = Persister.getInstance().getIndexableModel(modelPart.getDataset());
			LoadRdfFileWizard wizard = new LoadRdfFileWizard(modelToImportInto, window.getShell());
			//DummyWizard wizard = new DummyWizard();
			
			DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
			//LoadDataWizardDialog dialog = new LoadDataWizardDialog(window.getShell(), wizard);
			//UploadDialog dialog = new UploadDialog(window.getShell(), "Upload File", true);

			dialog.open();
		} catch (Exception e) {
			log.error("Error running LoadRdfFileAction", e);
		}
	}
	
	
	
	
	
	/**
	 * Create our own WizardDialog subclass so we can intercept any closing
	 * events, and dispose of the uploader widget.  If we fail to do this,
	 * repeated invocations of the uploader will cause a Javascript error
	 * @author David Donohue
	 * Feb 21, 2008
	 */
//	class LoadDataWizardDialog extends WizardDialog {
//		public LoadDataWizardDialog(Shell parentShell, LoadRdfFileWizard newWizard) {
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
//			LoadRdfFileWizard wizard = (LoadRdfFileWizard)getWizard();
//		  wizard.closeUploader();
//		  close();
//		}
//	}
}
