package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
public class NewBrowserAction extends Action {
	
	private final IWorkbenchWindow window;
	private String url;
	private static Logger log = Logger.getLogger(NewBrowserAction.class);
	public NewBrowserAction(IWorkbenchWindow window, String url, String title, String pluginId, String iconPath) {
		super(title);
		this.url = url;
		setId(this.getClass().getName());
		this.window = window;
		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "/" + iconPath));
	}
	
	public void run() {
		if(window != null) {
			try {
				//Shell newShell = new Shell(window.getWorkbench().getDisplay());
				//Shell newShell = new Shell(window.getWorkbench().getDisplay());
//				Shell newShell = new Shell(Display.getCurrent());
//				Browser browser = new Browser(newShell, SWT.NONE);
//				browser.setUrl(url);
//				newShell.open();
				//TODO figure out how to open in a new browser window, rather than the current
				Browser browser = new Browser(window.getShell(), SWT.NONE);
				browser.setUrl(url);
				
			} catch (Exception e) {
				log.error("Unable to open new browser window", e);
			}
		}
	}
	
}
