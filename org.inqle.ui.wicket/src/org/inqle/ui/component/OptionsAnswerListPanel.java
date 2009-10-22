/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

import com.antilia.web.beantable.Table;
import com.antilia.web.beantable.model.IColumnModel;
import com.antilia.web.beantable.model.TableModel;
import com.antilia.web.beantable.navigation.DropColumnItem;
import com.antilia.web.beantable.navigation.PageSizeButton;
import com.antilia.web.beantable.navigation.RefreshButton;
import com.antilia.web.beantable.navigation.UnusedColumnsButton;
import com.antilia.web.button.IMenuItem;
import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.button.MenuItemsFactory;
import com.antilia.web.menu.IMenuItemsAuthorizer;
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
		TableModel<Option> tableModel = new TableModel<Option>(Option.class, "id", "translationKey") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected  IColumnModel<Option> createColumnModel(String expresion) {
				IColumnModel<Option> incColumnModel = super.createColumnModel(expresion);
				if(expresion.equals("id"))
					incColumnModel.setWidth(300);
				else
					incColumnModel.setWidth(350);
				return incColumnModel;
			}
		};
		Table<Option> optionList = new Table<Option>("optionList", tableModel, optionsAnswer.getOptions()) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateRowMenu(IMenuItemHolder menu, int row, Option bean) {
				OptionsAnswerListPanel.this.populateRowMenu(menu, row, bean);
			}
			
			@Override
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				OptionsAnswerListPanel.this.addMenuItemsBeforeNavigation(factory);
			}
			
			@Override
			public IMenuItemsAuthorizer getTopMenuAuthorizer() {
				return new IMenuItemsAuthorizer() {
					
					private static final long serialVersionUID = 1L;

					public boolean isMenuItemAuthorized(IMenuItem menuItem) {
						if(menuItem instanceof PageSizeButton<?> || menuItem instanceof RefreshButton<?> 
								|| menuItem instanceof  DropColumnItem<?> || menuItem instanceof UnusedColumnsButton<?>) {
							return false;
						}
						return true;
					}
				};
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
	
	protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
		
	}
	
	public OptionsAnswer getOptionsAnswer() {
		return optionsAnswer;
	}
}
