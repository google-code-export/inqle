package org.inqle.data.rdf.jenabean;

/**
 * Base interface for (persistable) Jenabean objects
 * @author David Donohue
 * Apr 18, 2008
 */
public interface IBasicJenabean {

	public static final String ID_ATTRIBUTE = "id";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String DESCRIPTION_ATTRIBUTE = "description";
	
	public String getId();
	
	public void setId(String id);
	
	public String getUri();
	
	public String getName();
	
	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	/**
	 * Copy all values from the provided ISampler to this one
	 * @return
	 */
	public void replicate(IBasicJenabean templateSampler);
	
	/**
	 * Copy all values from the provided ISampler to this one,
	 * except do not copy the ID (i.e. allow this sampler to have a unique ID)
	 * @return
	 */
	public void clone(IBasicJenabean templateSampler);
	
	/**
	 * Create new clone of self.  
	 * The clone has all fields identical to this, except has unique ID
	 */
	public IBasicJenabean createClone();
	
	/**
	 * Create new replica of self.  
	 * The replica has all fields identical to self
	 */
	public IBasicJenabean createReplica();
}
