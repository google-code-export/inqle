/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.util.Date;
import java.util.UUID;

import org.inqle.data.rdf.RDF;


import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.TypeWrapper;

/**
 * Class to assist with creating Jenabean classes.  This class helps in these ways:
 * (1) provides means of creating a unique ID
 * 
 * @author David Donohue
 * Jan 25, 2008
 */
@Namespace(RDF.INQLE)
public abstract class BasicJenabean implements IBasicJenabean {

	protected String id;
	protected String description;
	protected String name;
	
	@Id
	public String getId() {
		if (id==null) {
			setRandomId();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setRandomId() {
		setId(UUID.randomUUID().toString());
	}
	
	public String getUri() {
		return TypeWrapper.instanceURI(this);
	}

	@RdfProperty(RDF.DESCRIPTION_PREDICATE)
	public String getDescription() {
		return description;
	}

	@RdfProperty(RDF.NAME_PREDICATE)
	public String getName() {
		if (name == null) {
			//return this.getClass().getName();
			return getId();
		}
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
		s += "[id=" + getId() + "]\n";
		s += "}";
		return s;
	}


	public void clone(IBasicJenabean template) {
		setName(template.getName());
		setDescription(template.getDescription());
	}

}
