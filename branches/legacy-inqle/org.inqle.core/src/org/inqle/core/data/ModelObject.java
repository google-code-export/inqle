package org.inqle.core.data;

import java.util.Date;
import java.util.UUID;

/**
 * Provides methods for allowing subclasses to more
 * easily comply with Jenabean interfaces
 * @author David Donohue
 *
 */
public abstract class ModelObject {

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		if (id==null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}
	
	public void setRandomId() {
		setId(UUID.randomUUID().toString());
	}
	
	public void setBaseId() {
		setId("Base_Object_" + getClass().getCanonicalName());
	}
	
	private Date creationDate;
	private Date updateDate;
	private String description;
	private String name;
	
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
	
	public void setDescription(String description) {
		this.description = description;		
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}
	
}
