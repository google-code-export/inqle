/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.dao.IQuestionsDao;
import org.inqle.ui.model.Question;

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
public class QuestionsListPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IQuestionsDao questionsDao;
	
	private IQuery<Question> query;
	
	/**
	 * @param id
	 */
	public QuestionsListPanel(String id) {
		super(id);
		query = new Query<Question>(Question.class);
		
		Form<Question> form = new Form<Question>("form");
		add(form);
		TableModel<Question> tableModel = new TableModel<Question>(Question.class, "id", "translationKey") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected  IColumnModel<Question> createColumnModel(String expresion) {
				IColumnModel<Question> incColumnModel = super.createColumnModel(expresion);
				if(expresion.equals("id"))
					incColumnModel.setWidth(300);
				else
					incColumnModel.setWidth(350);
				return incColumnModel;
			}
		};
		Table<Question> optionList = new Table<Question>("optionList", tableModel, questionsDao, query) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateRowMenu(IMenuItemHolder menu, int row, Question bean) {
				QuestionsListPanel.this.populateRowMenu(menu, row, bean);
			}
			
			@Override
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				QuestionsListPanel.this.addMenuItemsBeforeNavigation(factory);
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
	protected void populateRowMenu(IMenuItemHolder menu, int row, Question bean) {
		
	}
	
	protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
		
	}
}
