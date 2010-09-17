package org.inqle.qa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AskableQuestion {

	private String entityKey;
	private String id;
	private String questionText;
	private String priority;
	private Double minInterval;
	private String hint;
	private String answerPrefix;
	private String answerSuffix;
	private String answerType;
	private String measureId;
	private String referenceUnitId;
	private List<Option> options;
	private Double answerMinimum;
	private Double answerMaximum;
	
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionText() {
		return questionText;
	}
	
	public String toString() {
		String s = "Question:[";
		s+= "\n  id=" + id;
		s += "\n  questionText=" + questionText;
		s += "\n  hint=" + hint;
		s += "\n  priority=" + priority;
		s += "\n  minInterval=" + minInterval;
		s += "\n  answerPrefix=" + answerPrefix;
		s += "\n  answerSuffix=" + answerSuffix;
		s += "\n  answerType=" + answerType;
		s += "\n  measureId=" + measureId;
		s += "\n  referenceUnitId=" + referenceUnitId;
		s += "\n  options=" + options;
		s += "\n  answerMinimum=" + answerMinimum;
		s += "\n  answerMaximum=" + answerMaximum;
		s += "]";
		return s;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Double getMinInterval() {
		return minInterval;
	}

	public void setMinInterval(Double minInterval) {
		this.minInterval = minInterval;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getAnswerPrefix() {
		return answerPrefix;
	}

	public void setAnswerPrefix(String answerPrefix) {
		this.answerPrefix = answerPrefix;
	}

	public String getAnswerSuffix() {
		return answerSuffix;
	}

	public void setAnswerSuffix(String answerSuffix) {
		this.answerSuffix = answerSuffix;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public Double getAnswerMinimum() {
		return answerMinimum;
	}

	public void setAnswerMinimum(Double answerMinimum) {
		this.answerMinimum = answerMinimum;
	}

	public Double getAnswerMaximum() {
		return answerMaximum;
	}

	public void setAnswerMaximum(Double answerMaximum) {
		this.answerMaximum = answerMaximum;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public void setReferenceUnitId(String referenceUnitId) {
		this.referenceUnitId = referenceUnitId;
	}

	public String getReferenceUnitId() {
		return referenceUnitId;
	}
	
}