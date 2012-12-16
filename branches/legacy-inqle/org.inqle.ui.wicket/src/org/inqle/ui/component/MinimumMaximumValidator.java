/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MinimumMaximumValidator<T extends Number> extends AbstractFormValidator {

	
	private static final long serialVersionUID = 1L;
	
	private FormComponent<?>[] components;
	
	/**
	 * 
	 */
	public MinimumMaximumValidator(FormComponent<T> minimum, FormComponent<T> maximum) {
		if (minimum == null)
		{
			throw new IllegalArgumentException("argument formComponent1 cannot be null");
		}
		if (maximum == null)
		{
			throw new IllegalArgumentException("argument formComponent2 cannot be null");
		}
		components = new FormComponent[] { minimum, maximum};
	}


	@Override
	public FormComponent<?>[] getDependentFormComponents() {		 
		return this.components;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void validate(Form<?> form) {
		// we have a choice to validate the type converted values or the raw
		// input values, we validate the raw input
		final FormComponent<T> minimum = (FormComponent<T>)components[0];
		final FormComponent<T> maximum = (FormComponent<T>)components[1];

		if (minimum.getConvertedInput().doubleValue() >= maximum.getConvertedInput().doubleValue())
		{
			error(maximum);
		}
	}
	


}
