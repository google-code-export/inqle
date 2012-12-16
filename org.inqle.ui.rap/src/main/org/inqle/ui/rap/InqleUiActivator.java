package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.osgi.framework.BundleContext;

public class InqleUiActivator extends Plugin {

	private static Logger log = Logger.getLogger(InqleUiActivator.class);
	
	private static InqleUiActivator plugin;

	public InqleUiActivator() {
		
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		plugin = this;
	
		//set some filepath environment variables
		String inqleHome = System.getProperty(InqleInfo.INQLE_HOME);
		if (inqleHome == null) {
			inqleHome = Platform.getInstallLocation().getURL().getPath();
			System.setProperty(InqleInfo.INQLE_HOME, inqleHome);
		}
		System.setProperty("java.io.tmpdir", inqleHome + InqleInfo.TEMP_FOLDER);
		log.info("Set system property '" + InqleInfo.INQLE_HOME + "' to " + inqleHome);
		
		super.start(context);
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
