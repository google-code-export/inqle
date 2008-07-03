package org.inqle.data.rdf.jena.sdb;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.SDBConnection;


/**
 * @author David Donohue
 * Jul 17, 2007
 * 
 */
public class DBConnector {
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
	
	static Logger log = Logger.getLogger(DBConnector.class);
	private Store store = null;
	private Dataset dataset = null;
	private boolean initialized = false;
	private Connection connectionInfo = null;
	
	private String DEFAULT_DB_LAYOUT = "layout2/index";
	private String dbLayout = DEFAULT_DB_LAYOUT;
	private String dbDriver = null;
	private String dbUrl = null;
	private String dbUser = null;
	private String dbPassword = null;
	private String dbType = null;
	
	public DBConnector(Connection connectionInfo) {
		this.connectionInfo = connectionInfo;
		initConnection();
	}
	
	/**
	 * When SDB supports something like this
	 * getStore().getConnection().getTableNames()
	 * then switch to that.  For now, reconstitute the objects via Jenabean & Persister
	 * Yes, this is a circular reference between Persister and DBConnector
	 * @return
	 */
	public List<org.inqle.data.rdf.jena.Dataset> getExternalDatasets() {
		Persister persister = Persister.getInstance();
		Collection<?> datasetObjects = persister.reconstituteAll(Dataset.class);
		List<org.inqle.data.rdf.jena.Dataset> datasets = new ArrayList<org.inqle.data.rdf.jena.Dataset>();
		for (Object datasetObject: datasetObjects) {
			datasets.add((org.inqle.data.rdf.jena.Dataset)datasetObject);
		}
		return datasets;
	}
	
	/**
	 * initialize the connection
	 * @return true if the connection is intialized, false if the connection is not initialized
	 * (i.e. there was a problem instantiating the Driver)
	 */
	private boolean initConnection() {
		if (initialized) return true;
		
		dbLayout  = DEFAULT_DB_LAYOUT;
		dbDriver  = connectionInfo.getDbClass();
		dbUrl  = connectionInfo.getDbURL();
		dbUser = connectionInfo.getDbUser();
		dbPassword = connectionInfo.getDbPassword();
		dbType = connectionInfo.getDbType();
		
		try {
	    // Instantiate database driver
			if (dbDriver == null) {
				throw new ClassNotFoundException("Database driver is null");
			}
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
	  	log.error("Unable to declare DB Driver: " + dbDriver, e);
	  	return false;
	  }
	  
	  //initialize the dataset
	  initialized = true;
	  return true;
	}

	/**
	 * Retrieves a com.hp.hpl.jena.query.Dataset object, representing 1 or more Jena models
	 * @return
	 */
	public Dataset getDataset() {
		if (dataset != null) {
			return dataset;
		}
		dataset = SDBFactory.connectDataset(getStore());
		return dataset;
	}
	
	/**
	 * Retrieves a com.hp.hpl.jena.query.Dataset object, representing 1 or more Jena models
	 * @return
	 */
	public Store getStore() {
		if (store != null) {
			return store;
		}
		if (!initialized) {
			boolean successInitializing = initConnection();
			if (! successInitializing) return null;
		}
		StoreDesc storeDesc = new StoreDesc(dbLayout, dbType);
		SDBConnection conn = new SDBConnection(dbUrl, dbUser, dbPassword);
		store = SDBFactory.connectStore(conn, storeDesc);
		log.debug("retrieved store: dbUrl=" + dbUrl + "; dbLayout=" + dbLayout + "; dbType=" + dbType + "; dbUser=" + dbUser + "; dbPassword=" + dbPassword);
		return store;
	}
	
