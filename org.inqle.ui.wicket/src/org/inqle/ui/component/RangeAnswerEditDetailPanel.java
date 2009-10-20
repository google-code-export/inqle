	/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangeAnswerEditDetailPanel<T extends Number> extends Panel {

	private static final long serialVersionUID = 1L;

	private TextField<T> minimumResponse;
	
	private TextField<T> maximumResponse;
	
	/**
	 * @param id
	 */
	public RangeAnswerEditDetailPanel(String id, final Class<T> formatClass) {
		super(id);		
					
		minimumResponse = new TextField<T>("minimumResponse", formatClass);		
		minimumResponse.setRequired(true);
		add(minimumResponse);
		
		maximumResponse = new TextField<T>("maximumResponse", formatClass);		
		maximumResponse.setRequired(true);
		add(maximumResponse);		
	}

	public TextField<T> getMinimumResponse() {
		return minimumResponse;
	}

	public void setMinimumResponse(TextField<T> minimumResponse) {
		this.minimumResponse = minimumResponse;
	}

	public TextField<T> getMaximumResponse() {
		return maximumResponse;
	}

	public void setMaximumResponse(TextField<T> maximumResponse) {
		this.maximumResponse = maximumResponse;
	}

}
