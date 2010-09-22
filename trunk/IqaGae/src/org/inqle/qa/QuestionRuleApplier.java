package org.inqle.qa;

import java.util.List;

import org.inqle.qa.AskableQuestion;

public interface QuestionRuleApplier {

	List<AskableQuestion> getApplicableQuestions(String userId, String lang);

}
