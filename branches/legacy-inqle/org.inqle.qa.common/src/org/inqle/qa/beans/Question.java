package org.inqle.qa.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.inqle.qa.common.QAConstants;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;
import org.inqle.rdf.beans.UniqueJenabean;

import thewebsemantic.Namespace;

@TargetDatabaseId(QAConstants.QA_DATABASE)
@TargetModelName(QAConstants.DEFAULT_QUESTION_MODEL)
@Namespace(RDF.INQLE)
public class Question extends UniqueJenabean{

	private static final long serialVersionUID = 4758619924199193098L;
	
	public static final String DEFAULT_QUESTION_DATABASE = "_Questions";
	public static String QUESTION_TYPE_SINGLE_SELECTION = "SINGLE SELECTION";
	public static String QUESTION_TYPE_MULTIPLE_SELECTION = "MULTIPLE SELECTION";
	
	private Collection<Translation> translations = new ArrayList<Translation>();
	private List<Option> questionOptions;
	private String questionType;
	private Double minimumResponse = new Double(0);
	private Double maximumResponse;
	private Double acceptedResponseInterval;
	
	public void setQuestionTranslations(Collection<Translation> questionTranslations) {
		this.translations = questionTranslations;
	}
	
	public Collection<Translation> getQuestionTranslations() {
		return translations;
	}
	
	public void addQuestionTranslation(Translation questionTranslation) {
		translations.add(questionTranslation);
	}
	
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	public String getQuestionType() {
		if (questionType==null) {
			return QUESTION_TYPE_SINGLE_SELECTION;
		}
		return questionType;
	}
	
	public void setQuestionOptions(List<Option> questionOptions) {
		this.questionOptions = questionOptions;
	}
	
	public List<Option> getQuestionOptions() {
		return questionOptions;
	}
	
	public Translation getQuestionTranslation(String lang) {
		for (Translation translation: getQuestionTranslations()) {
			if (translation.getLang().equals(lang)) return translation;
		}
		return null;
	}
	
	public Translation getDefaultQuestionTranslation() {
		return getQuestionTranslation(QAInfo.DEFAULT_LANG);
	}

	public void setMinimumResponse(Double minimumResponse) {
		this.minimumResponse = minimumResponse;
	}

	public Double getMinimumResponse() {
		return minimumResponse;
	}

	public void setMaximumResponse(Double maximumResponse) {
		this.maximumResponse = maximumResponse;
	}

	public Double getMaximumResponse() {
		return maximumResponse;
	}

	public void setAcceptedResponseInterval(Double acceptedResponseInterval) {
		this.acceptedResponseInterval = acceptedResponseInterval;
	}

	public Double getAcceptedResponseInterval() {
		return acceptedResponseInterval;
	}
}
