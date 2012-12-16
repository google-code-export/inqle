/**
 * 
 */
package org.inqle.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A multiple choice kind of answer!
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class MultipleChoiceAnswer extends OptionsAnswer {

	private static final long serialVersionUID = 1L;

	private List<Option> selectedOptions = new ArrayList<Option>();
	
	/**
	 * 
	 */
	public MultipleChoiceAnswer() {
	}

	public MultipleChoiceAnswer addSelectedOption(Option option) {
		selectedOptions.add(option);		
		return this;
	}
	
	public MultipleChoiceAnswer removeSelectedOption(Option option) {
		selectedOptions.remove(option);		
		return this;
	}

	public List<Option> getSelectedOptions() {
		return selectedOptions;
	}
}
