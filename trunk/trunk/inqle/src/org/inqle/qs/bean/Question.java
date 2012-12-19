package org.inqle.qs.bean;

import static org.inqle.qs.Constants.NS_Q;

import org.openrdf.annotations.Iri;
import org.openrdf.repository.object.LangString;

@Iri(NS_Q)
public class Question {
	private LangString title;
	private LangString question;	
	
	public LangString getTitle() {
		return title;
	}
	public void setTitle(LangString title) {
		this.title = title;
	}
	public LangString getQuestion() {
		return question;
	}
	public void setQuestion(LangString question) {
		this.question = question;
	}
	
}
