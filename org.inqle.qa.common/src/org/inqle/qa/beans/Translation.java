package org.inqle.qa.beans;

import java.io.Serializable;

import org.inqle.core.data.ModelObject;

public class Translation extends ModelObject implements Serializable {
	public String text;
	public String lang;
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	
}
