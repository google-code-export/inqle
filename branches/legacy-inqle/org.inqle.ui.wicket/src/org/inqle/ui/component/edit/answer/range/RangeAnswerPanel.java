/**
 * 
 */
package org.inqle.ui.component.edit.answer.range;

import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangeAnswerPanel<T extends Number> extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public RangeAnswerPanel(String id, RangeAnswer<Number> rangeAnswer) {
		super(id);
	}

}
