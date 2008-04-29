package org.inqle.data.rdf.jena;

import static org.inqle.data.rdf.jena.AssemblerVocabulary.NS;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

@Namespace(NS)
public class RDBModel extends NamedModel {
	private Connection connection;

	@RdfProperty(NS + "connection")
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Generate a copy of the provided oldConnection, except give the new copy a random unique ID
	 * @param oldConnection
	 * @return
	 */
	public void clone(RDBModel objectToBeCloned) {
		super.clone(objectToBeCloned);
		setModelName(objectToBeCloned.getModelName());
		setConnection(objectToBeCloned.getConnection());
	}
	
	public void replicate(RDBModel objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
	}
	
	@Override
	public RDBModel createClone() {
		RDBModel newObj = new RDBModel();
		newObj.clone(this);
		return newObj;
	}

	@Override
	public RDBModel createReplica() {
		RDBModel newObj = new RDBModel();
		newObj.replicate(this);
		return newObj;
	}
}
