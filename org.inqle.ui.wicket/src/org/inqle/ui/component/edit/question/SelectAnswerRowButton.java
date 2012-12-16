/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.inqle.ui.model.IAnswer;

import com.antilia.web.dialog.DefaultDialog;
import com.antilia.web.dialog.util.OkDialogButton;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public abstract class SelectAnswerRowButton extends OkDialogButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IAnswer bean;
	/**
	 * @param id
	 * @param dialog
	 */
	public SelectAnswerRowButton(String id, DefaultDialog dialog, IAnswer bean) {
		super(id, dialog);
		this.bean = bean;
	}
	public IAnswer getBean() {
		return bean;
	}
	public void setBean(IAnswer bean) {
		this.bean = bean;
	}

	@Override
	protected String getLabel() {
		return null;
	}
	
	@Override
	protected String getLabelKey() {
		return null;
	}
	
	@Override
	protected IModel<String> getTitleModel() {
		return new Model<String>("Select this row");
	}
}
