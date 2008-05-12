package org.inqle.data.rdf.jenabean;

import java.util.Date;

import org.inqle.core.domain.INamedAndDescribed;

/**
 * Base interface for (persistable) Jenabean objects, which are reusable.
 * inplementations must return for the getId() method, a string which represents
 * a (reproducible) hash of the object.  
 * 
 * If your implementation contains 
 * recordkeeping fields, which are not of relevance to the object's global 
 * value, these fields should be removed before calculating this hash.  
 * 
 * @see GlobalJenabean
 * 
 * @author David Donohue
 * Apr 18, 2008
 */
public interface IGlobalJenabean extends INamedAndDescribed {
	
	public String getStringRepresentation();
	public void setStringRepresentation(String stringRepresentation);
}
