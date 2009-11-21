/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.inqle.ui.component.edit.answer.AnswersEditPanel;
import org.inqle.ui.model.IAnswer;

import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.button.MenuItemsFactory;
import com.antilia.web.dialog.DefaultDialog;
import com.antilia.web.dialog.IDialogLink;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class AnswersDialog extends DefaultDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param button
	 * @param dialogStyle
	 */
	public AnswersDialog(String id, IDialogLink button) {
		super(id, button);
		setWidth(800);
		setHeight(300);
		setCentered(true);
		setModal(true);
		setTopLevel(true);
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DefaultDialog#createBody(java.lang.String)
	 */
	@Override
	protected Component createBody(String id) {		
		return new AnswersEditPanel(id) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
				AnswersDialog.this.populateRowMenu(menu, row, bean);
			}
			
			@Override
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				AnswersDialog.this.addMenuItemsBeforeNavigation(factory);
			}
		};
	}
	
	protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
		
	}
	
	protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
	
	}
	
	@Override
	public IModel<String> getTitle() {
		return new Model<String>("Select answer");
	}

}
