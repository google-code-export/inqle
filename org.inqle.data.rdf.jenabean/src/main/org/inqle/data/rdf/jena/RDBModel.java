package org.inqle.data.rdf.jena;

import static org.inqle.data.rdf.jena.AssemblerVocabulary.NS;

import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

@Namespace(NS)
public class RDBModel extends NamedModel {
	//private Connection connection;
	private String connectionId;
	
	public Connection getConnection() {
		//return connection;
		Persister persister = Persister.getInstance();
		Object connectionObj = Persister.reconstitute(Connection.class, getConnectionId(), persister.getMetarepositoryModel(), true);
		return (Connection)connectionObj;
	}

//	public void setConnection(Connection connection) {
//		this.connection = connection;
//	}
	
	public void clone(RDBModel objectToBeCloned) {
		super.clone(objectToBeCloned);
		//setConnection(objectToBeCloned.getConnection());
		setConnectionId(objectToBeCloned.getConnectionId());
	}
	
	public void replicate(RDBModel objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}
	
	public RDBModel createClone() {
		RDBModel newObj = new RDBModel();
		newObj.clone(this);
		return newObj;
	}

	public RDBModel createReplica() {
		RDBModel newObj = new RDBModel();
		newObj.replicate(this);
		return newObj;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
}
