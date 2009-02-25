package org.inqle.ui.rap.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Opens a new browser.
 */
public class NewBrowserAction extends Action {
	
//	private static final String H2_ADMIN_BROWSER_ID = "H2_database_administration";
	private String url;
	private String browserId;
//	private static Logger log = Logger.getLogger(NewBrowserAction.class);
	public NewBrowserAction(String url, String title, String pluginId, String iconPath, String browserId) {
		super(title);
		this.url = url;
		this.browserId = browserId;
		setId(this.getClass().getName());
		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, "/" + iconPath));
	}
	
	public void run() {
		ExternalBrowser.open(browserId, url, ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR | ExternalBrowser.STATUS);
	}
}
