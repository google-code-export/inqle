package org.inqle.ui.rap.actions;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.UserAccount;
import org.inqle.ui.rap.widgets.LoginDialog;

public class LoginAction extends Action {

	private Shell shell;

	private LoginDialog loginDialog;

	private static Logger log = Logger.getLogger(LoginAction.class);
	public LoginAction(Shell shell) { 
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
	}

	public boolean wasLoginSuccessful() {
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
