/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.util.Date;
import java.util.UUID;


import thewebsemantic.Id;
import thewebsemantic.TypeWrapper;

/**
 * Class to assist with creating Jenabean classes.  This class helps in these ways:
 * (1) provides means of creating a unique ID
 * 
 * @author David Donohue
 * Jan 25, 2008
 */
public abstract class BasicJenabean implements IBasicJenabean {

	private String id;
	private String description;
	private String name;
	private Date creationDate;
	private Date updateDate;
	
	@Id
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUri() {
		return TypeWrapper.instanceURI(this);
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;		
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getClass() + "(" + getName() + ")";
	}

	public abstract IBasicJenabean createClone();

	public abstract IBasicJenabean createReplica();

	public void clone(IBasicJenabean template) {
		setName(template.getName());
		setDescription(template.getDescription());
	}

	/**
	 * Add all field values from the provided template sampler to this sampler, including the ID field
	 */
	public void replicate(IBasicJenabean template) {
		clone(template);
		setId(template.getId());
	}
	
	public Date getCreationDate() {
		if (creationDate == null) {
			creationDate = new Date();
		}
		return creationDate;
	}
	
	public Date getUpdateDate() {
		if (updateDate == null) {
			updateDate = getCreationDate();
		}
		return updateDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
