package org.inqle.qa.common;

public class Translation implements ITranslation {
	public String text;
	public String lang;
	
	/**
	 * Private constructor.  Use QAFactory.newTranslation();
	 */
	Translation(){};
	
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
