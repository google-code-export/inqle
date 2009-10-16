/**
 * 
 */
package org.inqle.ui.model;

/**
 * Answer that know how to validate themselves.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public interface ISelfValidatingAnswer extends IAnswer {

	/**
	 * @return if the answer is valid.
	 */
	boolean isValid();
	
	/**
	 * The validation errors associated to the answer!
	 * @return
	 */
	String[] getValidationErrors();
}
