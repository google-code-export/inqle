package org.inqle.qa.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.inqle.core.data.ModelObject;

/**
 * This class represents a question.
 * 
 * @author David Donohue
 * September 10, 2009
 *
 */
public class Question extends ModelObject implements IQuestion, Serializable {
	
	
	
	public static String QUESTION_TYPE_SINGLE_SELECTION = "SINGLE SELECTION";
	public static String QUESTION_TYPE_MULTIPLE_SELECTION = "MULTIPLE SELECTION";
	
	private Collection<ITranslation> translations = new ArrayList<ITranslation>();
	private List<IOption> questionOptions;
	private String questionType;
	private Double minimumResponse = new Double(0);
	private Double maximumResponse;
	private Double acceptedResponseInterval;
	/**
	 * Friendly constructor.  Use QAFactory.newQuestion();
	 */
	Question(){};
	
	public void setQuestionTranslations(Collection<ITranslation> questionTranslations) {
		this.translations = questionTranslations;
	}
	
	public Collection<ITranslation> getQuestionTranslations() {
		return translations;
	}
	
	public void addQuestionTranslation(ITranslation questionTranslation) {
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
	
	public void setQuestionOptions(List<IOption> questionOptions) {
		this.questionOptions = questionOptions;
	}
	
	public List<IOption> getQuestionOptions() {
		return questionOptions;
	}
	
	public ITranslation getQuestionTranslation(String lang) {
		for (ITranslation translation: getQuestionTranslations()) {
			if (translation.getLang().equals(lang)) return translation;
		}
		return null;
	}
	
	public ITranslation getDefaultQuestionTranslation() {
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