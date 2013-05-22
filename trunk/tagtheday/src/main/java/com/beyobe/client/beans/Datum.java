package com.beyobe.client.beans;

import java.util.Date;

public interface Datum {

	public static final Integer STATUS_ANSWERED_PERSONALLY = 2;
	public static final Integer STATUS_INFERRED = 1;
	public static final Integer STATUS_ASKED_BUT_NO_ANSWER = -1;
	public static final Integer STATUS_DECLINED_ANSWER = -2;
	public static final Integer STATUS_NEVER_ASK_AGAIN = -3;
	
	public abstract String getId();

	public abstract void setId(String uid);

	public abstract Date getCreated();

	public abstract void setCreated(Date created);

	public abstract Date getUpdated();

	public abstract void setUpdated(Date updated);

	public abstract String getQuestionUid();

	public abstract void setQuestionUid(String questionUid);

	public abstract Long getParticipantId();

	public abstract void setParticipantId(Long participantId);

	public abstract Double getNumericValue();

	public abstract void setNumericValue(Double numericValue);

	public abstract String getTextValue();

	public abstract void setTextValue(String textValue);

	public abstract Choice getChoice();

	public abstract void setChoice(Choice choice);

	public abstract Double getNormalizedValue();

	public abstract void setNormalizedValue(Double normalizedValue);

	public abstract Unit getUnit();

	public abstract void setUnit(Unit unit);

	public abstract Integer getStatus();

	public abstract void setStatus(Integer status);

	public abstract String getConceptUid();
	
	public abstract void setConceptUid(String conceptUid);

	public abstract Integer getIntegerValue();

	public abstract void setIntegerValue(Integer integerValue);

	public abstract Integer getDataType();

	public abstract void setDataType(Integer dataType);

//	public abstract String getLongTextValue();
//
//	public abstract void setLongTextValue(String longTextValue);

	public abstract Date getEffectiveDate();

	public abstract void setEffectiveDate(Date effectiveDate);

}