package org.inqle.data.rdf.jenabean;

import org.inqle.core.util.JavaHasher;
import org.inqle.data.rdf.RDF;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * This class is intended to be a base class for objects, which are intended to be
 * reusable and generatable by different INQLE servers.  When an object implementing IGlobalJenabean
 * 
 * @author David Donohue
 * May 12, 2008
 */
@Namespace(RDF.INQLE)
//public abstract class GlobalJenabean extends NamedAndDescribedJenabean implements IGlobalJenabean {
public abstract class GlobalJenabean extends NamedAndDescribedJenabean implements IJenabean {

//	@Id
//	public String getId() {
//		return super.getId();
//	}
	@Id
	@Override
	public String getId() {
		String hash = JavaHasher.hashSha256(getStringRepresentation());
//		String hash = JavaHasher.hashSha256(BeanTool.getStringRepresentationExcludingId(this));
		return hash;
	}
	
//	public void setStringRepresentation(String stringRepresentation) {
//		//do nothing, let the string representation be generated from the fields
//	}
	
	
	/**
	 * Get a representation of this object, which includes all fields except the ID, 
	 * in a predictable order, such that 2 objects with the same values will have the 
	 * identical string representation
	 */
	public abstract String getStringRepresentation();
	
	@Override
	public String toString() {
//		return BeanTool.getStringRepresentation(this);
		return getStringRepresentation();
	}
	
//	public int compareTo(IGlobalJenabean anotherBean) {
//		return getStringRepresentation().compareTo(anotherBean.getStringRepresentation());
//	}
}
