package org.inqle.qa;

public class Unit {
	private String key;
	private String id;
	private String text;
	private String abbreviation;
	private Double relativeAmplitude;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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
	
	public String toString() {
		String s = "Unit [";
		s+= "\n  key=" + key;
		s+= "\n  id=" + id;
		s+= "\n  text=" + text;
		s+= "\n  abbreviation=" + abbreviation;
		s+= "\n  relativeAmplitude=" + relativeAmplitude;
		s+= "]";
		return s;
	}
}
