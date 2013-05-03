package com.beyobe.client.beans;

import java.util.Date;
import java.util.List;

public class Question {
	public static final int DATA_TYPE_UNSPECIFIED = 0;
	public static final int DATA_TYPE_DOUBLE = 1;
	public static final int DATA_TYPE_INTEGER = 2;
	public static final int DATA_TYPE_MULTIPLE_CHOICE = 3;
	public static final int DATA_TYPE_SHORT_TEXT = 4;
	public static final int DATA_TYPE_LONG_TEXT = 5;
	public static final int DATA_TYPE_STARS = 6;
	
	private long id;
	private String uid;
	private String longForm;
//	private String shortForm;
    private String abbreviation;
    private Date created;
    private Date updated;
    private long conceptId;
    private long creatorId;
    private String creatorName;
    private Integer priority;
    private String lang;
    private int dataType = DATA_TYPE_UNSPECIFIED;
//    private Unit referenceUnit;
    private Measurement measurement;
    
	private List<Choice> choices;
	private Double minValue;
	public Double getMinValue() {
		return minValue;
	}
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	public Double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	private Double maxValue;
    
   
	public String getLongForm() {
		return longForm;
	}
	public void setLongForm(String longForm) {
		this.longForm = longForm;
	}
//	public String getShortForm() {
//		return shortForm;
//	}
//	public void setShortForm(String shortForm) {
//		this.shortForm = shortForm;
//	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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
	public long getConceptId() {
		return conceptId;
	}
	public void setConceptId(long conceptId) {
		this.conceptId = conceptId;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int answerType) {
		this.dataType = answerType;
	}
//	public Unit getReferenceUnit() {
//		return referenceUnit;
//	}
//	public void setReferenceUnit(Unit referenceUnit) {
//		this.referenceUnit = referenceUnit;
//	}
	public List<Choice> getChoices() {
		return choices;
	}
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Measurement getMeasurement() {
		return measurement;
	}
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
}
