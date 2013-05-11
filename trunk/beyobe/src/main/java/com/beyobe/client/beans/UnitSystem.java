package com.beyobe.client.beans;



public enum UnitSystem {
	ENGLISH("English", "pounds, inches"), 
	METRIC("Metric", "kg, cm"), 
	UNIVERSAL("Universal", "time, blood pressure");
	
	private String description;
	private String name;
	
	public String getDescription() {
		return description;
	}

	UnitSystem(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
