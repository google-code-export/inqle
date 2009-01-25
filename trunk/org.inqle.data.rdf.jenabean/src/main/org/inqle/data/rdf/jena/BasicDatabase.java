package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.jenabean.UniqueJenabean;

public class BasicDatabase extends UniqueJenabean implements IDatabase {

	public BasicDatabase createReplica() {
		BasicDatabase replica = new BasicDatabase();
		replica.replicate(this);
		return replica;
	}

	public BasicDatabase createClone() {
		BasicDatabase clone = new BasicDatabase();
		clone.clone(this);
		return clone;
	}

}
