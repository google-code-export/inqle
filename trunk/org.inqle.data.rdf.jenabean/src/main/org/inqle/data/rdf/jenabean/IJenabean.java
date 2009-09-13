package org.inqle.data.rdf.jenabean;

import java.util.Date;

import org.inqle.core.domain.INamedAndDescribed;

/**
 * Base interface for (persistable) cloneable Jenabean objects
 * @author David Donohue
 * Apr 18, 2008
 */
public interface IJenabean {

	public static final String ID_ATTRIBUTE = "id";
	
	public String getId();
	
	public void setId(String id);
	
	public String getUri();
	
	public void setRandomId();
	
}
