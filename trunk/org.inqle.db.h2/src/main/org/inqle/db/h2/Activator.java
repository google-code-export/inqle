package org.inqle.db.h2;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.h2.tools.Server;
import org.inqle.core.util.InqleInfo;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.inqle.db.h2";

	private static final String H2_FOLDER = "H2";

	private static Logger log = Logger.getLogger(Activator.class);
	
	// The shared instance
	private static Activator plugin;

	private Server dbServer;
	private Server webServer;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		//log.info("H2 database options");
		// start the H2 Database dbServer's TCP Server
		String[] args = { "-trace", "-tcp", "-web", "-pg", "-browser", "-baseDir " + getH2Directory() };
		
		dbServer = Server.createTcpServer(args).start();
		log.info("Started H2 DB Server using these args:" + Arrays.asList(args));
		log.info("H2 DBServer is running? " + dbServer.isRunning(true));
		log.info("H2 DBServer running at " + dbServer.getURL());
		
		webServer = Server.createWebServer(args).start();
		log.info("Started H2 Web Server using these args:" + Arrays.asList(args));
		log.info("H2 Web Server is running? " + webServer.isRunning(true));
		log.info("H2 Web Server running at " + webServer.getURL());
	}

	private String getH2Directory() {
		//String h2Directory = InqleInfo.getAssetsDirectory() + H2_FOLDER;
		//TODO move setting system property inqle.home into inqle.core and ensure it is started early
		String h2Directory = Platform.getInstallLocation().getURL().getPath() + InqleInfo.ASSETS_FOLDER + "/" + H2_FOLDER + "/";
		log.info("H2 Directory = " + h2Directory);
		return h2Directory;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		webServer.stop();
		log.info("Stopped H2 Web Server.");
		
		dbServer.stop();
		log.info("Stopped H2 Database Server.");
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
