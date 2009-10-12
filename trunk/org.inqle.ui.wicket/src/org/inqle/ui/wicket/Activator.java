package org.inqle.ui.wicket;

import com.antilia.web.AntiliaWebApplication;
import com.antilia.web.osgi.WebApplicationActivator;

public class Activator extends WebApplicationActivator {

	public Activator() {	
		//addServiceActivator(new StartDerbyActivator());
	}
	
	@Override
	protected Class<? extends AntiliaWebApplication> getApplicationClass() {
		return InqleApplication.class;
	}
	
	@Override
	protected String getServletAlias() {
		return "inqle";
	}
	
}
