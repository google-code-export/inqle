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
import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.provider.SelectionMode;

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
		Table<Option> optionList = new Table<Option>("optionList", tableModel, optionsAnswer.getOptions()) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateRowMenu(IMenuItemHolder menu, int row, Option bean) {
				OptionsAnswerListPanel.this.populateRowMenu(menu, row, bean);
			}
			
		};
		optionList.setColumnsResizable(false);
		optionList.setFirstColumnResizable(false);
		optionList.resetSelectionMode(SelectionMode.NONE);
		form.add(optionList);
	}

	/**
	 * 
	 * @param menu
	 * @param row
	 * @param bean
	 */
	protected void populateRowMenu(IMenuItemHolder menu, int row, Option bean) {
		
	}
	
	public OptionsAnswer getOptionsAnswer() {
		return optionsAnswer;
	}
}
