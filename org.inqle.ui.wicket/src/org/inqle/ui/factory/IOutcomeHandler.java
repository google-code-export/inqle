/**
 * 
 */
package org.inqle.ui.factory;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public interface IOutcomeHandler<B extends Serializable> extends Serializable {

	/**
	 * Executed when the used decide to persist changes.
	 * 
	 * @param target
	 * @param form
	 * @param bean
	 */
	public void onSave(AjaxRequestTarget target, Form<?> form, B bean);
	
	/**
	 * Executed when the user decides to cancel changes.
	 * 
	 * @param target
	 * @param bean
	 */
	public void onCancel(AjaxRequestTarget target, B bean);
}
