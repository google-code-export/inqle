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
public abstract class RangeAnswerEditPanel<R extends RangeAnswer<T>, T extends Number> extends BaseEditingPanel<R> {

	private static final long serialVersionUID = 1L;

	private Class<T> formatClass;
	

	/**
	 * @param id
	 */
	public RangeAnswerEditPanel(String id, R rangeAnswer, Class<T> formatClass, IOutcomeHandler<R> handler) {
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
