/**
 * 
 */
package org.inqle.ui.component.edit.answer.option;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.BaseEditingPanel;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.Option;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionEditPanel extends BaseEditingPanel<Option> {

	private static final long serialVersionUID = 1L;	
	
	/**
	 * @param id
	 */
	public OptionEditPanel(String id, Option option, IOutcomeHandler<Option> handler) {
		super(id, option, handler);	
	}
	
	@Override
	protected Component createdContent(String contentId) {
		OptionEditingDatailPanel optionDatailPanel = new OptionEditingDatailPanel(contentId);
		return optionDatailPanel;
	}
}