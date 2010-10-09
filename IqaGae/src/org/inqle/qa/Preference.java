package org.inqle.qa;

import java.util.Date;

public class Preference implements IQABean {

	private String id;
	private String key;
	private String user;
//	private String question;
//	private Date moratoriumUntil;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

//	public String getQuestion() {
//		return question;
//	}
//
//	public void setQuestion(String question) {
//		this.question = question;
//	}
//
//	public Date getMoratoriumUntil() {
//		return moratoriumUntil;
//	}
//
//	public void setMoratoriumUntil(Date moratoriumUntil) {
//		this.moratoriumUntil = moratoriumUntil;
//	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

}
