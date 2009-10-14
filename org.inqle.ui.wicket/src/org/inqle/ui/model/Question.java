/**
 * 
 */
package org.inqle.ui.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a question.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class Question extends IdentifiableTranslatable {

	/**
	 *  Question allows one or several answers.
	 *  
	 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
	 *
	 */
	public static enum AnswersMode {
		// only a single answer for this question are allowed
		SINGLE_SELECTION,
		// multiple answers for this questions are allowed.
		MULTIPLE_SELECTION
	}
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * List of posible answers (options).
	 */
	private Set<Answer>  answers = new HashSet<Answer>();
	
	/**
	 * The selected answer(s).
	 */
	private Set<Answer> selectedAnswers = new HashSet<Answer>();

	/**
	 * Flag to determine whether this question allows one or several answers.
	 */
	public AnswersMode answersMode = AnswersMode.SINGLE_SELECTION;

	public Question() {
	}
	
	public Question addAnswer(Answer answer) {
		answers.add(answer);
		return this;
	}
	
	public Question removeAnswer(Answer answer) {
		answers.remove(answer);
		return this;
	}
	
	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}


	public Set<Answer> getSelectedAnswers() {
		return selectedAnswers;
	}


	public void setSelectedAnswers(Set<Answer> selectedAnswers) {
		if(answersMode.equals(AnswersMode.SINGLE_SELECTION) && selectedAnswers.size() > 0) {
			throw new IllegalArgumentException("Only one answer is allowed!");
		}
		this.selectedAnswers = selectedAnswers;
	}
	
	public Question addSelectedAnswer(Answer answer) {
		if(answersMode.equals(AnswersMode.SINGLE_SELECTION) && selectedAnswers.size() > 0) {
			throw new IllegalArgumentException("Only one answer is allowed!");
		}
		selectedAnswers.add(answer);
		return this;
	}
	
	public Question removeSelectedAnswer(Answer answer) {
		selectedAnswers.remove(answer);
		return this;
	}
}
