package com.beyobe.client.beans;

public class Choice {
	private String id;

	private String text;

	private String description;
	
	public Choice(){}
	
	public Choice(String text, String description) {
		this.text = text;
		this.description = description;
	}
	
	public String getId() {
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

}