	/**
	 * Retrieves a Jena Model.  
	 * Ensure that the SDB Store is created first, such that the database
	 * is an SDB one instead of an ARQ one.
	 * @param modelName the name of the named graph to retrieve.  If null
	 * or blank, retrieve the default graph
	 * @return the Model object
	 */
	public Model getModel(String modelName) {
		Store store = getStore();
		
		//initialize SDB's connection to the dataset
	  //getDataset();
		Model model = null;
	  if (modelName == null || modelName.length()==0) {
	  	model = SDBFactory.connectDefaultModel(store);
	  } else {
	  	model = SDBFactory.connectNamedModel(store, modelName);
	  }
		return model;
	}
	
	public void close() {
		try {
			store.getConnection().close();
			store.close();
		} catch (Exception e) {
			//do not close if unable
		}
	}

	/**
   * Get a java.sql.Connection object, for Jena queries.
   * This might not be supported by SDB
   * 
   * @return a java.sql.Connection to the database
   * @throws SQLException 
   */
  public java.sql.Connection getJdbcConnection() throws SQLException {
  	if (!initialized) {
  		boolean successInitializing = initConnection();
  		if (! successInitializing) return null;
  	}
  	return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
  }

  /**
   * Get a Jena com.hp.hpl.jena.db.DBConnection object, for Jena ARQ queries
   * @return
   */
	public DBConnection getJenaConnection() {
		if (!initialized) {
  		boolean successInitializing = initConnection();
  		if (! successInitializing) return null;
  	}
		return new DBConnection(dbUrl, dbUser, dbPassword, dbType);
	}

	/**
	 * @return true if able to get a JDBC connection
	 */
	public boolean testConnection() {
		try {
			//DBConnection testConn = getJenaConnection();
			//ExtendedIterator allModelNames = testConn.getAllModelNames();
			//log.info("^^^Got Jena DBConnection:" + testConn + "\n" + allModelNames);
			Store store = getStore();
			log.info("Testing store: retrieved store " + store);
			if (store == null) return false;
			//When store has not yet had statements inserted, SDB API seems to return a store but this store throws an error on getSize() method.
			//long storeSize = store.getSize();
			//log.info("Testing store: has size " + storeSize);
			return true;
		} catch (Exception e) {
			log.info("Testing store: fails.", e);
			return false;
		}
	}
	
	/**
	 * This creates a new SDB store.
	 * ** CAUTION: This deletes the store if it already exists! **
	 */
	public void createSDBStore() {
		log.info("createSDBStore()...");
		try {
			getStore().getTableFormatter().create();
		} catch (Exception e) {
			log.error("Unable to createSDBStore()", e);
		}
	}

	/**
	 * Create a new SDB store only if it does not yet exist
	 * @return DBConnector.status
	 */
	public int tryToCreateSDBStore() {
		log.info("Trying to create store...");
		int status = STORE_NOT_CREATED;
		Store store = getStore();
		
		try {
			//When store has not yet been created, SDB API will return a store but this store throws an error on getSize() method.
			long storeSize = store.getSize();
			log.info("Store already has size " + storeSize);
			if (storeSize > 0) {
				status = STORE_HAS_STATEMENTS;
			} else {
				status = STORE_IS_BLANK;
			}
		} catch (Exception e) {
			log.error("Unable to connect to store " + connectionInfo.getDbUser() + "@" + connectionInfo.getDbURL() + "\nCreating this store...");
			createSDBStore();
			status = STORE_CREATED;
		}
		
		return status;
	}
	
	/**
	 * 
	 * @return true if successful; false if an error occurred
	 */
	public boolean deleteSDBStore() {
		Store store = getStore();
		try {
			store.getTableFormatter().truncate();
			return true;
		} catch (Exception e) {
			log.error("Unable to delete store", e);
			return false;
		}
	}
	
	public ModelMaker getModelMaker() {
		ModelMaker maker = ModelFactory.createModelRDBMaker(getJenaConnection());
		return maker;
	}
	
	public OntModel getMemoryOntModel(String modelName) {
		Model baseModel = getModel(modelName);
		
		OntModelSpec modelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
		modelSpec.setImportModelMaker(getModelMaker());
    
		OntModel ontModel = ModelFactory.createOntologyModel(modelSpec, baseModel);
		return ontModel;
	}
}
