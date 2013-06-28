package com.beyobe.client.beans;

import java.util.List;

public interface Parcel {
	public List<Question> getQuestionQueue();
	public void setQuestionQueue(List<Question> questions);
	public List<Question> getOtherKnownQuestions();
	public void setOtherKnownQuestions(List<Question> questions);
	public List<Datum> getData();
	public void setData(List<Datum> data);
	public Participant getParticipant();
	public void setParticipant(Participant participant);
	public String getSessionToken();
	public void setSessionToken(String sessionToken);
	public String getUsername();
	public void setUsername(String userName);
	public String getPassword();
	public void setPassword(String text);
	public Question getQuestion();
	public void setQuestion(Question question);
	public Datum getDatum();
	public void setDatum(Datum datum);
	public Message getMessage();
	public void setMessage(Message message);
}
