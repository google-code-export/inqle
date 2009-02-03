package org.inqle.data.rdf.jena;

import java.util.List;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;

public interface IDBConnector {

	/**
	 * This store already exists in the database, and has statements
	 */
	public static final int STORE_HAS_STATEMENTS = -2;
	
	/**
	 * This store already exists in the database, and it is blank
	 */
	public static final int STORE_IS_BLANK = -1;
	
	/**
	 * This store already exists in the database, and it is blank
	 */
	public static final int STORE_NOT_CREATED = 0;
	
	/**
	 * A store was just created in the database
	 */
	public static final int STORE_CREATED = 1;
	
	/**
	 * Get the Jena Datamodel object, representing a set of named graphs
	 * @return
	 */
	public Dataset getDataset(String datasetId);
	
	/**
	 * Get the Jenam model of the specified name
	 * @param modelName
	 * @return
	 */
	public Model getModel(String modelId);
	
	/**
	 * This creates a new database/store.
	 * Deletes the store if it already exists
	 */
	public void formatDatabase();
	
	/**
	 * Create a new database/store only if it does not yet exist
	 * @return IDBConnector.status
	 */
	public int createDatabase();
	
	/**
	 * 
	 * @return true if successful; false if an error occurred
	 */
	public boolean deleteDatabase();
	
	/**
	 * Close the DB Connector, if necessary
	 */
	public void close();
	
	/**
	 * Close the DB Connector, if necessary
	 */
	public boolean testConnection();

	public List<String> listDatabases();

	public List<String> listModels();

	public boolean deleteModel(String id);
}
