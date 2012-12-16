package org.inqle.ui.rap;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.RWT;
import org.eclipse.ui.AbstractSourceProvider;
import org.inqle.ui.rap.actions.LoginAction;

/**
 * This class supports security using activities.
 * Not sure if it works
 * @author David Donohue
 * May 21, 2009
 * @deprecated
 */
public class AuthenticationProvider extends AbstractSourceProvider {
 
 
  public AuthenticationProvider() {
    //
  }
 
  public void dispose() {
    //
  }
 
  public Map<String, Object> getCurrentState() {
    Map<String, Object> variables = new HashMap<String, Object>();
    HttpSession session = RWT.getSessionStore().getHttpSession();
    variables.put(LoginAction.USER_ROLE_ATTRIBUTE, session.getAttribute(LoginAction.USER_ROLE_ATTRIBUTE));
    return variables;
  }
 
  public String[] getProvidedSourceNames() {
    return new String[]{
    		LoginAction.USER_ROLE_ATTRIBUTE
    };
  }
 
  public void updateRights() {
    fireSourceChanged( 0, getCurrentState() );
  }
 }