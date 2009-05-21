package org.inqle.ui.rap;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	public static final String ADMIN_PERSPECTIVE_ID = "org.inqle.ui.rap.perspective";
//	public static final String WIKI_PERSPECTIVE_ID = "org.inqle.ui.rap.WikiPerspective";
    
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    return new ApplicationWorkbenchWindowAdvisor(configurer);
  }

	public String getInitialWindowPerspectiveId() {
		return ADMIN_PERSPECTIVE_ID;
	} 
	
}
