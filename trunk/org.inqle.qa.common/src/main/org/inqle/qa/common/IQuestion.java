package org.inqle.qa.common;

import java.util.Collection;
import java.util.List;

public interface IQuestion {
 
	public void setQuestionTranslations(Collection<ITranslation> questionTranslations);
	
	public Collection<ITranslation> getQuestionTranslations();
	
	public void addQuestionTranslation(ITranslation questionTranslation);
	
	public void setQuestionType(String questionType);
	
	public String getQuestionType();
	
	public void setQuestionOptions(List<IOption> questionOptions);
	
	public List<IOption> getQuestionOptions();
	
	public ITranslation getQuestionTranslation(String lang);
	
	public ITranslation getDefaultQuestionTranslation();
 
}