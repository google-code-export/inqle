package com.beyobe.client.beans;

import java.util.Date;

public class DatumImpl implements Datum {

	private long id;
    private Date created = new Date();
    private Date updated = null;
    private Date effectiveDate;
    private String questionUid;
    private long participantId;
    private Double numericValue;
    private Integer integerValue;
    private String textValue;
    private String longTextValue;
    private Choice choice;
    private Double normalizedValue;
    private Unit unit;
    private Integer status;
    private long conceptId;
	private Integer dataType;
	
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getCreated()
	 */
	@Override
	public Date getCreated() {
		return created;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setCreated(java.util.Date)
	 */
	@Override
	public void setCreated(Date created) {
		this.created = created;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getUpdated()
	 */
	@Override
	public Date getUpdated() {
		return updated;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setUpdated(java.util.Date)
	 */
	@Override
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getQuestionUid()
	 */
	@Override
	public String getQuestionUid() {
		return questionUid;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setQuestionUid(java.lang.String)
	 */
	@Override
	public void setQuestionUid(String questionUid) {
		this.questionUid = questionUid;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getParticipantId()
	 */
	@Override
	public long getParticipantId() {
		return participantId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setParticipantId(long)
	 */
	@Override
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getNumericValue()
	 */
	@Override
	public Double getNumericValue() {
		return numericValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setNumericValue(java.lang.Double)
	 */
	@Override
	public void setNumericValue(Double numericValue) {
		this.numericValue = numericValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getTextValue()
	 */
	@Override
	public String getTextValue() {
		return textValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setTextValue(java.lang.String)
	 */
	@Override
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getChoice()
	 */
	@Override
	public Choice getChoice() {
		return choice;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setChoice(com.beyobe.client.beans.Choice)
	 */
	@Override
	public void setChoice(Choice choice) {
		this.choice = choice;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getNormalizedValue()
	 */
	@Override
	public Double getNormalizedValue() {
		return normalizedValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setNormalizedValue(java.lang.Double)
	 */
	@Override
	public void setNormalizedValue(Double normalizedValue) {
		this.normalizedValue = normalizedValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getUnit()
	 */
	@Override
	public Unit getUnit() {
		return unit;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setUnit(com.beyobe.client.beans.Unit)
	 */
	@Override
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getStatus()
	 */
	@Override
	public Integer getStatus() {
		return status;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setStatus(java.lang.Integer)
	 */
	@Override
	public void setStatus(Integer status) {
		this.status = status;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getConceptId()
	 */
	@Override
	public long getConceptId() {
		return conceptId;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setConceptId(long)
	 */
	@Override
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
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getIntegerValue()
	 */
	@Override
	public Integer getIntegerValue() {
		return integerValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setIntegerValue(java.lang.Integer)
	 */
	@Override
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getDataType()
	 */
	@Override
	public Integer getDataType() {
		return dataType;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setDataType(java.lang.Integer)
	 */
	@Override
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getLongTextValue()
	 */
	@Override
	public String getLongTextValue() {
		return longTextValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setLongTextValue(java.lang.String)
	 */
	@Override
	public void setLongTextValue(String longTextValue) {
		this.longTextValue = longTextValue;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#getEffectiveDate()
	 */
	@Override
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Datum#setEffectiveDate(java.util.Date)
	 */
	@Override
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
