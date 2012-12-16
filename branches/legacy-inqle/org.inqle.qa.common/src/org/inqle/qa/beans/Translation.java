package org.inqle.qa.beans;

import org.inqle.qa.common.QAConstants;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;
import org.inqle.rdf.beans.GlobalJenabean;

import thewebsemantic.Namespace;

@TargetDatabaseId(QAConstants.QA_DATABASE)
@TargetModelName(QAConstants.DEFAULT_QUESTION_MODEL)
@Namespace(RDF.INQLE)
public class Translation extends GlobalJenabean {

	private static final long serialVersionUID = 4758619924199193098L;
	
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
	
	public String getStringRepresentation() {
		return "Translation [lang=" + lang + ", text=" + text + "]";
	}
}
