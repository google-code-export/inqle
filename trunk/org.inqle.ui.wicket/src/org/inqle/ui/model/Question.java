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
	private Set<IAnswer>  answers = new HashSet<IAnswer>();
	
	/**
	 * The selected answer(s).
	 */
	private Set<IAnswer> selectedAnswers = new HashSet<IAnswer>();

	/**
	 * Flag to determine whether this question allows one or several answers.
	 */
	public AnswersMode answersMode = AnswersMode.SINGLE_SELECTION;

	public Question() {
	}
	
	public Question addAnswer(IAnswer answer) {
		answers.add(answer);
		return this;
	}
	
	public Question removeAnswer(IAnswer answer) {
		answers.remove(answer);
		return this;
	}
	
	public Set<IAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<IAnswer> answers) {
		this.answers = answers;
	}


	public Set<IAnswer> getSelectedAnswers() {
		return selectedAnswers;
	}


	public void setSelectedAnswers(Set<IAnswer> selectedAnswers) {
		if(answersMode.equals(AnswersMode.SINGLE_SELECTION) && selectedAnswers.size() > 0) {
			throw new IllegalArgumentException("Only one answer is allowed!");
		}
		this.selectedAnswers = selectedAnswers;
	}
	
	public Question addSelectedAnswer(IAnswer answer) {
		if(answersMode.equals(AnswersMode.SINGLE_SELECTION) && selectedAnswers.size() > 0) {
			throw new IllegalArgumentException("Only one answer is allowed!");
		}
		selectedAnswers.add(answer);
		return this;
	}
	
	public Question removeSelectedAnswer(IAnswer answer) {
		selectedAnswers.remove(answer);
		return this;
	}
}
