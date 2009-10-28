/**
 * 
 */
package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;

import thewebsemantic.Namespace;

/**
 * A Datamodel, which contains internal INQLE data.  Use this type when you need 1 and only 1
 * datamodel to exist, of a particular ID
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDatamodelName(Persister.METAREPOSITORY_DATAMODEL)
@Namespace(RDF.INQLE)
public class SystemDatamodel extends DatabaseBackedDatamodel {
	
//	public void clone(SystemDatamodel objectToBeCloned) {
//		super.clone(objectToBeCloned);
////		setDatasetRole(objectToBeCloned.getDatasetRole());
//	}
//	
//	public void replicate(SystemDatamodel objectToBeCloned) {
//		clone(objectToBeCloned);
//		setId(objectToBeCloned.getId());
//	}
//	
//	public SystemDatamodel createClone() {
//		SystemDatamodel newObj = new SystemDatamodel();
//		newObj.clone(this);
//		return newObj;
//	}
//
//	public SystemDatamodel createReplica() {
//		SystemDatamodel newObj = new SystemDatamodel();
//		newObj.replicate(this);
//		return newObj;
//	}
	
}
