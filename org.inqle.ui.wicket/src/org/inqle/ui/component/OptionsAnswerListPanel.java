/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

import com.antilia.web.beantable.Table;
import com.antilia.web.beantable.model.TableModel;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionsAnswerListPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private OptionsAnswer optionsAnswer;
	
	/**
	 * @param id
	 */
	public OptionsAnswerListPanel(String id, OptionsAnswer optionsAnswer) {
		super(id);
		this.optionsAnswer = optionsAnswer;
		
		Form<Option> form = new Form<Option>("form");
		add(form);
		TableModel<Option> tableModel = new TableModel<Option>(Option.class, "id", "translationKey");
		Table<Option> optionList = new Table<Option>("optionList", tableModel, optionsAnswer.getOptions());
		form.add(optionList);
	}

	public OptionsAnswer getOptionsAnswer() {
		return optionsAnswer;
	}
}
