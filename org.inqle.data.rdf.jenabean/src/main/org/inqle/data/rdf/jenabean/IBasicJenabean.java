package org.inqle.data.rdf.jenabean;

import java.util.Date;

import org.inqle.core.domain.INamedAndDescribed;

/**
 * Base interface for (persistable) Jenabean objects
 * @author David Donohue
 * Apr 18, 2008
 */
public interface IBasicJenabean extends INamedAndDescribed {

	public static final String ID_ATTRIBUTE = "id";
	
	public String getId();
	
	public void setId(String id);
	
	public String getUri();
	
	/**
	 * Copy all values from the provided ISampler to this one,
	 * except do not copy the ID (i.e. allow this sampler to have a unique ID)
	 * @return
	 */
	public void clone(IBasicJenabean objectToBeCloned);
	
	/**
	 * Create new clone of self.  
	 * The clone has all fields identical to this, except has unique ID
	 */
	public IBasicJenabean createClone();
	
}
