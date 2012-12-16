package org.inqle.ui.rap.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.RWT;
import org.inqle.ui.rap.actions.LoginAction;


public class InqleUiInfo {

	@SuppressWarnings("unchecked")
	public static List<String> getUserRoles() {
		HttpSession session = RWT.getSessionStore().getHttpSession();
		Object rolesObj = session.getAttribute(LoginAction.USER_ROLE_ATTRIBUTE);
		List<String> roles = new ArrayList<String>();
		if (rolesObj==null) return roles;
		roles = (List<String>)rolesObj;
		return roles;
	}
}
