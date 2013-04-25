package com.beyobe.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.googlecode.mgwt.ui.client.theme.base.CarouselCss;

public interface AppBundle extends ClientBundle {
	//This is a very nasty workaround because GWT CssResource does not support @media correctly!
	@Source("app.css")
	TextResource css();

	public static final AppBundle INSTANCE = GWT.create(AppBundle.class);
}
