package org.inqle.qa;

public class Option  implements IQABean{
	private String key;
	private String id;
	private String text;
	private String description;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		String s = "Option [";
		s+= "\n  key=" + key;
		s+= "\n  id=" + id;
		s+= "\n  text=" + text;
		s+= "\n  description=" + description;
		s+= "]";
		return s;
	}
}
