package org.inqle.qa;

import java.util.List;

import com.google.appengine.api.datastore.Entity;


public interface QuestionFactory {

	public Question getQuestion(Object questionKey, String lang);

	public Question getQuestion(Entity questionEntity, String lang);
	
	public List<Question> listAllQuestions(String lang);

	
}
