package com.beyobe.client.beans;

import java.util.List;

import com.beyobe.domain.Datum;
import com.beyobe.domain.Participant;
import com.beyobe.domain.Question;

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
	public String getUserName();
	public void setUserName(String userName);
	public String getPassword();
	public void setPassword(String text);
	public Question getQuestion();
	public void setQuestion(Question question);
	public Datum getDatum();
	public void setDatum(Datum datum);
}
