package com.beyobe.client.beans;

import java.util.Date;

public class Datum {

	public static final Integer STATUS_ANSWERED_PERSONALLY = 2;
	public static final Integer STATUS_INFERRED = 1;
    public static final Integer STATUS_ASKED_BUT_NO_ANSWER = -1;
    public static final Integer STATUS_DECLINED_ANSWER = -2;
    public static final Integer STATUS_NEVER_ASK_AGAIN = -3;

    private long id;
    private Date created = new Date();
    private Date updated = null;
    private long questionId;
    private long participantId;
    private Double numericValue;
    private Integer integerValue;
    private String textValue;
    private String longTextValue;
    private Choice choice;
    private Double normalizedValue;
    private Unit unit;
//    private Unit canonicalUnit;
    private Double canonicalValue;
    private Integer status;
    private long conceptId;
	private Integer dataType;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public long getParticipantId() {
		return participantId;
	}
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
	public Double getNumericValue() {
		return numericValue;
	}
	public void setNumericValue(Double numericValue) {
		this.numericValue = numericValue;
	}
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public Choice getChoice() {
		return choice;
	}
	public void setChoice(Choice choice) {
		this.choice = choice;
	}
	public Double getNormalizedValue() {
		return normalizedValue;
	}
	public void setNormalizedValue(Double normalizedValue) {
		this.normalizedValue = normalizedValue;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
//	public Unit getCanonicalUnit() {
//		return canonicalUnit;
//	}
//	public void setCanonicalUnit(Unit canonicalUnit) {
//		this.canonicalUnit = canonicalUnit;
//	}
	public Double getCanonicalValue() {
		return canonicalValue;
	}
	public void setCanonicalValue(Double canonicalValue) {
		this.canonicalValue = canonicalValue;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public long getConceptId() {
		return conceptId;
	}
	public void setConceptId(long conceptId) {
		this.conceptId = conceptId;
	}
	public static Integer getStatusAskedButNoAnswer() {
		return STATUS_ASKED_BUT_NO_ANSWER;
	}
	public static Integer getStatusDeclinedAnswer() {
		return STATUS_DECLINED_ANSWER;
	}
	public static Integer getStatusNeverAskAgain() {
		return STATUS_NEVER_ASK_AGAIN;
	}
	public Integer getIntegerValue() {
		return integerValue;
	}
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getLongTextValue() {
		return longTextValue;
	}
	public void setLongTextValue(String longTextValue) {
		this.longTextValue = longTextValue;
	}
}
