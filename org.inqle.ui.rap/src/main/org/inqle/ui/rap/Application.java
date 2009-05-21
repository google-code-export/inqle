package org.inqle.ui.rap;

import org.apache.log4j.Logger;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.actions.AppInfoWizardAction;
import org.inqle.ui.rap.actions.LoginAction;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {

//	private static final int ALLOWED_NUMBER_OF_LOGIN_ATTEMPTS = 5;
	private Logger log = Logger.getLogger(Application.class);
	
	public int createUI() {
//		String inqleHome = Platform.getInstallLocation().getURL().getPath();
//		System.setProperty(InqleInfo.INQLE_HOME, inqleHome);
//		System.setProperty("java.io.tmpdir", inqleHome + InqleInfo.TEMP_FOLDER);
		
		Display display = PlatformUI.createDisplay();
		
		//if no AppInfo, run the setup wizard
		Persister persister = Persister.getInstance();
		while (persister.getAppInfo() == null) {
			try {
				AppInfoWizardAction appInfoWizardAction = new AppInfoWizardAction(display.getActiveShell());
				appInfoWizardAction.run();
				if (persister.getAppInfo() != null) {
					log.info("Registering Site with central INQLE server...");
					log.info("Success? " + Requestor.registerObject(persister.getAppInfo().getSite()));
				}
			} catch (Exception e) {
				log.error("Error running setup wizard", e);
			}
		}
		
		//display login dialog until logged in.
//		boolean loggedIn = false;
//		int numberOfAttempts = 0;
//		while (loggedIn == false && numberOfAttempts < ALLOWED_NUMBER_OF_LOGIN_ATTEMPTS) {
//			LoginAction loginAction = new LoginAction(display.getActiveShell());
//			loginAction.run();
//			loggedIn = loginAction.isAdminUser();
//			numberOfAttempts++;
//		}
//		
//		if (loggedIn) {
			//initialize the (singleton) Persister
			Persister.getInstance().initialize();
			
			PlatformUI.createAndRunWorkbench( display, new ApplicationWorkbenchAdvisor() );
			return PlatformUI.RETURN_OK;
//		} else {
//			return PlatformUI.RETURN_EMERGENCY_CLOSE;
//		}
	}
}
