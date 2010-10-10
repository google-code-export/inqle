package org.inqle.qa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Answer implements IQABean {

	private String key;
	private String id;
	private String user;
	private Date date;
	private String question;
	private List<Option> selectedOptions;
	private String text;
	private Integer answerInteger;
	private Double answerDouble;
	private Double referenceValue;
	private Unit answerUnit;
	private Unit referenceUnit;
	private Date moratoriumUntil;
	
	public Answer() { }
	
	/**
	 * Construct an Answer, setting the ID according to the user ID, question ID, and current date
	 * @param user
	 * @param question
	 * @param date
	 */
	public Answer(String user, String question) {
		this(user, question, new Date());
	}
	
	/**
	 * Construct an Answer, setting the ID according to the user ID, question ID, and date
	 * @param user
	 * @param question
	 * @param date
	 */
	public Answer(String user, String question, Date date) {
		this.user = user;
		this.question = question;
		this.date = date;
		updateId();
	}
	
	private void updateId() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");	
		String dateTimeStr = format.format(date);
		id = user + "/" + question + "/" + dateTimeStr;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
		updateId();
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
		updateId();
	}
	public List<Option> getSelectedOptions() {
		return selectedOptions;
	}
	public void setSelectedOptions(List<Option> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getAnswerInteger() {
		return answerInteger;
	}
	public void setAnswerInteger(Integer answerInteger) {
		this.answerInteger = answerInteger;
	}
	public Double getAnswerDouble() {
		return answerDouble;
	}
	public void setAnswerDouble(Double answerDouble) {
		this.answerDouble = answerDouble;
	}
	public Double getReferenceValue() {
		return referenceValue;
	}
	public void setReferenceValue(Double referenceValue) {
		this.referenceValue = referenceValue;
	}
	public Unit getAnswerUnit() {
		return answerUnit;
	}
	public void setAnswerUnit(Unit answerUnit) {
		this.answerUnit = answerUnit;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
		updateId();
	}
	public void setReferenceUnit(Unit referenceUnit) {
		this.referenceUnit = referenceUnit;
	}
	public Unit getReferenceUnit() {
		return referenceUnit;
	}
	public String toString() {
		String s = "Answer:[";
		s += "\n  key=" + key;
		s += "\n  id=" + id;
		s += "\n  text=" + text;
		s += "\n  date=" + date;
		s += "\n  user=" + user;
		s += "\n  question=" + question;
		s += "\n  selectedOptions=" + selectedOptions;
		s += "\n  answerInteger=" + answerInteger;
		s += "\n  answerDouble=" + answerDouble;
		s += "\n  referenceValue=" + referenceValue;
		s += "\n  referenceUnit=" + referenceUnit;
		s += "\n  moratoriumUntil=" + moratoriumUntil;
		s += "]";
		return s;
	}

	public void setMoratoriumUntil(Date moratoriumUntil) {
		this.moratoriumUntil = moratoriumUntil;
	}

	public Date getMoratoriumUntil() {
		return moratoriumUntil;
	}
}
