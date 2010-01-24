/**
 * 
 */
package org.inqle.rdf.beans;

import java.util.UUID;

import org.inqle.rdf.RDF;

import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.TypeWrapper;

/**
 * Class to assist with creating Jenabean classes.  This class helps in these ways:
 * (1) provides the @Id, which Jenabean uses for crafting the URI
 * (2) provides means of creating a unique ID
 * (3) Convenient way of getting the URI
 * 
 * @author David Donohue
 * Jan 25, 2008
 */
@Namespace(RDF.INQLE)
public abstract class AJenabean implements IJenabean {

	protected String id;
	
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
	
	public void setBaseId() {
		setId("Base_Object_" + getClass().getCanonicalName());
	}
	
	public String getUri() {
		return TypeWrapper.instanceURI(this);
	}
}
