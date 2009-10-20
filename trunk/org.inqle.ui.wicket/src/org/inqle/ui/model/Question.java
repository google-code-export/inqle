/**
 * 
 */
package org.inqle.ui.model;


/**
 * Represents a question.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class Question extends IdentifiableTranslatable {

	private static final long serialVersionUID = 1L;
	
	
	private IAnswer answer;

	public Question() {
		
	}
	
	public Question(IAnswer answer) {
		super();
		this.answer = answer;
	}
	


	public IAnswer getAnswer() {
		return answer;
	}


	public void setAnswer(IAnswer answer) {
		this.answer = answer;
	}
}
