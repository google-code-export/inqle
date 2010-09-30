package org.inqle.qa;

import java.util.Date;
import java.util.List;

public class Answer {

	private String entityKey;
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
	
	public String getEntityKey() {
		return entityKey;
	}
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
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
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
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
	}
	public void setReferenceUnit(Unit referenceUnit) {
		this.referenceUnit = referenceUnit;
	}
	public Unit getReferenceUnit() {
		return referenceUnit;
	}
	public String toString() {
		String s = "Answer:[";
		s += "\n  entityKey=" + entityKey;
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
		s += "]";
		return s;
	}
}
