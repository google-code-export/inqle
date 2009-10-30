/**
 * 
 */
package org.inqle.ui.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.inqle.ui.model.IUIRenderable;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DafaultIUIRenderableBuilderService implements IUIRenderableBuilderService {

	private List<IRenderableUIBuilder> builders = new ArrayList<IRenderableUIBuilder>();
	
	
	public DafaultIUIRenderableBuilderService() {
		addBuilder(new OptionsBuilder());
		addBuilder(new RangeAnswerBuilder());
		addBuilder(new MultipleChoiceBuilder(this));
		addBuilder(new SingleChoiceBuilder(this));
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswersBuilderService#createAdminUserUI(org.inqle.ui.model.IAnswer)
	 */
	public Component createAdminEditUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {
		for(IRenderableUIBuilder builder: builders) {
			if(builder.canHandleAnswer(renderable))
				builder.createAdminEditUI(id,renderable, handler);
		}
		return new Label(id, "Cannot created UI for type of answer: " + renderable.getClass().getName());
	}

	@Override
	public Component createAdminCreateUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler) {
		for(IRenderableUIBuilder builder: builders) {
			if(builder.canHandleAnswer(renderable))
				builder.createAdminCreateUI(id, renderable, handler);
		}
		return new Label(id, "Cannot created UI for type of answer: " + renderable.getClass().getName());
	}
	
	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswersBuilderService#createFinalUserUI(org.inqle.ui.model.IAnswer)
	 */
	public Component createFinalUserUI(String id,IUIRenderable renderable) {
		for(IRenderableUIBuilder builder: builders) {
			if(builder.canHandleAnswer(renderable))
				builder.createFinalUserUI(id,renderable);
		}
		return new Label(id, "Cannot created UI for type of answer: " + renderable.getClass().getName());
	}
	
	public IUIRenderableBuilderService addBuilder(IRenderableUIBuilder builder) {
		builders.add(builder);
		return this;
	}
	
	public IUIRenderableBuilderService removeBuilder(IRenderableUIBuilder builder) {
		builders.remove(builder);
		return this;
	}

}
