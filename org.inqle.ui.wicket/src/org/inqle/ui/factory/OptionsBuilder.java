/**
 * 
 */
package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.inqle.ui.model.ITranslationService;
import org.inqle.ui.model.IUIRenderable;
import org.inqle.ui.model.Option;

import com.google.inject.Inject;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionsBuilder implements IUIRenderableBuilder {

	@Inject
	private ITranslationService translationService;
	/**
	 * 
	 */
	public OptionsBuilder() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#canHandleAnswer(org.inqle.ui.model.IAnswer)
	 */
	@Override
	public boolean canHandleAnswer(IUIRenderable renderable) {
		return (renderable instanceof Option);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createAdminUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createAdminUserUI(String id, IUIRenderable renderable) {		
		return new TextField<String>(id, new PropertyModel<String>(((Option)renderable), "translationKey"));
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IUIRenderable renderable) {
		// translation needed here.
		return new Label(id, translationService.translate((Option)renderable));
	}

}