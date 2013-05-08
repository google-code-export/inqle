package com.beyobe.client.event;

import com.beyobe.client.widgets.TagButton;
import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {

public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
private TagButton tagButton;
private String userName;
private String password;

  public LoginEvent(String userName, String password) {
	this.userName = userName;
	this.password = password;
}

@Override
public Type<LoginEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(LoginEventHandler handler) {
    handler.onLogin(this);
}

public TagButton getTagButton() {
	return tagButton;
}

}