package org.inqle.qa.gae;

import org.inqle.qa.AskableQuestion;

public interface AskableQuestionFactory {

	public AskableQuestion getAskableQuestion(Object questionKey, String lang);
}
