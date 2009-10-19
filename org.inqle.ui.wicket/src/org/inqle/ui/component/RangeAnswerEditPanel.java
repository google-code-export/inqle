/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.TextField;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangeAnswerEditPanel<T extends Number> extends AnswerEditingPanel<RangeAnswer<T>> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public RangeAnswerEditPanel(String id, RangeAnswer<T> rangeAnswer) {
		super(id, rangeAnswer);		
					
		TextField<T> minimumResponse = new TextField<T>("minimumResponse");		
		minimumResponse.setRequired(true);
		add(minimumResponse);
		
		TextField<T> maximumResponse = new TextField<T>("maximumResponse");		
		minimumResponse.setRequired(true);
		add(maximumResponse);		
	}

}
