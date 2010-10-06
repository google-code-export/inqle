package org.inqle.qa;

import java.util.List;


public interface QuestionBroker {

	public Question getQuestion(Object questionKey, String lang);
	
	public List<Question> listAllQuestions(String lang);
	
}
