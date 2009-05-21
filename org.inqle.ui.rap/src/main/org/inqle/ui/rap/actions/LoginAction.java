package org.inqle.ui.rap.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.UserAccount;
import org.inqle.ui.rap.AuthenticationProvider;
import org.inqle.ui.rap.tree.PartsView;
import org.inqle.ui.rap.widgets.LoginDialog;

public class LoginAction extends Action {

	private Shell shell;

	private LoginDialog loginDialog;

	public static final String USER_ID_ATTRIBUTE = "org.inqle.userid";

	public static final String ADMIN_USER = "admin";

	public static final String USER_ROLE_ATTRIBUTE = "org.inqle.userRole";

	private static Logger log = Logger.getLogger(LoginAction.class);
	public LoginAction(Shell shell) { 
		super("Login");
		//this.window = window;
		this.shell = shell;
	}
	
	@Override
	public void run() {
		try {
			loginDialog = new LoginDialog(shell);
			loginDialog.open();
		} catch (Exception e) {
			log.error("Error running the LoginDialog", e);
		}
		
		//populate session variable with the roles
		List<String> roles = new ArrayList<String>();
		HttpSession session = RWT.getSessionStore().getHttpSession();
		if (isAdminUser()) {
			session.setAttribute(LoginAction.USER_ID_ATTRIBUTE, loginDialog.getUserName());
			roles.add(LoginAction.ADMIN_USER);
		}
		
		session.setAttribute(LoginAction.USER_ROLE_ATTRIBUTE, roles);
		new AuthenticationProvider().updateRights();
		
		//refresh the navigation tree menu
		try {
			IViewPart treeView = PlatformUI.getWorkbench().getActiveWorkbenchWindow ().getActivePage().showView(PartsView.ID);
			PartsView partsView = (PartsView)treeView;
			partsView.refresh();
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public boolean isAdminUser() {
		String login = loginDialog.getUserName();
		String password = loginDialog.getPassword();
		if (login==null) login = "";
		if (password==null) password = "";
		
//		log.info("User entered login=" + login + "; password=" + password);
		Persister persister = Persister.getInstance();
		AppInfo appInfo = persister.getAppInfo();
//		log.info("Retrieved AppInfo:" + JenabeanWriter.toString(appInfo));
		Collection<UserAccount> adminAccounts = appInfo.getAdminAccounts();
		for (UserAccount adminAccount: adminAccounts) {
//			log.info("Testing adminAccount: user name=" + adminAccount.getUserName() + "; password=" + adminAccount.getPassword());
			if (login.equals(adminAccount.getUserName()) && password.equals(adminAccount.getPassword())) {
				return true;
			}
		}
		return false;
	}
}
