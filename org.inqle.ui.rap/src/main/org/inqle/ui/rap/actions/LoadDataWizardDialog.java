package org.inqle.ui.rap.actions;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.widgets.Shell;

/**
	 * Create our own WizardDialog subclass so we can intercept any closing
	 * events, and dispose of the uploader widget.  If we fail to do this,
	 * repeated invocations of the uploader will cause a Javascript error
	 * @author David Donohue
	 * Feb 21, 2008
	 */
	public class LoadDataWizardDialog extends DynaWizardDialog {
		public LoadDataWizardDialog(Shell parentShell, IWizard newWizard) {
			super(parentShell, newWizard);
			//this.getButton(WizardDialog.CANCEL).setEnabled(false);
		}
		
		
		
//		protected void closeUploader() {
//			LoadRdfFileWizard wizard = (LoadRdfFileWizard)getWizard();
//		  wizard.closeUploader();
//		  close();
//		}
	}