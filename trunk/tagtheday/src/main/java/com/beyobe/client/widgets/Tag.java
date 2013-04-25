package com.beyobe.client.widgets;

import java.util.UUID;

import com.google.gwt.user.client.ui.Composite;

public class Tag extends Composite {

	public Tag() {
		
	}

	public String getLabel(){
		return "Tag-" + System.currentTimeMillis();
	}
}
