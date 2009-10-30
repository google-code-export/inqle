/**
 * 
 */
package org.inqle.ui.component.edit.answer.range;

import org.apache.wicket.Component;
import org.inqle.ui.component.MinimumMaximumValidator;
import org.inqle.ui.component.edit.BaseEditingPanel;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangeAnswerEditPanel<T extends Number> extends BaseEditingPanel<RangeAnswer<T>> {

	private static final long serialVersionUID = 1L;

	private Class<T> formatClass;
	

	/**
	 * @param id
	 */
	public RangeAnswerEditPanel(String id, RangeAnswer<T> rangeAnswer, Class<T> formatClass, IOutcomeHandler<RangeAnswer<T>> handler) {
		super(id, rangeAnswer, handler);	
		this.formatClass = formatClass;
	}
	
	@Override
	protected Component createdContent(String contentId) {
		RangeAnswerEditDetailPanel<T> rangeAnswerEditDetailPanel = new RangeAnswerEditDetailPanel<T>(contentId, this.formatClass);
		// validating that minimum is not bigger that maximum.
		getForm().add(new MinimumMaximumValidator<T>(
				rangeAnswerEditDetailPanel.getMinimumResponse(), 
				rangeAnswerEditDetailPanel.getMaximumResponse()));
		return rangeAnswerEditDetailPanel;
	}
}
