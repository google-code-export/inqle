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
public abstract class GlobalJenabean extends BasicJenabean implements IGlobalJenabean {

//	@Id
//	public String getId() {
//		return super.getId();
//	}
	@Id
	@Override
	public String getId() {
		String hash = JavaHasher.hashSha256(getStringRepresentation());
		return hash;
	}
	
	public void setStringRepresentation(String stringRepresentation) {
		//do nothing, let the string representation be generated from the fields
	}
	
	@Override
	public String toString() {
		return getStringRepresentation();
	}
	
//	public int compareTo(IGlobalJenabean anotherBean) {
//		return getStringRepresentation().compareTo(anotherBean.getStringRepresentation());
//	}
}
