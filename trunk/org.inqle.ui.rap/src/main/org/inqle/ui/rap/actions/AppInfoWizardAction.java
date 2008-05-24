package org.inqle.ui.rap.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class AppInfoWizardAction extends Action {
	
	private IWorkbenchWindow window;

	public AppInfoWizardAction(IWorkbenchWindow window) { 
		this.window = window;
	}
	
	@Override
	public void run() {
		AppInfoWizard wizard = new AppInfoWizard(window.getShell());
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
		
	}
}
