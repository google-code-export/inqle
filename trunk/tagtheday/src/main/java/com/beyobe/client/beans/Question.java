package com.beyobe.client.beans;

import java.util.Date;
import java.util.List;

public interface Question {

	public static final int DATA_TYPE_UNSPECIFIED = 0;
	public static final int DATA_TYPE_DOUBLE = 1;
	public static final int DATA_TYPE_INTEGER = 2;
	public static final int DATA_TYPE_MULTIPLE_CHOICE = 3;
	public static final int DATA_TYPE_SHORT_TEXT = 4;
	public static final int DATA_TYPE_LONG_TEXT = 5;
	public static final int DATA_TYPE_STARS = 6;

	public abstract Double getMinValue();

	public abstract void setMinValue(Double minValue);

	public abstract Double getMaxValue();

	public abstract void setMaxValue(Double maxValue);

	public abstract Integer getMaxLength();
	
	public abstract void setMaxLength(Integer maxLength);
	
	public abstract Integer getPriority();
	
	public abstract void setPriority(Integer priority);
	
	public Long getLatency();
	
	public void setLatency(Long latency);

	public abstract String getLongForm();

	public abstract void setLongForm(String longForm);

	//	public String getShortForm() {
	//		return shortForm;
	//	}
	//	public void setShortForm(String shortForm) {
	//		this.shortForm = shortForm;
	//	}
	public abstract String getAbbreviation();

	public abstract void setAbbreviation(String abbreviation);

	public abstract Date getCreated();

	public abstract void setCreated(Date created);

	public abstract Date getUpdated();

	public abstract void setUpdated(Date updated);

	public abstract Long getCreatedBy();

	public abstract void setCreatedBy(Long creatorId);
	
	public abstract Long getUpdatedBy();

	public abstract void setUpdatedBy(Long updaterId);

//	public abstract String getCreatorName();
//
//	public abstract void setCreatorName(String creatorName);

	public abstract String getLang();

	public abstract void setLang(String lang);

	public abstract Long getId();

	public abstract void setId(Long id);
	
	public abstract String getConceptUid();
	
	public abstract void setConceptUid(String conceptUid);

	public abstract Integer getDataType();

	public abstract void setDataType(Integer answerType);

	//	public Unit getReferenceUnit() {
	//		return referenceUnit;
	//	}
	//	public void setReferenceUnit(Unit referenceUnit) {
	//		this.referenceUnit = referenceUnit;
	//	}
	public abstract List<Choice> getChoices();

	public abstract void setChoices(List<Choice> choices);

	public abstract String getUid();

	public abstract void setUid(String uid);

	public abstract Measurement getMeasurement();

	public abstract void setMeasurement(Measurement measurement);

}