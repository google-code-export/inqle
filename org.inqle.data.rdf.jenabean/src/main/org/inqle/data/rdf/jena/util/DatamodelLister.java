package org.inqle.data.rdf.jena.util;

import java.util.List;

import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;

public class DatamodelLister {

	public List<PurposefulDatamodel> listAllPurposefulDatamodels() {
		IDBConnector dbConnector = DBConnectorFactory.getDBConnector();
		List<String> databases = dbConnector.listDatabases();
		for (String databaseId: databases) {
			
		}
	}
}
