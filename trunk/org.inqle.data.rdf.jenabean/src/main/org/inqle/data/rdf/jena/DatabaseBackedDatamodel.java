package org.inqle.data.rdf.jena;

import java.util.UUID;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
public abstract class DatabaseBackedDatamodel extends Datamodel {
	private String databaseId;
//	private String datamodelName;
	
	@Override
	@Id
	public String getId() {
//		return getDatabaseId() + "/" + getDatamodelName();
		return getDatabaseId() + "/" + getName();
	}
	
//	public void clone(DatabaseBackedDatamodel objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		//setConnection(objectToBeCloned.getConnection());
//		setDatabaseId(objectToBeCloned.getDatabaseId());
//	}
//	
//	public void replicate(DatabaseBackedDatamodel objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

//	public void setDatamodelName(String datamodelName) {
//		this.datamodelName = datamodelName;
//	}

//	public String getDatamodelName() {
//		if (datamodelName == null) {
//			setDatamodelName(UUID.randomUUID().toString());
//		}
//		return datamodelName;
//	}
	
	@Override
	public String getName() {
		if (name == null) {
			setName(UUID.randomUUID().toString());
		}
		return name;
	}
}
