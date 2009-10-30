/**
 * 
 */
package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.answer.range.RangeAnswerEditPanel;
import org.inqle.ui.model.IUIRenderable;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangeAnswerBuilder implements IRenderableUIBuilder {

	/**
	 * 
	 */
	public RangeAnswerBuilder() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#canHandleAnswer(org.inqle.ui.model.IAnswer)
	 */
	@Override
	public boolean canHandleAnswer(IUIRenderable renderable) {
		return (renderable instanceof RangeAnswer<?>);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createAdminUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminEditUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {		
		RangeAnswer<Float> rangeAnswer = (RangeAnswer<Float>)renderable;
		IOutcomeHandler<RangeAnswer<Float>> handler1 = (IOutcomeHandler<RangeAnswer<Float>>)handler;
		return new RangeAnswerEditPanel<Float>(id, rangeAnswer, Float.class, handler1);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IUIRenderable renderable) {
		// TODO: create a component that allows to select a value within limits.
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminCreateUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {
		RangeAnswer<Float> rangeAnswer = (RangeAnswer<Float>)renderable;
		IOutcomeHandler<RangeAnswer<Float>> handler1 = (IOutcomeHandler<RangeAnswer<Float>>)handler;
		return new RangeAnswerEditPanel<Float>(id, rangeAnswer, Float.class, handler1);
	}

}
