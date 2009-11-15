package org.inqle.ui.model;

import java.util.List;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class ListQuestionsSet implements IQuestionsSet {

	private static final long serialVersionUID = 1L;
	
	private List<Question> questions;
	
	private int current = -1;
	
	public ListQuestionsSet(List<Question> questions) {
		this.questions= questions;
	}

	@Override
	public boolean hasNext() {
		if(current < (questions.size()-1))
			return  true;
		return false;
	}
	
	@Override
	public boolean hasPrevious() {
		return current > 0;
	}
	
	@Override
	public Question next() {
		return this.questions.get(current++);
	}
	
	@Override
	public Question previous() {
		return this.questions.get(current--);
	}
}
