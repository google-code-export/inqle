package org.inqle.ui.rap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.inqle.core.util.InqleInfo;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {

	public int createUI() {
		String inqleHome = Platform.getInstallLocation().getURL().getPath();
		System.setProperty(InqleInfo.INQLE_HOME, inqleHome);
		System.setProperty("java.io.tmpdir", inqleHome + InqleInfo.TEMP_FOLDER);
		Display display = PlatformUI.createDisplay();
		PlatformUI.createAndRunWorkbench( display, new ApplicationWorkbenchAdvisor() );
		return PlatformUI.RETURN_OK;
	}

//	public Display createUI() {
//		
//		System.setProperty("INQLE.HOME", Platform.getInstallLocation().getURL().getPath());
//		Display display = PlatformUI.createDisplay();
//		PlatformUI.createAndRunWorkbench( display, new ApplicationWorkbenchAdvisor() );
//		return display;
//	}
}
