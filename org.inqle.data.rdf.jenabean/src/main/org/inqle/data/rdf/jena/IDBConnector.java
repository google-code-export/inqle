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
	 * Get the Jena Dataset object, representing a set of named graphs
	 * @return
	 */
	public Dataset getDataset(String modelType, String datasetId);
	
	/**
	 * Get the Jena "data" model of the specified name
	 * @param modelName
	 * @return
	 */
	public Model getModel(String modelType, String modelName);
	
	/**
	 * This creates a new database/store.
	 * Deletes the store if it already exists
	 */
	public void formatDatabase();
	
	/**
	 * Create a new database/store only if it does not yet exist
	 * @return IDBConnector.status
	 */
	public boolean createDatabase();
	
	/**
	 * 
	 * @return true if successful; false if an error occurred
	 */
	public boolean deleteDatabase();
	
	/**
	 * Close the DB Connector, if necessary
	 */
	public void close();
	
	public boolean testConnection();

	/**
	 * List all databases on this INQLE server
	 * @return
	 */
	public List<String> listAllDatabases();

	public List<String> listModelNames(String modelType);

	public boolean deleteModel(String modelType, String id);

	public boolean modelExists(String modelType, String datamodelName);
}
