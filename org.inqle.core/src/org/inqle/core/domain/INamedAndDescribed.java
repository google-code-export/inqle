package org.inqle.core.domain;

public interface INamedAndDescribed {

	String NAME_ATTRIBUTE = "name";
	String DESCRIPTION_ATTRIBUTE = "description";
	
	public String getName();
	public String getDescription();
	public void setName(String name);
	public void setDescription(String description);
}
