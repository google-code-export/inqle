package org.inqle.data.rdf.jenabean;

import java.util.Date;
import java.util.UUID;

import thewebsemantic.Id;

public abstract class UniqueJenabean extends BasicJenabean implements IUniqueJenabean {

	private Date creationDate;
	private Date updateDate;

	@Id
	public String getId() {
		if (super.getId() == null) {
			setId(UUID.randomUUID().toString());
		}
		return super.getId();
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
	
	public void replicate(IUniqueJenabean objectToReplicate) {
		clone(objectToReplicate);
		setId(objectToReplicate.getId());
	}
}
