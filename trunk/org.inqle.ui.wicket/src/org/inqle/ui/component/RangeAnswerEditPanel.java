/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangeAnswerEditPanel<T extends Number> extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public RangeAnswerEditPanel(String id, RangeAnswer<Number> rangeAnswer) {
		super(id);
	}

}
