package com.beyobe.client.beans;

import java.util.Date;
import java.util.List;

public class QuestionImpl implements Question {
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
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getMinValue()
	 */
	@Override
	public Double getMinValue() {
		return minValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setMinValue(java.lang.Double)
	 */
	@Override
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getMaxValue()
	 */
	@Override
	public Double getMaxValue() {
		return maxValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setMaxValue(java.lang.Double)
	 */
	@Override
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setPriority(java.lang.Integer)
	 */
	@Override
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	private Double maxValue;
    
   
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getLongForm()
	 */
	@Override
	public String getLongForm() {
		return longForm;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setLongForm(java.lang.String)
	 */
	@Override
	public void setLongForm(String longForm) {
		this.longForm = longForm;
	}
//	public String getShortForm() {
//		return shortForm;
//	}
//	public void setShortForm(String shortForm) {
//		this.shortForm = shortForm;
//	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getAbbreviation()
	 */
	@Override
	public String getAbbreviation() {
		return abbreviation;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setAbbreviation(java.lang.String)
	 */
	@Override
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getCreated()
	 */
	@Override
	public Date getCreated() {
		return created;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setCreated(java.util.Date)
	 */
	@Override
	public void setCreated(Date created) {
		this.created = created;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getUpdated()
	 */
	@Override
	public Date getUpdated() {
		return updated;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setUpdated(java.util.Date)
	 */
	@Override
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getConceptId()
	 */
	@Override
	public long getConceptId() {
		return conceptId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setConceptId(long)
	 */
	@Override
	public void setConceptId(long conceptId) {
		this.conceptId = conceptId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getCreatorId()
	 */
	@Override
	public long getCreatorId() {
		return creatorId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setCreatorId(long)
	 */
	@Override
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getCreatorName()
	 */
	@Override
	public String getCreatorName() {
		return creatorName;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setCreatorName(java.lang.String)
	 */
	@Override
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getLang()
	 */
	@Override
	public String getLang() {
		return lang;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setLang(java.lang.String)
	 */
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getDataType()
	 */
	@Override
	public int getDataType() {
		return dataType;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setDataType(int)
	 */
	@Override
	public void setDataType(int answerType) {
		this.dataType = answerType;
	}
//	public Unit getReferenceUnit() {
//		return referenceUnit;
//	}
//	public void setReferenceUnit(Unit referenceUnit) {
//		this.referenceUnit = referenceUnit;
//	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getChoices()
	 */
	@Override
	public List<Choice> getChoices() {
		return choices;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setChoices(java.util.List)
	 */
	@Override
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getUid()
	 */
	@Override
	public String getUid() {
		return uid;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setUid(java.lang.String)
	 */
	@Override
	public void setUid(String uid) {
		this.uid = uid;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#getMeasurement()
	 */
	@Override
	public Measurement getMeasurement() {
		return measurement;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Question#setMeasurement(com.beyobe.client.beans.Measurement)
	 */
	@Override
	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
}
