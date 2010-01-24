package org.inqle.rdf.beans;

import java.util.Date;

import org.inqle.core.domain.INamedAndDescribed;

/**
 * Base interface for (persistable) cloneable Jenabean objects
 * @author David Donohue
 * Apr 18, 2008
 */
public interface INamedAndDescribedJenabean extends IJenabean, INamedAndDescribed {
	
	/**
	 * Copy all values from the provided ISampler to this one,
	 * except do not copy the ID (i.e. allow this sampler to have a unique ID)
	 * @return
	 */
//	public void clone(ICloneableJenabean objectToBeCloned);
	
	/**
	 * Create new clone of self.  
	 * The clone has all fields identical to this, except has unique ID
	 */
//	public ICloneableJenabean createClone();
	
}
