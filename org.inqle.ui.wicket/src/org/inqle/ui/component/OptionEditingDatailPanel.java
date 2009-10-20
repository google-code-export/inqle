/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionEditingDatailPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private TextField<String> translationKey;
	/**
	 * @param id
	 */
	public OptionEditingDatailPanel(String id) {
		super(id);
		
		translationKey = new TextField<String>("translationKey");		
		translationKey.setRequired(true);
		add(translationKey);
	}
}
