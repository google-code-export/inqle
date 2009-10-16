/**
 * 
 */
package org.inqle.ui;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.inqle.ui.model.IAnswer;
import org.inqle.ui.model.ITranslationService;
import org.inqle.ui.model.TextualAnswer;

import com.google.inject.Inject;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class TextualAnswerBuilder implements IAnswerUIBuilder {

	@Inject
	private ITranslationService translationService;
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
		return new TextField<String>(id, new PropertyModel<String>(((TextualAnswer)answer), "translationKey"));
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IAnswer answer) {
		// translation needed here.
		return new Label(id, translationService.translate((TextualAnswer)answer));
	}

}
