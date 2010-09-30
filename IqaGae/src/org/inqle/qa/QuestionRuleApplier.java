package org.inqle.qa;

import java.util.List;

import org.inqle.qa.Question;

public interface QuestionRuleApplier {

	List<Question> getApplicableQuestions(String userId, String lang);

}
