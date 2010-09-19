package org.inqle.qa;

import java.util.List;

import com.google.appengine.api.datastore.Entity;


public interface AskableQuestionFactory {

	public AskableQuestion getAskableQuestion(Object questionKey, String lang);

	public AskableQuestion getAskableQuestion(Entity questionEntity, String lang);
	
	public List<AskableQuestion> listAllAskableQuestions(String lang);

	
}
