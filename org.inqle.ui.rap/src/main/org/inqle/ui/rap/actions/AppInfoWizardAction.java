package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class AppInfoWizardAction extends Action {
	
	private IWorkbenchWindow window;

	private Shell shell;

	private static Logger log = Logger.getLogger(AppInfoWizardAction.class);
	public AppInfoWizardAction(Shell shell) { 
		//this.window = window;
		this.shell = shell;
	}
	
	@Override
	public void run() {
		try {
			AppInfoWizard wizard = new AppInfoWizard(shell);
			DynaWizardDialog dialog = new DynaWizardDialog(shell, wizard);
			dialog.open();
		} catch (Exception e) {
			log.error("Error running the AppInfoWizard", e);
		}
		
	}
}
