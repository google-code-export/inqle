package org.inqle.qa.common;

import java.util.Collection;

public interface IAnswer {

	public IQuestion getQuestion();

	public void setQuestion(IQuestion question);

	public void setSelectedOptions(Collection<IOption> selectedOptions);

	public Collection<IOption> getSelectedOptions();
}
