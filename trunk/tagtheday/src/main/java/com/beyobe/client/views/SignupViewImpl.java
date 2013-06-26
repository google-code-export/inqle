package com.beyobe.client.views;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.beans.Parcel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SignupViewImpl extends Composite implements SignupView {

	private static final long MAX_COUNTER = 10000;
	@UiField Label message;
	@UiField TextBox userName;
	@UiField TextBox password;
	@UiField TextBox password2;
	
	private Presenter presenter;
	private int status;
	
	private static SignupViewImplUiBinder uiBinder = GWT
			.create(SignupViewImplUiBinder.class);

	interface SignupViewImplUiBinder extends UiBinder<Widget, SignupViewImpl> {
	}

//	public SignupViewImpl() {
//		initWidget(uiBinder.createAndBindUi(this));
//	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public SignupViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

//	void onTestUsername() {
//		Parcel parcel = App.dataBus.newParcel();
//		parcel.setUsername(userName.getText());
//		parcel.setPassword(password.getText());
//		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_TESTUSERNAME);
//		long counter = 0;
//		boolean abortFlag = false;
//		while (! abortFlag) {
//			//increment a counter in case our timer fails us
//			counter++;
////			log.info("counter=" + counter);
//			if(counter > MAX_COUNTER) abortFlag = true;
//		}
//		status = App.parcelClient.getStatus();
//		if (status == Constants.STATUS_ALREADY_RUNNING) {
//			new Timer() {
//				@Override
//				public void run() {
//					status = App.parcelClient.getStatus();
//					if (status < 1) {
//						message.setText("Login failed: " + status);
//					}
//				}
//				}.schedule(Constants.TIMEOUT_SIGNUP);
//		} else {
//			if (status < 1) {
//				message.setText("Login failed: " + status);
//			}
//		}
//	}
	
	@UiHandler("submitButton")
	void onClick(ClickEvent e) {
		if (! isValidEmail() ) {
			message.setText("Please enter a valid email");
			return;
		}
		
		if (password.getText() == null || password.getText().length() < Constants.MINIMUM_PASSWORD_LENGTH) {
			message.setText("Your password should be a minimum of " + Constants.MINIMUM_PASSWORD_LENGTH + " characters.");
			return;
		}
		if (! (password.getText().equals(password2.getText()))) {
			message.setText("Your 2 passwords do not match");
		}
//		Window.alert("Hello!");
//		App.eventBus.fireEvent(new LoginEvent(userName.getText(), password.getText()));
		Parcel parcel = App.dataBus.newParcel();
		parcel.setUsername(userName.getText());
		parcel.setPassword(password.getText());
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_SIGNUP);
//		log.info("wait until request comes back or timer times out");
		long counter = 0;
		boolean abortFlag = false;
		while (! abortFlag) {
			//increment a counter in case our timer fails us
			counter++;
//			log.info("counter=" + counter);
			if(counter > MAX_COUNTER) abortFlag = true;
		}
		status = App.parcelClient.getStatus();
		//if no answer yet on login, that means the Timer in Teller failed. Delay x seconds then check again
		if (status == Constants.STATUS_ALREADY_RUNNING) {
			new Timer() {
				@Override
				public void run() {
					status = App.parcelClient.getStatus();
					if (status < 1) {
						message.setText("Login failed: " + status);
					}
				}
				}.schedule(Constants.TIMEOUT_SIGNUP);
		} else {
			if (status < 1) {
				message.setText("Login failed: " + status);
			}
		}
		
//		if (App.isUserLoggedIn()) {
//			presenter.goTo(new TagdayPlace());
//		} else {
//			message.setText("Login failed: " + status);
//		}
	}

	private boolean isValidEmail() {
		String email = getEmail();
		if (email == null) return false;
		RegExp regExp = RegExp.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
		return regExp.test(email);
	}
	
	public String getEmail() {
		String email = userName.getText();
		if (email == null) return null;
		return email.trim();
	}

}
