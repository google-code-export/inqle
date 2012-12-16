package org.inqle.ui.rap;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.ui.rap.actions.AboutAction;
import org.inqle.ui.rap.actions.LoginAction;
import org.inqle.ui.rap.actions.NewBrowserAction;
import org.inqle.ui.rap.actions.OpenViewAction;
import org.inqle.ui.rap.util.ExtensionSecurityManager;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

		public static final String VIEWS = "org.eclipse.ui.views";
    public static final String NAME = "name";
		public static final String ID = "id";
		public static final String ICON = "icon";
//		private static final String H2_ADMIN_URL = "http://localhost:8082";
//		private static final String H2_ADMIN_TEXT = "Administer Embedded H2 Server";
		public static final String PLUGIN_ID = "org.inqle.ui.rap";
//		private static final String H2_ADMIN_ICON_PATH = "icons/h2.jpeg";
		// Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    private IAction aboutAction;
//    private IAction h2AdminAction;
    //private OpenViewAction openViewAction;
    //private Action messagePopupAction;
		private List<OpenViewAction> openViewActions = new ArrayList<OpenViewAction>();
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = new AboutAction(window);
        register(aboutAction);
        
//        h2AdminAction = new NewBrowserAction(H2_ADMIN_URL, H2_ADMIN_TEXT, PLUGIN_ID, H2_ADMIN_ICON_PATH);
//        register(h2AdminAction);
        
        //Create a new OpenViewAction for each view plugin
        List<IExtensionSpec> extensionSpecs = ExtensionSecurityManager.getPermittedExtensionSpecs(VIEWS);
        for(IExtensionSpec extensionSpec: extensionSpecs) {
//        	OpenViewAction openViewAction = new OpenViewAction(window, extensionSpec.getAttribute(NAME), extensionSpec.getAttribute(ID), extensionSpec.getPluginId(), extensionSpec.getAttribute(ICON));
//        	openViewActions.add(openViewAction);
//        	register(openViewAction);
        	
        	String icon = extensionSpec.getAttribute(ICON);
        	OpenViewAction openViewAction = new OpenViewAction(window, extensionSpec.getAttribute(NAME), extensionSpec.getAttribute(ID), extensionSpec.getPluginId(), icon);
        	if (icon != null && icon.length() > 0) {
        		openViewActions.add(openViewAction);
        	}
        	register(openViewAction);
        }
        
//        
//        messagePopupAction = new MessagePopupAction("Open Message", window);
//        register(messagePopupAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
//        MenuManager toolsMenu = new MenuManager("&Tools", IWorkbenchActionConstants.M_LAUNCH);
//        MenuManager loginMenu = new MenuManager("&Login", IWorkbenchActionConstants.M_EDIT);
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(windowMenu);
//        menuBar.add(toolsMenu);
//        menuBar.add(loginMenu);
        menuBar.add(helpMenu);
        
//        Display display = PlatformUI.createDisplay();
//        LoginAction loginAction = new LoginAction(display.getActiveShell());
//        loginMenu.add(loginAction);
        
        // File
        //fileMenu.add(messagePopupAction);
        //fileMenu.add(openViewAction);
        //fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        //window
        for(OpenViewAction openViewAction: openViewActions) {
        	windowMenu.add(openViewAction);
        }
        
        //Tools
//        toolsMenu.add(h2AdminAction);
        
        // Help
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
//        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
//        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        //toolbar.add(openViewAction);
        //toolbar.add(messagePopupAction);
    }
}
