package com.beyobe.client.beans;

import java.util.List;

public interface Parcel {
//	public Directive getDirective();
//	public void setDirective(Directive directive);
	
	public Participant getParticipant();
	public void setParticipant(Participant participant);
	public String getSessionToken();
	public void setSessionToken(String sessionToken);
	public String getUsername();
	public void setUsername(String userName);
	public String getPassword();
	public void setPassword(String text);
	
//	public List<Question> getQuestionQueue();
//	public void setQuestionQueue(List<Question> questions);
	
	public List<Question> getOtherKnownQuestions();
	public void setOtherKnownQuestions(List<Question> questions);
	
	public Question getQuestion();
	public void setQuestion(Question question);
	public List<Question> getQuestions();
	public void setQuestions(List<Question> questions);
	
	public Datum getDatum();
	public void setDatum(Datum datum);
	public List<Datum> getData();
	public void setData(List<Datum> data);
	
	public Message getMessage();
	public void setMessage(Message message);
	public String getQueryTerm();
	public void setQueryTerm(String queryTerm);
	
}
