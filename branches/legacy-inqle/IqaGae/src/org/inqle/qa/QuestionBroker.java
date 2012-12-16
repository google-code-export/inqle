package org.inqle.qa;

import java.util.List;

public interface QuestionBroker {

	public List<Question> listAllQuestions(String lang);
	
	public Question getQuestion(Object questionKeyObj, String lang);
}
