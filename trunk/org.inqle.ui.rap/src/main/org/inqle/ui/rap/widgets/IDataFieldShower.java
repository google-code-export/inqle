package org.inqle.ui.rap.widgets;


public interface IDataFieldShower {
	
	public void setEnabled(boolean enabled);

	/**
	 * Sometimes, this object represents a particular RDF property/predicate.  This field contains the URI of such property.
	 * @return
	 */
	public String getFieldUri();

	public void setFieldUri(String fieldUri);
	
	public String getValue();
	
	public void remove();
}
