package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class AppInfoWizardAction extends Action {
	
	private IWorkbenchWindow window;

	private static Logger log = Logger.getLogger(AppInfoWizardAction.class);
	public AppInfoWizardAction(IWorkbenchWindow window) { 
		this.window = window;
	}
	
	@Override
	public void run() {
		try {
			AppInfoWizard wizard = new AppInfoWizard(window.getShell());
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.open();
		} catch (Exception e) {
			log.error("Error running the AppInfoWizard", e);
		}
		
	}
}
