package org.inqle.qa;



public interface AnswerBroker {

	public void storeAnswer(Answer answer);
	
	public Answer getAnswer(Object answerKey);
	
}
