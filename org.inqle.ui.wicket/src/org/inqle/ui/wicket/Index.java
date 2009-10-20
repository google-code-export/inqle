/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.inqle.ui.component.RangeAnswerEditPanel;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class Index extends WebPage {

	private RangeAnswer<Float> rangeAnswer;
	/**
	 * 
	 */
	public Index() {
		rangeAnswer = new RangeAnswer<Float>();
		RangeAnswerEditPanel<Float> testEdit = new RangeAnswerEditPanel<Float>("testEdit", rangeAnswer, Float.class);
		add(testEdit);
	}
}
