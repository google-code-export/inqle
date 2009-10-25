/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.Component;
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
	public RangeAnswerEditPanel(String id, RangeAnswer<T> rangeAnswer, Class<T> formatClass) {
		super(id, rangeAnswer);	
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
