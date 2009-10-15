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
	
	@Override
	public void setQuestionTranslations(Collection<ITranslation> questionTranslations) {
		this.translations = questionTranslations;
	}
	
	@Override
	public Collection<ITranslation> getQuestionTranslations() {
		return translations;
	}
	
	@Override
	public void addQuestionTranslation(ITranslation questionTranslation) {
		translations.add(questionTranslation);
	}
	
	@Override
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	@Override
	public String getQuestionType() {
		if (questionType==null) {
			return QUESTION_TYPE_SINGLE_SELECTION;
		}
		return questionType;
	}
	
	@Override
	public void setQuestionOptions(List<IOption> questionOptions) {
		this.questionOptions = questionOptions;
	}
	
	@Override
	public List<IOption> getQuestionOptions() {
		return questionOptions;
	}
	
	@Override
	public ITranslation getQuestionTranslation(String lang) {
		for (ITranslation translation: getQuestionTranslations()) {
			if (translation.getLang().equals(lang)) return translation;
		}
		return null;
	}
	
	@Override
	public ITranslation getDefaultQuestionTranslation() {
		return getQuestionTranslation(QAInfo.DEFAULT_LANG);
	}

	@Override
	public void setMinimumResponse(Double minimumResponse) {
		this.minimumResponse = minimumResponse;
	}

	@Override
	public Double getMinimumResponse() {
		return minimumResponse;
	}

	@Override
	public void setMaximumResponse(Double maximumResponse) {
		this.maximumResponse = maximumResponse;
	}

	@Override
	public Double getMaximumResponse() {
		return maximumResponse;
	}

	@Override
	public void setAcceptedResponseInterval(Double acceptedResponseInterval) {
		this.acceptedResponseInterval = acceptedResponseInterval;
	}

	@Override
	public Double getAcceptedResponseInterval() {
		return acceptedResponseInterval;
	}
	
}
