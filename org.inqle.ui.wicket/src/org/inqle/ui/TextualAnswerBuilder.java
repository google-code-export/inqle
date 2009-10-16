/**
 * 
 */
package org.inqle.ui;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.inqle.ui.model.IAnswer;
import org.inqle.ui.model.TextualAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class TextualAnswerBuilder implements IAnswerUIBuilder {

	/**
	 * 
	 */
	public TextualAnswerBuilder() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#canHandleAnswer(org.inqle.ui.model.IAnswer)
	 */
	@Override
	public boolean canHandleAnswer(IAnswer answer) {
		return (answer instanceof TextualAnswer);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createAdminUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createAdminUserUI(String id, IAnswer answer) {
		return new Label(id, ((TextualAnswer)answer).getTranslationKey());
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IAnswer answer) {
		return new Label(id, ((TextualAnswer)answer).getTranslationKey());
	}

}
