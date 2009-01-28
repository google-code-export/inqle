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
public class LocalFolderDatabase extends UniqueJenabean implements IDatabase {
	
	public void clone(LocalFolderDatabase objectToBeCloned) {
		super.clone(objectToBeCloned);
	}
	
	public void replicate(LocalFolderDatabase objectToBeCloned) {
		clone(objectToBeCloned);
		setId(objectToBeCloned.getId());
	}
	
	public LocalFolderDatabase createClone() {
		LocalFolderDatabase newObj = new LocalFolderDatabase();
		newObj.clone(this);
		return newObj;
	}

	public LocalFolderDatabase createReplica() {
		LocalFolderDatabase newObj = new LocalFolderDatabase();
		newObj.replicate(this);
		return newObj;
	}
	public String getDisplayName() {
		return getId();
	}
	
}