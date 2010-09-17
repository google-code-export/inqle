package org.inqle.qa;

public class Option {
	private String entityKey;
	private String id;
	private String text;
	private String description;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		String s = "Option [";
		s+= "\n  entityKey=" + entityKey;
		s+= "\n  id=" + id;
		s+= "\n  text=" + text;
		s+= "\n  description=" + description;
		s+= "]";
		return s;
	}
}
