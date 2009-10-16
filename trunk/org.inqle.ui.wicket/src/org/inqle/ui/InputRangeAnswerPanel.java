/**
 * 
 */
package org.inqle.ui;

import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class InputRangeAnswerPanel<T extends Number> extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public InputRangeAnswerPanel(String id, RangeAnswer<Number> rangeAnswer) {
		super(id);
	}

}
