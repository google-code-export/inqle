/**
 * 
 */
package org.inqle.ui.factory.impl;

import org.apache.wicket.Component;
import org.inqle.ui.component.edit.answer.range.DoubleRangeAnswerEditPanel;
import org.inqle.ui.component.view.answer.range.RangePanel;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.factory.IRenderableUIBuilder;
import org.inqle.ui.model.DoubleRangeAnswer;
import org.inqle.ui.model.IUIRenderable;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DoubleRangeAnswerBuilder implements IRenderableUIBuilder {

	/**
	 * 
	 */
	public DoubleRangeAnswerBuilder() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#canHandleAnswer(org.inqle.ui.model.IAnswer)
	 */
	@Override
	public boolean canHandleAnswer(IUIRenderable renderable) {
		return (renderable instanceof DoubleRangeAnswer);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createAdminUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminEditUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {		
		DoubleRangeAnswer rangeAnswer = (DoubleRangeAnswer)renderable;
		IOutcomeHandler<DoubleRangeAnswer> handler1 = (IOutcomeHandler<DoubleRangeAnswer>)handler;
		return new DoubleRangeAnswerEditPanel(id, rangeAnswer, handler1);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswerUIBuilder#createFinalUserUI(java.lang.String, org.inqle.ui.model.IAnswer)
	 */
	@Override
	public Component createFinalUserUI(String id, IUIRenderable renderable) {
		DoubleRangeAnswer rangeAnswer = (DoubleRangeAnswer)renderable;		
		return new RangePanel<DoubleRangeAnswer, Double>(id, rangeAnswer, Double.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component createAdminCreateUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {
		DoubleRangeAnswer rangeAnswer = (DoubleRangeAnswer)renderable;
		IOutcomeHandler<DoubleRangeAnswer> handler1 = (IOutcomeHandler<DoubleRangeAnswer>)handler;
		return new DoubleRangeAnswerEditPanel(id, rangeAnswer, handler1);
	}

}
