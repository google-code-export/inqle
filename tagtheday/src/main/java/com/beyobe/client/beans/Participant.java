package com.beyobe.client.beans;

import java.util.Date;

public interface Participant {

	public abstract long getId();

	public abstract void setId(long id);

	public abstract Date getCreated();

	public abstract void setCreated(Date created);

	public abstract Date getUpdated();

	public abstract void setUpdated(Date updated);
	
	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getLang();

	public abstract void setLang(String lang);

}