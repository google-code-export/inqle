package com.beyobe.client.beans;

import java.util.List;

import org.springframework.roo.addon.json.RooJson;

import com.beyobe.domain.Datum;
import com.beyobe.domain.Participant;
import com.beyobe.domain.Question;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
 
@RooJson
public class Parcel {
	private List<Question> questionQueue;
	private List<Question> otherKnownQuestions;
	private List<Datum> data;
	private Participant participant;
	private String sessionToken;
	private String username;
	private String password;
	private Question question;
	private Datum datum;
	
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

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Datum getDatum() {
		return datum;
	}

	public void setDatum(Datum datum) {
		this.datum = datum;
	}

	public static Parcel fromJsonToParcel(String json) {
	   return new JSONDeserializer<Parcel>().use(null, Parcel.class).deserialize(json);
	}

	public String toJson() {
		return new JSONSerializer().deepSerialize( this );
	}
}
