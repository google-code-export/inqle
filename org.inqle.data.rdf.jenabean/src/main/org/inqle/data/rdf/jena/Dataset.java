package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;

import thewebsemantic.Namespace;

@Namespace(RDF.INQLE)
public abstract class Dataset extends NamedModel {
	//private Connection connection;
	private String connectionId;

//	public void setConnection(Connection connection) {
//		this.connection = connection;
//	}
	
	public void clone(Dataset objectToBeCloned) {
		super.clone(objectToBeCloned);
		//setConnection(objectToBeCloned.getConnection());
		setConnectionId(objectToBeCloned.getConnectionId());
	}
	
	public void replicate(Dataset objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
}
