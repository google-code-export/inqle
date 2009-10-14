package org.inqle.ui.model;

/**
 * Objects with a definite identity.
 * 
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IDentifiable {
	
	/**
	 * @return The ID of the object. Unique within certain context?
	 */
	public String getId();

	/**
	 *  Sets the ID.
	 * @param id
	 */
	public void setId(String id);

}
