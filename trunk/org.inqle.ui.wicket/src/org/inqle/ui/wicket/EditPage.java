/**
 * 
 */
package org.inqle.ui.wicket;

import org.inqle.ui.component.edit.question.QuestionsEditPanel;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class EditPage extends BasePage {

	
	/**
	 * 
	 */
	public EditPage() {
		super();
		QuestionsEditPanel questions = new QuestionsEditPanel("questions");
		add(questions);
	}
}
