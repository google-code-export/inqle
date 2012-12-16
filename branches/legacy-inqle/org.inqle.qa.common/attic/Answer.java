package org.inqle.qa.beans;

import java.util.Collection;
import java.io.Serializable;

import org.inqle.core.data.ModelObject;

public class Answer extends ModelObject implements Serializable {

	private static final long serialVersionUID = -5195167656076619583L;
	private Question question;
	private Collection<Option> selectedOptions;
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setSelectedOptions(Collection<Option> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public Collection<Option> getSelectedOptions() {
		return selectedOptions;
	}
	
	
}
