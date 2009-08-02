package org.inqle.data.rdf.jenabean;

import java.util.Date;
import java.util.UUID;

import org.inqle.data.rdf.RDF;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
public abstract class UniqueCloneableJenabean extends CloneableJenabean implements IUniqueJenabean {

	private Date creationDate;
	private Date updateDate;

//	@Id
//	public String getId() {
//		if (super.getId() == null) {
//			setId(UUID.randomUUID().toString());
//		}
//		return super.getId();
//	}
	
	/**
	 * If the ID is null, it will be set to the ID of the base object.
	 * New ID is created upon cloning this object.
	 */
	@Override
	@Id
	public String getId() {
		if (id == null) {
			id = this.getClass().getName();
		}
		return id;
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
	
	/**
	 * Assign a clone a new unique ID
	 * @param objectToClone
	 */
	public void clone(IUniqueJenabean objectToClone) {
		super.clone(objectToClone);
		setId(UUID.randomUUID().toString());
	}
	
	public void replicate(IUniqueJenabean objectToReplicate) {
		clone(objectToReplicate);
		setId(objectToReplicate.getId());
	}
	
	@Override
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[id=" + getId() + "]\n";
		s += "[name=" + name + "]\n";
		s += "[description=" + description + "]\n";
		s += "}";
		return s;
	}
}
