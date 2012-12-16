/**
 * 
 */
package org.inqle.ui.wicket;

import java.util.ArrayList;

import org.inqle.ui.component.view.question.QuestionsViewsPanel;
import org.inqle.ui.model.ListQuestionsSet;
import org.inqle.ui.model.Question;


/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class ViewPage extends BasePage {

	
	/**
	 * 
	 */
	public ViewPage() {
		super();
		ListQuestionsSet questions = new ListQuestionsSet(new ArrayList<Question>());
		QuestionsViewsPanel view = new QuestionsViewsPanel("view", questions);
		add(view);
	}
}
