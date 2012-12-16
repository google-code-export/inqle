/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.inqle.ui.model.IAnswer;

import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.button.MenuItemsFactory;
import com.antilia.web.dialog.DefaultDialog;
import com.antilia.web.dialog.DialogLink;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class SelectAnswerDialogLink extends DialogLink {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public SelectAnswerDialogLink(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#getImage()
	 */
	@Override
	protected ResourceReference getImage() {
		return DefaultStyle.IMG_EDIT;
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#getLabel()
	 */
	@Override
	protected String getLabel() {
		return "Select answer";
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#getLabelKey()
	 */
	@Override
	protected String getLabelKey() {
		return "Select answer";
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DialogLink#newDialog(java.lang.String)
	 */
	@Override
	public DefaultDialog newDialog(String id) {
		return new AnswersDialog(id, this) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
				SelectAnswerDialogLink.this.populateRowMenu(menu, row, bean);
				menu.addMenuItem(new SelectAnswerRowButton("select", this, bean) {
					
					private static final long serialVersionUID = 1L;

					protected void onOk(AjaxRequestTarget target, Form<?> form) {
						SelectAnswerDialogLink.this.onOk(target, form, getBean());
					}
				});
			}
			
			@Override
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				SelectAnswerDialogLink.this.addMenuItemsBeforeNavigation(factory);
			}
		};
	}
	
	protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
		
	}
	
	protected void onOk(AjaxRequestTarget target, Form<?> form, IAnswer answer) {
		
	}
	
	protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
	
	}
	
}
