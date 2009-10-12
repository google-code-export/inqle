package org.inqle.qa.common;

import java.util.Collection;

public class Answer implements IAnswer {

	private IQuestion question;
	private Collection<IOption> selectedOptions;
	
	/**
	 * Private constructor.  Use QAFactory.newAnswer();
	 */
	Answer(){};
	
	public IQuestion getQuestion() {
		return question;
	}

	public void setQuestion(IQuestion question) {
		this.question = question;
	}

	public void setSelectedOptions(Collection<IOption> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public Collection<IOption> getSelectedOptions() {
		return selectedOptions;
	}
	
	
}
