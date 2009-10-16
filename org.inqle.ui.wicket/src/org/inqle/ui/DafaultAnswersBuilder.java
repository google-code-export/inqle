/**
 * 
 */
package org.inqle.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.inqle.ui.model.IAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DafaultAnswersBuilder implements IAnswersBuilderService {

	private List<IAnswerUIBuilder> builders = new ArrayList<IAnswerUIBuilder>();
	
	
	public DafaultAnswersBuilder() {
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswersBuilderService#createAdminUserUI(org.inqle.ui.model.IAnswer)
	 */
	public Component createAdminUserUI(String id,IAnswer answer) {
		for(IAnswerUIBuilder builder: builders) {
			if(builder.canHandleAnswer(answer))
				builder.createAdminUserUI(id,answer);
		}
		return new Label(id, "Cannot created UI for type of answer: " + answer.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.IAnswersBuilderService#createFinalUserUI(org.inqle.ui.model.IAnswer)
	 */
	public Component createFinalUserUI(String id,IAnswer answer) {
		for(IAnswerUIBuilder builder: builders) {
			if(builder.canHandleAnswer(answer))
				builder.createFinalUserUI(id,answer);
		}
		return new Label(id, "Cannot created UI for type of answer: " + answer.getClass().getName());
	}
	
	public IAnswersBuilderService addBuilder(IAnswerUIBuilder builder) {
		builders.add(builder);
		return this;
	}
	
	public IAnswersBuilderService removeBuilder(IAnswerUIBuilder builder) {
		builders.remove(builder);
		return this;
	}

}
