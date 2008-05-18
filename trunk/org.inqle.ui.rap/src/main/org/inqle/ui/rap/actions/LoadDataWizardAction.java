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
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.tree.parts.ModelPart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class LoadDataWizardAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister = null;
	private ModelPart modelPart = null;
	private Connection connection = null;
	
	private static final Logger log = Logger.getLogger(LoadDataWizardAction.class);
	
	public LoadDataWizardAction(String menuText, ModelPart modelPart, IWorkbenchWindow window) {
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
		LoadDataWizard wizard = new LoadDataWizard(modelPart, connection);
		//DummyWizard wizard = new DummyWizard();
		
		LoadDataWizardDialog dialog = new LoadDataWizardDialog(window.getShell(), wizard);
		//UploadDialog dialog = new UploadDialog(window.getShell(), "Upload File", true);

		dialog.open();
	}
	
	
	
	
	
	/**
	 * Create our own WizardDialog subclass so we can intercept any closing
	 * events, and dispose of the uploader widget.  If we fail to do this,
	 * repeated invocations of the uploader will cause a Javascript error
	 * @author David Donohue
	 * Feb 21, 2008
	 */
	class LoadDataWizardDialog extends WizardDialog {
		public LoadDataWizardDialog(Shell parentShell, LoadDataWizard newWizard) {
			super(parentShell, newWizard);
			//this.getButton(WizardDialog.CANCEL).setEnabled(false);
		}
		
		/**
		 * this disposes of uploader on clicking the wizard's cancel button
		 */
		@Override
		protected void cancelPressed() {
			closeUploader();
		}
		/**
		 * this disposes of uploader on clicking the wizard's "X" in upper right
		 */
		@Override
		protected boolean canHandleShellCloseEvent() {
			closeUploader();
		  return true;
		}
		
		private void closeUploader() {
			LoadDataWizard wizard = (LoadDataWizard)getWizard();
		  wizard.closeUploader();
		  close();
		}
	}
}
