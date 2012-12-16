package org.inqle.ui.rap.pages;

public interface IRadioBeanProvider {

	public void setBean(Object bean);
	
	/**
	 * Get the object
	 * @return
	 */
	public Object getValue();
	
	public void setValue(Object value);
}
