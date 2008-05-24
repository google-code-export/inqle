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
	
	public void clone(RDBModel objectToBeCloned) {
		super.clone(objectToBeCloned);
		setConnection(objectToBeCloned.getConnection());
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
}
