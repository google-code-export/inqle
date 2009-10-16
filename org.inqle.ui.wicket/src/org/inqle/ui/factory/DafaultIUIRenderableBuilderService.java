/**
 * 
 */
package org.inqle.ui.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.inqle.ui.model.IAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DafaultIUIRenderableBuilderService implements IUIRenderableBuilderService {

	private List<IUIRenderableBuilder> builders = new ArrayList<IUIRenderableBuilder>();
	
	
	public DafaultIUIRenderableBuilderService() {
		addBuilder(new OptionsBuilder());
		addBuilder(new RangeAnswerBuilder());
		addBuilder(new MultipleChoiceBuilder(this));
		addBuilder(new SingleChoiceBuilder(this));
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswersBuilderService#createAdminUserUI(org.inqle.ui.model.IAnswer)
	 */
	public Component createAdminUserUI(String id,IAnswer answer) {
		for(IUIRenderableBuilder builder: builders) {
			if(builder.canHandleAnswer(answer))
				builder.createAdminUserUI(id,answer);
		}
		return new Label(id, "Cannot created UI for type of answer: " + answer.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswersBuilderService#createFinalUserUI(org.inqle.ui.model.IAnswer)
	 */
	public Component createFinalUserUI(String id,IAnswer answer) {
		for(IUIRenderableBuilder builder: builders) {
			if(builder.canHandleAnswer(answer))
				builder.createFinalUserUI(id,answer);
		}
		return new Label(id, "Cannot created UI for type of answer: " + answer.getClass().getName());
	}
	
	public IUIRenderableBuilderService addBuilder(IUIRenderableBuilder builder) {
		builders.add(builder);
		return this;
	}
	
	public IUIRenderableBuilderService removeBuilder(IUIRenderableBuilder builder) {
		builders.remove(builder);
		return this;
	}

}
