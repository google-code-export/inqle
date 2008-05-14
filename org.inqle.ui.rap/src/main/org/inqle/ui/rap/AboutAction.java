package org.inqle.ui.rap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.core.util.InqleInfo;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
public class AboutAction extends Action {
	
	private final IWorkbenchWindow window;
	
	public AboutAction(IWorkbenchWindow window) {
		super("About");
		setId(this.getClass().getName());
		this.window = window;
	}
	
	public void run() {
		if(window != null) {	
			String title = "About INQLE";
			String msg =   "INQLE version " + InqleInfo.getInqleVersion() + ".\n\n"
			             + "INQLE is the property of INQLING, LLC.\n\n"
			             + "Project website: " + InqleInfo.PROJECT_WEBSITE + "\n\n"
									 + "Conceived and created by David P. Donohue";
			MessageDialog.openInformation( window.getShell(), title, msg ); 
		}
	}
	
}
