package org.inqle.rdf.beans;

import java.util.Date;

import org.inqle.core.domain.INamedAndDescribed;

/**
 * Base interface for (persistable) Jenabean objects, which are to be unique.  After creation,
 * the ID value does not change.
 * This is the case for objects created & edited by the user, that are intended to be updateable.
 * 
 * @author David Donohue
 * Apr 18, 2008
 */
public interface IUniqueJenabean extends INamedAndDescribedJenabean {

	public Date getCreationDate();
	
	public Date getUpdateDate();
	
	public void setUpdateDate(Date updateDate);
	
	/**
	 * Copy all values from the provided ISampler to this one
	 * @return
	 */
//	public void replicate(IUniqueJenabean objectToReplicate);
	
	/**
	 * Create new replica of self.  
	 * The replica has all fields identical to self
	 */
//	public IUniqueJenabean createReplica();
}
