package org.inqle.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Answer that represents several options
 *  
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public abstract class OptionsAnswer implements IAnswer {

	private static final long serialVersionUID = 1L;

	private List<Option> options = new ArrayList<Option>();
		
	/**
	 * Constructor.
	 */
	public OptionsAnswer() {
	}
	
	public OptionsAnswer addOption(Option option) {
		options.add(option);
		return this;
	}
	
	public OptionsAnswer removeOption(Option option) {
		options.remove(option);
		return this;
	}

	public List<Option> getOptions() {
		return options;
	}
}
