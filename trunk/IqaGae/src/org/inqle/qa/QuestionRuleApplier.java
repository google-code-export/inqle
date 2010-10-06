package org.inqle.qa;

import java.util.List;

import org.inqle.qa.Question;

public interface QuestionRuleApplier {

	List<Question> listAllApplicableQuestions(String userId, String lang);

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

	public List<Question> listApplicableTopPlusRandomQuestions(String lang, String user, int numberOfQuestions, int priorityThreshold);
}
