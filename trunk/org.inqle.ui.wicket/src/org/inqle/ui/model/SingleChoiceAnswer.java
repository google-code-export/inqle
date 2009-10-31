/**
 * 
 */
package org.inqle.ui.model;


/**
 * A single choice kind of answer!
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class SingleChoiceAnswer extends OptionsAnswer {

	private static final long serialVersionUID = 1L;

	private Option selected;
	
	/**
	 * 
	 */
	public SingleChoiceAnswer() {
	}

	public Option getSelected() {
		return selected;
	}

	public void setSelected(Option selected) {
		this.selected = selected;
	}

	
	
}
