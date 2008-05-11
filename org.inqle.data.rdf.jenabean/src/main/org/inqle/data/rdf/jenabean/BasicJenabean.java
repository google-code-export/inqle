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
	
	@Id
	public String getId() {
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
		String s = getClass().toString() + " {\n";
		s += "[name=" + name + "]\n";
		s += "[description=" + description + "]\n";
		s += "}";
		return s;
	}


	public void clone(IBasicJenabean template) {
		setName(template.getName());
		setDescription(template.getDescription());
	}

}
