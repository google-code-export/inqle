package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.jena.sdb.SDBConnector;
import org.inqle.data.rdf.jena.tdb.TDBConnector;

public class DBConnectorFactory {
	
	/**
	 * Get the database connector.  Assume it should be a TDBConnector, unless the IDatabase is an
	 * instance of class Connection (i.e. represents an SDB database)
	 * @param databaseId
	 * @return
	 */
	public static IDBConnector getDBConnector(IDatabase database) {
		if (database instanceof Connection) {
			return new SDBConnector((Connection)database);
		}
		return new TDBConnector(database.getId());
	}
	
	/**
	 * Get the default database connector
	 * @param databaseId
	 * @return
	 */
	public static IDBConnector getDBConnector(String databaseId) {
		return new TDBConnector(databaseId);
	}

	public static IDBConnector getDBConnector() {
		return new TDBConnector();
	}
}
