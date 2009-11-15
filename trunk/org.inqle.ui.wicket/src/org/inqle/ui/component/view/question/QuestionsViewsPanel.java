package org.inqle.ui.component.view.question;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.IQuestionsSet;
import org.inqle.ui.model.Question;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class QuestionsViewsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private IQuestionsSet questionsSet;
	
	/**
	 * @param id
	 */
	public QuestionsViewsPanel(String id, IQuestionsSet questionsSet) {
		super(id);
		this.questionsSet = questionsSet;
		Form<Question> form = new Form<Question>("form");
		add(form);
	}
}
