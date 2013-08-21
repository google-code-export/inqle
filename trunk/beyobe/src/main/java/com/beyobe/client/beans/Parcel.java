package com.beyobe.client.beans;

import java.util.List;

import org.springframework.roo.addon.json.RooJson;

import com.beyobe.client.util.FromGwtDateObjectFactory;
import com.beyobe.domain.Datum;
import com.beyobe.domain.Session;
import com.beyobe.domain.Question;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
 
@RooJson
public class Parcel {
//	private List<Question> questionQueue;
	private List<Question> otherKnownQuestions;
	private List<Datum> data;
	private Session session;
	private String sessionToken;
	private String username;
	private String password;
	private Question question;
	private Datum datum;
	private Message message;
	private String queryTerm;
	private List<Question> questions;
	private List<String> savedQuestions;
	private List<String> savedData;
	private String client;
	
//	public List<Question> getQuestionQueue() {
//		return questionQueue;
//	}
//
//	public void setQuestionQueue(List<Question> questionQueue) {
//		this.questionQueue = questionQueue;
//	}

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

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
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
		JSONDeserializer<Parcel> deserializer = new JSONDeserializer<Parcel>();
		FromGwtDateObjectFactory gwtFactory = new FromGwtDateObjectFactory();
		return deserializer
			.use(gwtFactory, new String[] {
					"question.created", "question.updated", "datum.created", "datum.updated", "datum.effectiveDate"})
	    	.use(null, Parcel.class)
	    	.deserialize(json);
	}

	public String toJson() {
		return new JSONSerializer()
		.exclude("*.class")
    	.exclude("*.created")
    	.exclude("*.updated")
    	.exclude("*.createdBy")
    	.exclude("*.updatedBy")
    	.exclude("*.password")
    	.exclude("*.owner")
    	.exclude("*.clientIpAddress")
    	.exclude("datum.question")
    	.exclude("datum.formula")
    	.exclude("data.question")
    	.exclude("data.session")
//    	.exclude("session.id")
    	.exclude("*.version")
		.deepSerialize( this );
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getQueryTerm() {
		return queryTerm;
	}
	
	public void setQueryTerm(String queryTerm) {
		this.queryTerm = queryTerm;
	}

	public List<Question> getQuestions() {
		return questions;
	}
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<String> getSavedQuestions() {
		return savedQuestions;
	}

	public void setSavedQuestions(List<String> savedQuestions) {
		this.savedQuestions = savedQuestions;
	}

	public List<String> getSavedData() {
		return savedData;
	}

	public void setSavedData(List<String> savedData) {
		this.savedData = savedData;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}
}
