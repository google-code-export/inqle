package org.inqle.ui.rap.widgets;

import com.hp.hpl.jena.rdf.model.RDFNode;

public interface IDataFieldShower {
	
	public void setEnabled(boolean enabled);

	/**
	 * Sometimes, this object represents a particular RDF property/predicate.  This field contains the URI of such property.
	 * @return
	 */
	public String getFieldUri();

	public void setFieldUri(String fieldUri);
	
}
