package com.beyobe.client.beans;

import java.util.Date;

public class Question {
	private long id;
	private String longForm;
	private String shortForm;
    private String abbreviation;
    private Date created;
    private Date updated;
    private long conceptId;
    private long creatorId;
    private String creatorName;
    private int priority;
    private String lang;
    
    public String getLongForm() {
		return longForm;
	}
	public void setLongForm(String longForm) {
		this.longForm = longForm;
	}
	public String getShortForm() {
		return shortForm;
	}
	public void setShortForm(String shortForm) {
		this.shortForm = shortForm;
	}
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
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
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
}
