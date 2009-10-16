/**
 * 
 */
package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.inqle.ui.model.IUIRenderable;
import org.inqle.ui.model.SingleChoiceAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class SingleChoiceBuilder implements IUIRenderableBuilder {
	
	/**
	 * Needed to render options!
	 */
	private IUIRenderableBuilderService renderableBuilderService;
	
	/**
	 * 
	 */
	public SingleChoiceBuilder(IUIRenderableBuilderService renderableBuilderService) {
		this.renderableBuilderService = renderableBuilderService;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#canHandleAnswer(org.inqle.ui.model.IAnswer)
	 */
	@Override
	public boolean canHandleAnswer(IUIRenderable renderable) {
		return (renderable instanceof SingleChoiceAnswer);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createAdminUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createAdminUserUI(String id, IUIRenderable renderable) {		
		// TODO: create a component that allows to create edit single choices.
		// this is essentially the same as multiple choices!
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IUIRenderable renderable) {
		// TODO: create a component that allows to select only one choice.
		// this is radically different from multiple choices!
		return null;
	}

	public IUIRenderableBuilderService getRenderableBuilderService() {
		return renderableBuilderService;
	}

}
