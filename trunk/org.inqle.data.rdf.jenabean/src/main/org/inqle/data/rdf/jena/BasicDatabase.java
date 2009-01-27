/**
 * 
 */
package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Namespace;

/**
 * A Connection, which contains internal INQLE data
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDataset(Persister.METAREPOSITORY_DATASET)
@Namespace(RDF.INQLE)
public class BasicDatabase extends UniqueJenabean implements IDatabase {

//	private String role;
//
//	public String getRole() {
//		return role;
//	}
//	public void setRole(String role) {
//		this.role = role;
//	}
	
	public void clone(BasicDatabase objectToBeCloned) {
		super.clone(objectToBeCloned);
//		setRole(objectToBeCloned.getRole());
	}
	
	public void replicate(BasicDatabase objectToBeCloned) {
		clone(objectToBeCloned);
//		setId(objectToBeCloned.getId());
	}
	
	public BasicDatabase createClone() {
		BasicDatabase newObj = new BasicDatabase();
		newObj.clone(this);
		return newObj;
	}

	public BasicDatabase createReplica() {
		BasicDatabase newObj = new BasicDatabase();
		newObj.replicate(this);
		return newObj;
	}
	public String getDisplayName() {
		return getId();
	}
	
}
