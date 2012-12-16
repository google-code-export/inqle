package org.inqle.core.beans;

import java.util.Date;
/**
 * @deprecated
 * @author gd9345
 *
 */
public abstract class DatedBean {

	private Date creationDate;
	private Date updateDate;
	
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
