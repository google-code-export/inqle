package org.inqle.qa;

public class Unit {
	private String entityKey;
	private String id;
	private String text;
	private String abbreviation;
	private Double relativeAmplitude;
	
	public String getEntityKey() {
		return entityKey;
	}
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public Double getRelativeAmplitude() {
		return relativeAmplitude;
	}
	public void setRelativeAmplitude(Double relativeAmplitude) {
		this.relativeAmplitude = relativeAmplitude;
	}
}
