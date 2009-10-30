/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class QuestionEditingDatailPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private TextField<String> translationKey;
	/**
	 * @param id
	 */
	public QuestionEditingDatailPanel(String id) {
		super(id);
		
		translationKey = new TextField<String>("translationKey");		
		translationKey.setRequired(true);
		add(translationKey);
		
		Label answer = new Label("answer", "selected answer");
		add(answer);
	}
}
