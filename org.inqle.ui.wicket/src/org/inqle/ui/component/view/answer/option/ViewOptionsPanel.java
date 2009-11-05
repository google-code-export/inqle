/**
 * 
 */
package org.inqle.ui.component.view.answer.option;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class ViewOptionsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public ViewOptionsPanel(String id) {
		super(id);
	}

	/**
	 * @param id
	 * @param model
	 */
	public ViewOptionsPanel(String id, IModel<?> model) {
		super(id, model);
	}

}
