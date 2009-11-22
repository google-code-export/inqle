/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import org.inqle.data.rdf.RDF;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

/**
 * Class to assist with creating Jenabean classes.  This class helps in these ways:
 * (1) provides means of creating a unique ID
 * 
 * @author David Donohue
 * Jan 25, 2008
 */
@Namespace(RDF.INQLE)
//public abstract class NamedAndDescribedJenabean extends AJenabean implements ICloneableJenabean {
public abstract class NamedAndDescribedJenabean extends AJenabean implements IJenabean {

	protected String description;
	protected String name;

	@RdfProperty(RDF.DESCRIPTION_PREDICATE)
	public String getDescription() {
		return description;
	}

	@RdfProperty(RDF.NAME_PREDICATE)
	public String getName() {
		if (name == null) {
//			if (this.id != null) {
//				name = this.id;
//			} else {
				name = this.getClass().getName();
//			}
//			return getId();
		}
		return name;
	}

	public void setDescription(String description) {
		this.description = description;		
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
//	@Override
//	public String getStringRepresentation() {
//		String s = getClass().toString() + " {\n";
//		s += "[id=" + getId() + "]\n";
//		s += "[name=" + name + "]\n";
//		s += "[description=" + description + "]\n";
//		s += "}";
//		return s;
//	}

//	@Override
//	public String toString() {
//		return BeanTool.getStringRepresentation(this);
//	}

//	public void clone(ICloneableJenabean template) {
//		setName(template.getName());
//		setDescription(template.getDescription());
//	}

}
