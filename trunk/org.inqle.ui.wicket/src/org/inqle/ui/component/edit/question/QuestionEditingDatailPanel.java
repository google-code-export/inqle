/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.IAnswer;
import org.inqle.ui.model.Question;

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
	public QuestionEditingDatailPanel(String id, final Question question) {
		super(id);
		
		translationKey = new TextField<String>("translationKey");		
		translationKey.setRequired(true);
		add(translationKey);
		
		SelectAnswerPanel answer = new SelectAnswerPanel("answer") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public IAnswer getAnswer() {
				return question.getAnswer();
			}
			
			public void setAnswer(IAnswer answer) {
				question.setAnswer(answer);
			};
		}
		;
		add(answer);
	}
}
