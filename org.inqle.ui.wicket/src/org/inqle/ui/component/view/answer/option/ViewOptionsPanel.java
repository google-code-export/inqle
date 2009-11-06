/**
 * 
 */
package org.inqle.ui.component.view.answer.option;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class ViewOptionsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private OptionsAnswer optionsAnswer;
	
	/**
	 * @param id
	 */
	public ViewOptionsPanel(String id, OptionsAnswer optionsAnswer) {
		super(id);
		this.optionsAnswer = optionsAnswer;
		
		RepeatingView repeatingView = new RepeatingView("option");
		for(Option option: optionsAnswer.getOptions()) {
			add(new Label(repeatingView.newChildId(), option.getTranslationKey()));
		}
		add(repeatingView);
	}

	public OptionsAnswer getOptionsAnswer() {
		return optionsAnswer;
	}

	public void setOptionsAnswer(OptionsAnswer optionsAnswer) {
		this.optionsAnswer = optionsAnswer;
	}

}
