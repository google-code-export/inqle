package com.beyobe.client.beans;

import java.util.List;

public interface Parcel {

	public List<Question> getQuestionQueue();
	public List<Question> getOtherKnownQuestions();
	public List<Datum> getData();
	public Participant getParticipant();
}
