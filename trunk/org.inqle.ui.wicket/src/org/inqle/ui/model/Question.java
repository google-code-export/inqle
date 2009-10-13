/**
 * 
 */
package org.inqle.ui.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class Question extends BaseTranslatable {

	public static enum AnswersMode {
		// only a single answer for this question are allowed
		SINGLE_SELECTION,
		// multiple answers for this questions are allowed.
		MULTIPLE_SELECTION
	}
	
	private static final long serialVersionUID = 1L;

	private Long id;
		
		
	private Set<Answer>  answers = new HashSet<Answer>();
	
	private Set<Answer> selectedAnswers = new HashSet<Answer>();

	public AnswersMode answersMode = AnswersMode.SINGLE_SELECTION;

	public Question() {
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
