package com.beyobe.client.views;

import java.util.logging.Logger;

import org.mortbay.log.Log;

import sun.security.pkcs.SigningCertificateInfo;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.SignupPlace;
import com.beyobe.client.beans.Parcel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite implements LoginView {

	private static final Logger log = Logger.getLogger("LoginViewImpl");
	private static final long MAX_COUNTER = 10000;
	@UiField Label message;
	@UiField TextBox userName;
	@UiField TextBox password;
	@UiField Anchor signupLink;
	
	private Presenter presenter;
//	private int status;
	
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

	@UiHandler("signupLink")
	void onClickSignup(ClickEvent e) {
		log.info("signupLink clicked");
		App.placeController.goTo(new SignupPlace());
	}
	
	@UiHandler("submitButton")
	void onSubmit(ClickEvent e) {
//		Window.alert("Hello!");
//		App.eventBus.fireEvent(new LoginEvent(userName.getText(), password.getText()));
		Parcel parcel = App.dataBus.newParcel();
		parcel.setUsername(userName.getText());
		parcel.setPassword(password.getText());
		parcel.setAction(Constants.SERVERACTION_LOGIN);
		App.parcelClient.sendParcel(parcel);
//		log.info("wait until request comes back or timer times out");
//		long counter = 0;
//		boolean abortFlag = false;
//		while (! abortFlag) {
//			//increment a counter in case our timer fails us
//			counter++;
//			if(counter > MAX_COUNTER) abortFlag = true;
//		}
//		status = App.parcelClient.getStatus();
//		//if no answer yet on login, that means the Timer in Teller failed. Delay x seconds then check again
//		if (status == Constants.STATUS_ALREADY_RUNNING) {
//			new Timer() {
//				@Override
//				public void run() {
//					status = App.parcelClient.getStatus();
//					if (status < 1) {
//						message.setText("Login failed: " + status);
//					}
//				}
//				}.schedule(Constants.TIMEOUT_LOGIN);
//		} else {
//			if (status < 1) {
//				message.setText("Login failed: " + status);
//			}
//		}
	}
}
