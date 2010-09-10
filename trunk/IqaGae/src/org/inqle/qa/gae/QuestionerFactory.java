package org.inqle.qa.gae;

import org.inqle.qa.Questioner;

public interface QuestionerFactory {

	public Questioner getQuestioner(String questionKey, String lang);
}
