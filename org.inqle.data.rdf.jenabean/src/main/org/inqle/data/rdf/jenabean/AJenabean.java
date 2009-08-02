/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.util.UUID;

import org.inqle.data.rdf.RDF;

import thewebsemantic.Id;
import thewebsemantic.Namespace;
import thewebsemantic.TypeWrapper;

/**
 * Class to assist with creating Jenabean classes.  This class helps in these ways:
 * (1) provides means of creating a unique ID
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
	
	public String getUri() {
		return TypeWrapper.instanceURI(this);
	}
}
