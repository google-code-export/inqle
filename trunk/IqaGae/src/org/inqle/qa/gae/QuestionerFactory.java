package org.inqle.qa.gae;

import org.inqle.qa.Questioner;

public interface QuestionerFactory {

	public Questioner getQuestioner(Object questionKey, String lang);
}
