package org.inqle.qa.beans;

import java.io.Serializable;

import org.inqle.core.data.GlobalModelObject;

public class Translation extends GlobalModelObject implements Serializable {

	private static final long serialVersionUID = 551131315984762079L;
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
	
	public String getDefiningStringRepresentation() {
		return "Translation [lang=" + lang + ", text=" + text + "]";
	}
}
