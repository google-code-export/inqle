/**
 * 
 */
package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.answer.range.IntegerRangeAnswerEditPanel;
import org.inqle.ui.model.IUIRenderable;
import org.inqle.ui.model.IntegerRangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class IntegerRangeAnswerBuilder implements IRenderableUIBuilder {

	/**
	 * 
	 */
	public IntegerRangeAnswerBuilder() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#canHandleAnswer(org.inqle.ui.model.IAnswer)
	 */
	@Override
	public boolean canHandleAnswer(IUIRenderable renderable) {
		return (renderable instanceof IntegerRangeAnswer);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createAdminUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminEditUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {		
		IntegerRangeAnswer rangeAnswer = (IntegerRangeAnswer)renderable;
		IOutcomeHandler<IntegerRangeAnswer> handler1 = (IOutcomeHandler<IntegerRangeAnswer>)handler;
		return new IntegerRangeAnswerEditPanel(id, rangeAnswer, handler1);
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
		IntegerRangeAnswer rangeAnswer = (IntegerRangeAnswer)renderable;
		IOutcomeHandler<IntegerRangeAnswer> handler1 = (IOutcomeHandler<IntegerRangeAnswer>)handler;
		return new IntegerRangeAnswerEditPanel(id, rangeAnswer, handler1);
	}

}
