/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.inqle.ui.component.edit.answer.AnswersEditPanel;

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
	}

	/* (non-Javadoc)
	 * @see com.antilia.web.dialog.DefaultDialog#createBody(java.lang.String)
	 */
	@Override
	protected Component createBody(String id) {		
		return new AnswersEditPanel(id);
	}
	
	@Override
	public IModel<String> getTitle() {
		return new Model<String>("Select answer");
	}

}
