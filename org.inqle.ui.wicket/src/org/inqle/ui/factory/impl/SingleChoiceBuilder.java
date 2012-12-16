/**
 * 
 */
package org.inqle.ui.factory.impl;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.answer.option.OptionsAnswerEditPanel;
import org.inqle.ui.component.view.answer.option.SingleChoiceOptionsPanel;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.factory.IRenderableUIBuilder;
import org.inqle.ui.factory.IUIRenderableBuilderService;
import org.inqle.ui.model.IUIRenderable;
import org.inqle.ui.model.OptionsAnswer;
import org.inqle.ui.model.SingleChoiceAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class SingleChoiceBuilder implements IRenderableUIBuilder {
	
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
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminEditUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {		
		SingleChoiceAnswer singleChoiceAnswer = (SingleChoiceAnswer)renderable;		
		OptionsAnswerEditPanel answerListPanel = new OptionsAnswerEditPanel(id, singleChoiceAnswer, (IOutcomeHandler<OptionsAnswer>)handler); 
		return answerListPanel;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IUIRenderable renderable) {
		SingleChoiceOptionsPanel optionsPanel = new SingleChoiceOptionsPanel(id, (SingleChoiceAnswer)renderable);
		return optionsPanel;
	}

	public IUIRenderableBuilderService getRenderableBuilderService() {
		return renderableBuilderService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminCreateUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {
		SingleChoiceAnswer singleChoiceAnswer = (SingleChoiceAnswer)renderable;		
		OptionsAnswerEditPanel answerListPanel = new OptionsAnswerEditPanel(id, singleChoiceAnswer, (IOutcomeHandler<OptionsAnswer>)handler); 
		return answerListPanel;
	}
}
