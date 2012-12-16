/**
 * 
 */
package org.inqle.ui.component.edit.answer.range;

import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.DoubleRangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DoubleRangeAnswerEditPanel extends RangeAnswerEditPanel<DoubleRangeAnswer,Double> {


	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param rangeAnswer
	 * @param formatClass
	 * @param handler
	 */
	public DoubleRangeAnswerEditPanel(String id,
			DoubleRangeAnswer rangeAnswer,
			IOutcomeHandler<DoubleRangeAnswer> handler) {
		super(id, rangeAnswer, Double.class, handler);
	}

}
