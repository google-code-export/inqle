/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.Component;
import org.inqle.ui.model.Option;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionEditPanel extends AnswerEditingPanel<Option> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public OptionEditPanel(String id, Option option) {
		super(id, option);	
	}
	
	@Override
	protected Component createdContent(String contentId) {
		OptionEditingDatailPanel optionDatailPanel = new OptionEditingDatailPanel(contentId);
		return optionDatailPanel;
	}

}
