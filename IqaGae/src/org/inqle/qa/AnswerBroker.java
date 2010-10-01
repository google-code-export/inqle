package org.inqle.qa;

import java.util.List;

import com.google.appengine.api.datastore.Entity;


public interface AnswerBroker {

	public void storeAnswer(Answer answer);
	
	public Answer getAnswer(Object answerKey);
	
}
