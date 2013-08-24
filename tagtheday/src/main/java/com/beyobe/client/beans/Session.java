package com.beyobe.client.beans;

import java.util.Date;
import java.util.List;

public interface Session {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract Date getCreated();

	public abstract void setCreated(Date created);
	
	public abstract String getCreatedBy();
	
	public abstract void setCreatedBy(String createdBy);

	public abstract Date getUpdated();

	public abstract void setUpdated(Date updated);
	
	public abstract String getUpdatedBy();
	
	public abstract void setUpdatedBy(String updatedBy);
	
	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getEmail();

	public abstract void setEmail(String email);
	
	public abstract String getLang();

	public abstract void setLang(String lang);

	public abstract List<String> getRoles();
	
	public abstract void setRoles(List<String> roles);

	public abstract String getUserUid();
	
	public abstract void setUserUid(String userId);
}