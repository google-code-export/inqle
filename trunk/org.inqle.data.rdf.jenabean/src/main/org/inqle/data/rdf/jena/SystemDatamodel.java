/**
 * 
 */
package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

/**
 * A Datamodel, which contains internal INQLE data
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDatamodel(Persister.METAREPOSITORY_DATAMODEL)
@Namespace(RDF.INQLE)
public class SystemDatamodel extends DatabaseBackedDatamodel {

//	private String datasetRole;

//	public String getDatasetRole() {
//		return datasetRole;
//	}
//	public void setDatasetRole(String datasetRole) {
//		this.datasetRole = datasetRole;
//	}
	
	public void clone(SystemDatamodel objectToBeCloned) {
		super.clone(objectToBeCloned);
//		setDatasetRole(objectToBeCloned.getDatasetRole());
	}
	
	public void replicate(SystemDatamodel objectToBeCloned) {
		clone(objectToBeCloned);
		setId(objectToBeCloned.getId());
	}
	
	public SystemDatamodel createClone() {
		SystemDatamodel newObj = new SystemDatamodel();
		newObj.clone(this);
		return newObj;
	}

	public SystemDatamodel createReplica() {
		SystemDatamodel newObj = new SystemDatamodel();
		newObj.replicate(this);
		return newObj;
	}
	
}
