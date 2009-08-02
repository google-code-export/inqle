package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.UniqueCloneableJenabean;

import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
public abstract class DatabaseBackedDatamodel extends Datamodel {
	private String databaseId;

	
	public void clone(DatabaseBackedDatamodel objectToBeCloned) {
		super.clone(objectToBeCloned);
		//setConnection(objectToBeCloned.getConnection());
		setDatabaseId(objectToBeCloned.getDatabaseId());
	}
	
	public void replicate(DatabaseBackedDatamodel objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
}
