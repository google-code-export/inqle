package com.beyobe.domain;

import java.util.List;



public class Parcel {
	
	private List<Question> questionQueue;
	private List<Question> otherKnownQuestions;
	private List<Datum> data;
	private Participant participant;
	
	public List<Question> getQuestionQueue() {
		return questionQueue;
	}
	public void setQuestionQueue(List<Question> questionQueue) {
		this.questionQueue = questionQueue;
	}
	public List<Question> getOtherKnownQuestions() {
		return otherKnownQuestions;
	}
	public void setOtherKnownQuestions(List<Question> otherKnownQuestions) {
		this.otherKnownQuestions = otherKnownQuestions;
	}
	public List<Datum> getData() {
		return data;
	}
	public void setData(List<Datum> data) {
		this.data = data;
	}
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

}
