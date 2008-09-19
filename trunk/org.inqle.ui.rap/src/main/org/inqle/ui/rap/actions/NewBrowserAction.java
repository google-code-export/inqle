package org.inqle.ui.rap.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
public class NewBrowserAction extends Action {
	
	private static final String H2_ADMIN_BROWSER_ID = "H2_database_administration";
	private String url;
//	private static Logger log = Logger.getLogger(NewBrowserAction.class);
	public NewBrowserAction(String url, String title, String pluginId, String iconPath) {
		super(title);
		this.url = url;
		setId(this.getClass().getName());
		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "/" + iconPath));
	}
	
	public void run() {
		ExternalBrowser.open(H2_ADMIN_BROWSER_ID, url, ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR | ExternalBrowser.STATUS);
	}
}
