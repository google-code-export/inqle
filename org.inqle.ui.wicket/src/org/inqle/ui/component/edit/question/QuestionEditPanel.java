/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.BaseEditingPanel;
import org.inqle.ui.model.Question;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class QuestionEditPanel extends BaseEditingPanel<Question> {

	private static final long serialVersionUID = 1L;	
	
	/**
	 * @param id
	 */
	public QuestionEditPanel(String id, Question question) {
		super(id, question);	
	}
	
	@Override
	protected Component createdContent(String contentId) {
		QuestionEditingDatailPanel optionDatailPanel = new QuestionEditingDatailPanel(contentId);
		return optionDatailPanel;
	}
}
