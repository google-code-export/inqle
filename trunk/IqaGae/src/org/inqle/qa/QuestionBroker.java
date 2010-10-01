package org.inqle.qa;

import java.util.List;


public interface QuestionBroker {

	public Question getQuestion(Object questionKey, String lang);
	
	public List<Question> listAllQuestions(String lang);

	public List<Question> listTopQuestions(String lang, int numberOfQuestions);
	
	public List<Question> listRandomQuestions(String lang, int numberOfQuestions);
	
	/**
	 * List all askable questions
	 * @param lang the localization to use when building the questions
	 * @param numberOfQuestions the number to return
	 * @param priorityThreshold questions with this priority or better (lower number) will always be returned.
	 * @return
	 */
	public List<Question> listTopPlusRandomQuestions(String lang, int numberOfQuestions, int priorityThreshold);
	
}
