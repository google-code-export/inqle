/**
 * 
 */
package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

/**
 * A SDBDatabase, which contains internal INQLE data
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDatamodel(Persister.METAREPOSITORY_DATAMODEL)
@Namespace(RDF.INQLE)
public class InternalConnection extends SDBDatabase {

	private String connectionRole;

	public String getConnectionRole() {
		return connectionRole;
	}
	public void setConnectionRole(String connectionRole) {
		this.connectionRole = connectionRole;
	}
	
	public void clone(InternalConnection objectToBeCloned) {
		super.clone(objectToBeCloned);
		setConnectionRole(objectToBeCloned.getConnectionRole());
	}
	
	public void replicate(InternalConnection objectToBeCloned) {
		clone(objectToBeCloned);
		setId(objectToBeCloned.getId());
	}
	
	public InternalConnection createClone() {
		InternalConnection newObj = new InternalConnection();
		newObj.clone(this);
		return newObj;
	}

	public InternalConnection createReplica() {
		InternalConnection newObj = new InternalConnection();
		newObj.replicate(this);
		return newObj;
	}
	
}
