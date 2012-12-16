/**
 * 
 */
package org.inqle.ui.component.edit.answer.option;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.BaseEditingPanel;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.OptionsAnswer;
/**
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class OptionsAnswerEditPanel extends BaseEditingPanel<OptionsAnswer> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public OptionsAnswerEditPanel(String id, OptionsAnswer bean, IOutcomeHandler<OptionsAnswer> handler) {
		super(id, bean, handler);
	}

	@Override
	protected Component createdContent(String contentId) {
		return new OptionsAnswerEditDetailPanel(contentId, getBean());
	}
}
