package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.inqle.ui.rap.ICommandIds;

/**
 * When run, this action will open another instance of a view.
 */
public class OpenViewAction extends Action {
	
	protected IWorkbenchWindow theWindow = null;
	//private int instanceNum = 0;
	protected String viewId = null;
	
	private static final Logger log = Logger.getLogger(OpenViewAction.class);
	
	public OpenViewAction(
			IWorkbenchWindow window, 
			String label, 
			String viewId, 
			String pluginId, 
			String iconPath) {
		log.trace("Create OpenViewAction: viewId=" + viewId + "; pluginId=" + pluginId + "; iconPath=" + iconPath);
		this.theWindow = window;
		this.viewId = viewId;
        setText(label);
    // The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN);
		
    // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN);
		//setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("org.inqle.ui.rap", "/icons/sample2.gif"));
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "/" + iconPath));
	}
	
	public void run() {
		if(theWindow != null) {	
			try {
				IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
				if (view==null) {
					view = (IViewPart)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
				}
				
				//window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
				theWindow.getActivePage().showView(viewId);
				theWindow.getActivePage().bringToTop(view);
			} catch (PartInitException e) {
				MessageDialog.openError(theWindow.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
