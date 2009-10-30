/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.inqle.ui.component.edit.question.QuestionsEditPanel;
import org.inqle.ui.resources.Styles;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class Index extends WebPage {

	
	/**
	 * 
	 */
	public Index() {
		add(CSSPackageResource.getHeaderContribution(Styles.MAIN_CSS));	
		QuestionsEditPanel questions = new QuestionsEditPanel("questions");
		add(questions);
	}
}
