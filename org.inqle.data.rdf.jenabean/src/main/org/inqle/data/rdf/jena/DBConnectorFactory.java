package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.jena.tdb.TDBConnector;

public class DBConnectorFactory {
	
	/**
	 * Get the database connector.  Assume it should be a TDBConnector, unless the IDatabase is an
	 * instance of class SDBDatabase (i.e. represents an SDB database)
	 * @param databaseId
	 * @return
	 */
	public static IDBConnector getDBConnector(IDatabase database) {
		if (database==null) {
			return getDBConnector();
		}
//		if (database instanceof SDBDatabase) {
//			return new SDBConnector((SDBDatabase)database);
//		}
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
