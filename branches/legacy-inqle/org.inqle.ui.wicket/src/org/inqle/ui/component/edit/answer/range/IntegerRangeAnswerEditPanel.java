/**
 * 
 */
package org.inqle.ui.component.edit.answer.range;

import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.IntegerRangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class IntegerRangeAnswerEditPanel extends RangeAnswerEditPanel<IntegerRangeAnswer,Integer> {


	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param rangeAnswer
	 * @param formatClass
	 * @param handler
	 */
	public IntegerRangeAnswerEditPanel(String id,
			IntegerRangeAnswer rangeAnswer,
			IOutcomeHandler<IntegerRangeAnswer> handler) {
		super(id, rangeAnswer, Integer.class, handler);
	}

}
