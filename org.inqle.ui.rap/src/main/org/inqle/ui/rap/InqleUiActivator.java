package org.inqle.ui.rap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.inqle.core.util.InqleInfo;
import org.osgi.framework.BundleContext;

public class InqleUiActivator extends Plugin {

	private static InqleUiActivator plugin;

	public InqleUiActivator() {
		
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	
		//set some filepath environment variables
		String inqleHome = Platform.getInstallLocation().getURL().getPath();
		System.setProperty(InqleInfo.INQLE_HOME, inqleHome);
		System.setProperty("java.io.tmpdir", inqleHome + InqleInfo.TEMP_FOLDER);
	
		//try to initialize the Persister
//		PersisterInitializer.initialize();
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static InqleUiActivator getDefault() {
		return plugin;
	}
}
