package com.beyobe.client.beans;

public class Choice {
	private long id;

	private String shortForm;

	private String longForm;
	
	public Choice(long id, String shortForm, String longForm) {
		this.id = id;
		this.shortForm = shortForm;
		this.longForm = longForm;
	}
	
	public long getId() {
		return id;
	}
	
	public String getShortForm() {
		return shortForm;
	}
	
	public String getLongForm() {
		return longForm;
	}

}
