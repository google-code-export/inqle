/**
 * 
 */
package org.inqle.ui.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String enuntiation;
	
	private Set<Answer>  answers = new HashSet<Answer>();
	
	private Answer selectedAnswer;
		
	public Answer getSelectedAnswer() {
		return selectedAnswer;
	}


	public void setSelectedAnswer(Answer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}


	public Question() {
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnuntiation() {
		return enuntiation;
	}

	public void setEnuntiation(String ennuntiate) {
		this.enuntiation = ennuntiate;
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
	
}
