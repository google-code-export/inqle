package com.beyobe.client.beans;

public class Choice {
	private long id;

	private String text;

	private String description;
	
	private String uid;
	
	public Choice(){}
	
	public Choice(long id, String text, String description) {
		this.id = id;
		this.text = text;
		this.description = description;
	}
	
	public long getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
