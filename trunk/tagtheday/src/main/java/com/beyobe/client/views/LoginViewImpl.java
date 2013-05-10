package com.beyobe.client.views;

import com.beyobe.client.App;
import com.beyobe.client.activities.TagdayPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite implements LoginView {

	@UiField Label message;
	@UiField TextBox userName;
	@UiField TextBox password;
	
	private Presenter presenter;
	
	private static LoginViewImplUiBinder uiBinder = GWT
			.create(LoginViewImplUiBinder.class);

	interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl> {
	}

//	public LoginViewImpl() {
//		initWidget(uiBinder.createAndBindUi(this));
//	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public LoginViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("submitButton")
	void onClick(ClickEvent e) {
//		Window.alert("Hello!");
//		App.eventBus.fireEvent(new LoginEvent(userName.getText(), password.getText()));
		
		//TODO: try to login
		int status = App.teller.loginUser(presenter, userName.getText(), password.getText());
		Window.alert("Tried to login.  Success? " + status);
		if (status < 1) {
			message.setText("Login failed: " + status);
		}
//		if (App.isUserLoggedIn()) {
//			presenter.goTo(new TagdayPlace());
//		} else {
//			message.setText("Login failed: " + status);
//		}
	}

}
