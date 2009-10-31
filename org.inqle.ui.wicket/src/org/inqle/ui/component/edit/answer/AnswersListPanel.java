/**
 * 
 */
package org.inqle.ui.component.edit.answer;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.dao.IAnswersDao;
import org.inqle.ui.model.IAnswer;

import com.antilia.common.query.IQuery;
import com.antilia.common.query.Query;
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
import com.google.inject.Inject;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class AnswersListPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IAnswersDao dao;
	
	private IQuery<IAnswer> query;
	
	/**
	 * @param id
	 */
	public AnswersListPanel(String id) {
		super(id);
		query = new Query<IAnswer>(IAnswer.class);
		
		Form<IAnswer> form = new Form<IAnswer>("form");
		add(form);
		TableModel<IAnswer> tableModel = new TableModel<IAnswer>(IAnswer.class, "id", "translationKey") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected  IColumnModel<IAnswer> createColumnModel(String expresion) {
				IColumnModel<IAnswer> incColumnModel = super.createColumnModel(expresion);
				if(expresion.equals("id"))
					incColumnModel.setWidth(300);
				else
					incColumnModel.setWidth(350);
				return incColumnModel;
			}
		};
		Table<IAnswer> optionList = new Table<IAnswer>("optionList", tableModel, dao, query) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
				AnswersListPanel.this.populateRowMenu(menu, row, bean);
			}
			
			@Override
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				AnswersListPanel.this.addMenuItemsBeforeNavigation(factory);
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
	protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
		
	}
	
	protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
		
	}
}
