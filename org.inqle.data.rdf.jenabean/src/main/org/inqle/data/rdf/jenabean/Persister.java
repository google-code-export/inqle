package org.inqle.data.rdf.jenabean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.Datafile;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.sdb.DBConnector;

import thewebsemantic.Bean2RDF;
import thewebsemantic.NotFoundException;
import thewebsemantic.RDF2Bean;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.FileUtils;


/**
 * This class interfaces with the application's RDF model, 
 * to store and retrieve Object objects
 * 
 * Each instance of Persister might have 1 or more open connections to the database.  
 * When finished with an instance of persister, you should call the 
 * <code>close()</code> method.
 * @author David Donohue
 * December 5, 2007
 * 
 * TODO reorganize into fewer public methods, which handle closing models.
 */
public class Persister {
	
	public static final String SYSTEM_PROPERTY_TEMP_DIR = "java.io.tmpdir";
	public static final String FILENAME_APPINFO = "assets/_private/AppInfo.ttl";
	public static final String TEMP_DIRECTORY = "assets/temp/";
	public static final Class<?>[] MODEL_CLASSES = {Dataset.class, Datafile.class};
	private AppInfo appInfo = null;
	private OntModel metarepositoryModel = null;
	private OntModel logModel = null;
	private static Logger log = Logger.getLogger(Persister.class);
	public static int persisterId = 0;
	
	/* *********************************************************************
	 * *** FACTORY METHODS
	 * ********************************************************************* */
	private Persister() {}
	
	/**
	* AgentRegistryHolder is loaded on the first execution of AgentRegistry.getInstance() 
	* or the first access to AgentRegistryHolder.instance, not before.
	*/
	private static class PersisterHolder { 
		private final static Persister instance = new Persister();
	}
	 
	public static Persister getInstance() {
		return PersisterHolder.instance;
	}
	
	public static Persister getInstance(AppInfo appInfo) {
		Persister persister = PersisterHolder.instance;
		persister.setAppInfo(appInfo);
		return persister;
	}
	
//	/**
//	 * private constructor.  Use static method <code>createPersister()</code>
//	 * to create new instance of Persister
//	 * @param appInfo
//	 */
//	private Persister(AppInfo appInfo) {
//		persisterId++;
//		this.appInfo = appInfo;
//	}
//	
//	/**
//	 * Creates a new Persister, using the provided AppInfo object
//	 * @param appInfo
//	 * @return
//	 */
//	public static Persister createPersister(AppInfo appInfo) {
//		assert(appInfo != null);
//		Persister persister = new Persister(appInfo);
//		return persister;
//	}
//	
//	/**
//	 * Create a new Persister, by loading the AppInfo file from its known location
//	 * @return
//	 */
//	public static Persister createPersister() {
//		AppInfo appInfo = loadAppInfo();
//		Persister persister = new Persister(appInfo);
//		return persister;
//	}

	/* *********************************************************************
	 * *** APPINFO METHODS
	 * ********************************************************************* */
	
	private void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public AppInfo getAppInfo() {
		if (appInfo == null) {
			appInfo = Persister.loadAppInfo();
		}
		return appInfo;
	}
	
	public static AppInfo loadAppInfo() {
		RDF2Bean reader = new RDF2Bean(getAppInfoModel());
		AppInfo loadedAppInfo = null;
		try {
			loadedAppInfo = (AppInfo)reader.load(AppInfo.class, AppInfo.APPINFO_INSTANCE_ID);
			//log.info("Retrieved appInfo:" + JenabeanWriter.toString(loadedAppInfo));
		} catch (NotFoundException e) {
			log.warn("AppInfo not available.");
		}
		return loadedAppInfo;
	}
	
	/**
	 * get file path to AppInfo file
	 * @return
	 */
	public static String getAppInfoFilePath() {
		return System.getProperty(InqleInfo.INQLE_HOME) + FILENAME_APPINFO;
	}
	
	public static String getAppFilePath() {
		return System.getProperty(InqleInfo.INQLE_HOME);
	}
	
	public static File getTempDirectory() {
		//System.setProperty(SYSTEM_PROPERTY_TEMP_DIR, getAppFilePath() + TEMP_DIRECTORY);
		return new File(System.getProperty(SYSTEM_PROPERTY_TEMP_DIR));
	}
	
	/**
	 * Get the Jena model containing the AppInfo object
	 * @return
	 */
	private static OntModel getAppInfoModel() {
		OntModel appInfoModel = null;
		try {
			appInfoModel = ModelFactory.createOntologyModel();
			appInfoModel.add(FileManager.get().loadModel( getAppInfoFilePath() ));
		} catch (Exception e) {
			log.error("Error getting Model for AppInfo:", e);
		}
		//log.info("Retrieved appInfoModel w/ " + appInfoModel.size() + " statements");
		return appInfoModel;
	}

	/* *********************************************************************
	 * *** JENA MODEL METHODS
	 * ********************************************************************* */
	
	/**
	 * Creates a new Jena SDB Model, 
	 * given a Connection object and the name of a model.
	 * 
	 * TODO Note that this might be different from a SDB data model
	 * @param dbConnectionInfo
	 * @param dbModelName
	 * @return
	 */
	public Model createDBModel(Connection connection, String dbModelName) {
		assert(connection != null && dbModelName != null && dbModelName.length() > 0);
		DBConnector dbConnector = new DBConnector(connection);
		//DBConnection dbConnection = dbConnector.getJenaConnection();
				
		// create a model maker with the given connection parameters
		//ModelMaker maker = ModelFactory.createModelRDBMaker(dbConnection);

		log.debug("Creating Model of name '" + dbModelName + "'.");
		//Model model = maker.createModel(dbModelName, true);
		
		//close opened connections
		//maker.close();
		/*try {
			dbConnection.close();
		} catch (SQLException e) {
			//don't close if unable
		}
		dbConnector.close();
		*/
		
		Model model = dbConnector.getModel(dbModelName);
		return model;
	}
	
	/**
	 * Creates a new Jena SDB Model, 
	 * given a Connection object and the name of a model.
	 * 
	 * Best practice is to close() the model after use.
	 * 
	 * TODO Note that this might be different from a SDB data model
	 * @param dbConnectionInfo
	 * @param dbModelName
	 * @return
	 */
	public Model createDBModel(Dataset rdbModel) {
		Connection connection = getConnection(rdbModel.getConnectionId());
		//String dbModelName = rdbModel.getModelName();
		String dbModelName = rdbModel.getId();
		assert(connection != null && dbModelName != null && dbModelName.length() > 0);
		DBConnector dbConnector = new DBConnector(connection);
		//DBConnection dbConnection = dbConnector.getJenaConnection();
				
		// create a model maker with the given connection parameters
		//ModelMaker maker = ModelFactory.createModelRDBMaker(dbConnection);

		log.debug("Creating Model of name '" + dbModelName + "'.");
		//Model model = maker.createModel(dbModelName, true);
		
		//close opened connections
		//maker.close();
		/*try {
			dbConnection.close();
		} catch (SQLException e) {
			//don't close if unable
		}
		dbConnector.close();
		*/
		
		Model model = dbConnector.getModel(dbModelName);
		
		//store the Dataset object
		persist(rdbModel, getMetarepositoryModel());
		return model;
	}
	
	/**
	 * Given the URI or ID of a NamedModel, get the Jena Model object.
	 * 
	 * Best practice is to close() the model after use.
	 * @param namedModelUri
	 * @return the model, or null if no model found in the metarepository
	 */
	public Model getModel(String namedModelId) {
		assert(namedModelId != null && namedModelId.length() > 0);
		NamedModel namedModel = getNamedModel(namedModelId);
		if (namedModel != null) {
			return getModel(namedModel);
		}
		return null;
	}
	
	/**
	 * Given an instance of a NamedModel, retrieve the Jena model
	 * @param namedModel
	 * @return
	 */
	public Model getModel(NamedModel namedModel) {
		assert(namedModel != null);
		
		Model repositoryModel = getMetarepositoryModel();
		//if the model being requested is not in the Repositories model, retrieve that specially
		if (namedModel.getId().equals(getAppInfo().getMetarepositoryDataset().getId())) {
			//log.info("#" + persisterId + ":getModel(" + namedModel.getId() + "): return metarepository model");
			return repositoryModel;
		}
		
		//log.info("#" + persisterId + ":getModel(" + namedModel.getId() + "): get new model");
		//otherwise the requested model is a regular data-containing model.  Retrieve it from the Repositories Model
		Model model = null;
		if (namedModel instanceof Dataset) {
			Dataset rdbModel = (Dataset)namedModel;
			RDF2Bean reader = new RDF2Bean(repositoryModel);
			Connection dbConnectionInfo;
			try {
				dbConnectionInfo = (Connection)reader.load(Connection.class, getConnection(rdbModel.getConnectionId()).getId());
			} catch (NotFoundException e) {
				log.error("Unable to load Connection " + getConnection(rdbModel.getConnectionId()).getId());
				return null;
			}
			DBConnector connector = new DBConnector(dbConnectionInfo);
			model = connector.getModel(namedModel.getId());
			
			/*if null, create a new model
			if (model == null) {
				log.debug("Creating Dataset '" + namedModel.getModelName() + "'...");
				model = createDBModel(dbConnectionInfo, namedModel.getModelName());
			}
			*/
			
			//close
			//connector.close();
			
		} else if (namedModel instanceof Datafile){
			model = Persister.getModelFromFile(((Datafile)namedModel).getFileUrl());
		}
		
		return model;
	}
	
	/**
	 * Loads an RDF file from local filesystem or remote (HTTP) source
	 * @param filePath the file path or URL
	 * @return the resulting Model
	 */
	private static Model getModelFromFile(String filePath) {
		try {
			Model model = FileManager.get().loadModel( filePath );
			return model;
		} catch (Exception e) {
			log.error("Unable to retrieve model from " + filePath + ".  It may not exist.", e);
			return null;
		}
	}

	/**
	 * Given the URI of a NamedModel, get the Jena Model object
	 * @param namedModelUri
	 * @return
	 */
	public OntModel getOntModel(String namedModelId) {
		NamedModel namedModel = getNamedModel(namedModelId);
		if (namedModel != null) {
			return getOntModel(namedModel);
		}
		return null;
	}

	/**
	 * Given an instance of a NamedModel, retrieve the Jena model
	 * @param aModel
	 * 
	 * @return
	 * TODO Untested
	 */
	public OntModel getOntModel(NamedModel namedModel) {
		OntModel repositoryOntModel = getMetarepositoryModel();
		//if the model being requested is not in the Repositories model, retrieve that specially
		if (namedModel.getId().equals(getAppInfo().getMetarepositoryDataset().getId())) {
			return repositoryOntModel;
		}
		
		//otherwise the requested model is a regular data-containing model.  Retrieve it from the Repositories Model
		OntModel ontModel = null;
		if (namedModel instanceof Dataset) {
			Dataset rdbModel = (Dataset)namedModel;
			RDF2Bean reader = new RDF2Bean(repositoryOntModel);
			Connection dbConnectionInfo;
			try {
				dbConnectionInfo = (Connection)reader.load(Connection.class, getConnection(rdbModel.getConnectionId()).getId());
			} catch (NotFoundException e) {
				log.error("Unable to load Connection info " + rdbModel.getConnectionId());
				e.printStackTrace();
				return null;
			}
			DBConnector connector = new DBConnector(dbConnectionInfo);
//			ontModel = connector.getOntModel(namedModel.getModelName());
			ontModel = connector.getMemoryOntModel(namedModel.getId());
			
			/*if null, create a new model
			if (model == null) {
				log.debug("Creating Dataset '" + namedModel.getModelName() + "'...");
				model = createDBModel(dbConnectionInfo, namedModel.getModelName());
			}
			*/
			
			//close
			//connector.close();
			
		} else if (namedModel instanceof Datafile){
			ontModel = ModelFactory.createOntologyModel();
			ontModel.add(getModelFromFile(((Datafile)namedModel).getFileUrl()));
		}
		
		return ontModel;
	}
	
	/* *********************************************************************
	 * *** INTERNAL MODEL METHODS
	 * ********************************************************************* */
	
//	public OntModel getLogModel() {
//		//if (metarepositoryModel != null && ! metarepositoryModel.isClosed()) {
//		if (logModel != null) {
//			//log.info("#" + persisterId + ":getRepositoryModel(): return saved metarepository");
//			return this.logModel;
//		}
//		//log.info("#" + persisterId + ":getRepositoryModel(): get new metarepository");
//		Connection logConnection = null;
//		NamedModel logNamedModel = getAppInfo().getLogNamedModel();
//		
//		if (logNamedModel instanceof Dataset) {
//			logConnection = ((Dataset)logNamedModel).getConnection();
//		}
//		//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
//		DBConnector connector = new DBConnector(logConnection);
//		log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + logNamedModel.getId());
//		
//		//this.logModel = connector.getOntModel(logNamedModel.getModelName());
//		this.logModel = connector.getOntModel(logNamedModel.getId());
//		
//		return this.logModel;
//	}
	
	public OntModel getMetarepositoryModel() {
		//if (metarepositoryModel != null && ! metarepositoryModel.isClosed()) {
		if (metarepositoryModel != null) {
			//log.info("#" + persisterId + ":getRepositoryModel(): return saved metarepository");
			return this.metarepositoryModel;
		}
		//log.info("#" + persisterId + ":getRepositoryModel(): get new metarepository");
		Dataset metarepositoryRDBModel = getAppInfo().getMetarepositoryDataset();
		Connection metarepositoryConnection = getAppInfo().getMetarepositoryConnection();
		
		//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
		DBConnector connector = new DBConnector(metarepositoryConnection);
		//log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + repositoryNamedModel.getModelName());
		log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + metarepositoryConnection.getId());

		//this.metarepositoryModel = connector.getOntModel(repositoryNamedModel.getModelName());
		this.metarepositoryModel = connector.getMemoryOntModel(metarepositoryRDBModel.getId());
		return this.metarepositoryModel;
	}
	
	
	/* *********************************************************************
	 * *** CONNECTION METHODS
	 * ********************************************************************* */
	
	public Connection getConnection(String connectionId) {
		Object connectionObj = Persister.reconstitute(Connection.class, connectionId, getMetarepositoryModel(), true);
		return (Connection)connectionObj;
	}

	/**
	 * Store a new Connection object in the metarepository and
	 * create the SDB store in the actual database.
	 * TODO: handle connection problems
	 * @param testConnectionInfo
	 */
	public int createNewDBConnection(Connection connection) {
		//log.info("Will try to create a new Conection spec for test data:\n" + JenabeanWriter.toString(connection));
		//first create the DBConnector and use it to create the SDB store in the database
		DBConnector connector = new DBConnector(connection);
		//int status = DBConnector.STORE_CREATED;
		int status = connector.tryToCreateSDBStore();
		//connector.createSDBStore();
		log.debug("Tried to create new SDB store, with status=" + status);
		//next register the new DB in the repositories namedModel
		//TODO: when deleting works, remove the below " || status == DBConnector.STORE_IS_BLANK"
		if (status == DBConnector.STORE_CREATED || status == DBConnector.STORE_IS_BLANK) {
			//log.info("Persisting new connectioninfo ... \n" + JenabeanWriter.toString(connectionInfo) + "\n...to the repository model...");
			OntModel metarepositoryOntModel = getMetarepositoryModel();
			//log.info("BEFORE: Repository model has " + metarepositoryOntModel.size() + " statements");
			persist(connection, metarepositoryOntModel, true);
			//log.info("AFTER: Repository model has " + metarepositoryOntModel.size() + " statements");
			//metarepositoryModel.commit();
		}
		
		//TODO consider close() this object in Persister.close()
		//connector.close();
		
		return status;
	}
	
	/* *********************************************************************
	 * *** NAMEDMODEL METHODS
	 * ********************************************************************* */
	/**
	 * Gets the NamedModel matching the provided URI
	 * @return the NamedModel
	 * 
	 * TODO add support fo any other NamedModel subclasses e.g. UrlModel
	 */
	public NamedModel getNamedModel(String namedModelId) {
		OntModel metarepositoryModel = getMetarepositoryModel();
		for (Class<?> clazz: MODEL_CLASSES) {
			NamedModel namedModel = (NamedModel)reconstitute(clazz, namedModelId, metarepositoryModel, true);
			if (namedModel != null) {
				return namedModel;
			}
		}
		return null;
	}
	
	/* *********************************************************************
	 * *** PERSISTING METHODS
	 * ********************************************************************* */
		/**
		 * Persist a Object object to an SDB store as RDF
		 * @param persistableObj the object implementing Persistable_legacy interface
		 * @param model the Jena model into which to persist
		 * @param persistMembers if true, all Object members (and their members, recursively)
		 * will be persisted.  If false, they will not.
		 */
		public void persist(Object persistableObj, Model model) {
			//log.info("Persisting:\n" + JenabeanWriter.toString(persistableObj));
			persist(persistableObj, model, true);
		}
	
	/**
	 * Persist a Object object to an SDB store as RDF
	 * @param persistableObj the object implementing Persistable_legacy interface
	 * @param model the Jena model into which to persist
	 * @param persistMembers if true, all Object members (and their members, recursively)
	 * will be persisted.  If false, they will not.
	 * TODO verify this works as described above
	 */
	public void persist(Object persistableObj, Model model, boolean persistMembers) {
		log.trace("Persister.persist():" + JenabeanWriter.toString(persistableObj));
		if (persistableObj instanceof IUniqueJenabean) {
			((IUniqueJenabean)persistableObj).setUpdateDate(new Date());
		}
		//log.info("set update date");
		Bean2RDF writer = new Bean2RDF(model);
		if (persistMembers) {
			writer.saveDeep(persistableObj);
		} else {
			writer.save(persistableObj);
		}
		//log.info("Saved");
	}

	/**
	 * Save the Object to a file using "RDF/XML" format
	 * @param persistableObj
	 * @param fileName the name of the file in the filesystem
	 * @param includeMembers if true, all members will be recursively included in the file
	 * Possible values for lang include
	 * "RDF/XML", "RDF/XML-ABBREV", "N-TRIPLE" and "N3"
	 * @throws FileNotFoundException 
	 */
	public static void persistToFile(Object persistableObj, String fileName, boolean includeMembers) throws FileNotFoundException {
		persistToFile(persistableObj, fileName, "N3", includeMembers);
	}
	
	/**
	 * Save the persistable to a file
	 * Possible values for lang include
	 * "RDF/XML", "RDF/XML-ABBREV", "N-TRIPLE" and "N3"
	 * @throws FileNotFoundException 
	 */
	public static void persistToFile(Object persistableObj, String fileName, String lang, boolean includeMembers) throws FileNotFoundException {
		OntModel persistableAsModel = ModelFactory.createOntologyModel();
		Bean2RDF converter = new Bean2RDF(persistableAsModel);
		converter.save(persistableObj);
		//the statements to add
		
		persistableAsModel.setNsPrefix("inqle", RDF.INQLE);
		persistableAsModel.setNsPrefix("xsd", RDF.XSD);
		FileOutputStream fos = null;
		fos = new FileOutputStream(fileName);
		persistableAsModel.write(fos, lang);
		
		try {
			fos.close();
		} catch (IOException e) {
			//do not close if unable
		}
		persistableAsModel.close();
	}

	/* *********************************************************************
	 * *** RECONSTITUTING METHODS
	 * ********************************************************************* */
	
	/**
	 * Reconstitute an object from Jena OntModel
	 * @param clazz the class of the object to reconstitute
	 * @param objectId the ID or relative URI of the object to reconstitute
	 * @param model which contains the object
	 * @param reconstituteMembers if true, do deep reconstitute 
	 * @return
	 */
	public static Object reconstitute(Class<?> clazz, String objectId, Model model, boolean reconstituteMembers) {
		//OntModel ontModel = ModelFactory.createOntologyModel();
		//ontModel.add(model);
		RDF2Bean reader = new RDF2Bean(model);
		
		//the object to create and return
		Object reconstitutedObj = null;
		try {
			if (reconstituteMembers) {
				log.debug("reader.loadDeep(" + clazz + ", " + objectId + ")");
				reconstitutedObj = reader.loadDeep(clazz, objectId);
				log.debug("Reconstituted " + JenabeanWriter.toString(reconstitutedObj));
			} else {
				log.debug("reader.load(" + clazz + ", " + objectId + ")");
				reconstitutedObj = reader.load(clazz, objectId);
			}
		} catch (Exception e) {
			//return null
			log.error("Error reconstituting object of class " + clazz + " with ID of " + objectId, e);
		}
		return reconstitutedObj;
	}
	
	public List<NamedModel> listNamedModels() {
		List<NamedModel> namedModels = new ArrayList<NamedModel>();
		namedModels.addAll((Collection<? extends NamedModel>) reconstituteAll(Dataset.class));
		namedModels.addAll((Collection<? extends NamedModel>) reconstituteAll(Datafile.class));
		return namedModels;
	}
	
	/**
	 * Retrieve a Collection of jenabeans from the metarepository
	 * @param clazz
	 * @param model
	 * @return
	 */
	public Collection<?> reconstituteAll(Class<?> clazz) {
		return reconstituteAll(clazz, getMetarepositoryModel());
	}
	
	/**
	 * Retrieve a Collection of jenabeans from the specified model
	 * @param clazz
	 * @param model
	 * @return
	 */
	public Collection<?> reconstituteAll(Class<?> clazz, Model model) {
		RDF2Bean loader = new RDF2Bean(model);
		Collection<?> objects = loader.loadDeep(clazz);
		//log.debug("Retrieved these Connections:" + connections);
		return objects;
	}
	

	
	/* *********************************************************************
	 * *** DELETING METHODS
	 * ********************************************************************* */
	/**
	 * Delete a database connection and all its statements, plus its representation in the metarepository
	 * 
	 * TODO if the model ever has member Objects, would need to call method which recurses these members
	 * TODO need to check for exceptions
	 */
	public boolean deleteConnection(Connection connection) {
		//first remove the connection reference from the metarepository
		log.debug("Removing connection: " + connection.getUri());
		Persister.remove(connection, getMetarepositoryModel());
		/*old way to remove:
		 * 
		 * Individual individualToRemove = getMetarepositoryModel().getIndividual(connectionUri);
		if (individualToRemove == null) {
			log.warn("Unable to find or remove connection:" + connectionUri);
			return false;
		}
		individualToRemove.remove();
		*/
		
		//try to delete the connection
		try {
			DBConnector connector = new DBConnector(connection);
			connector.deleteSDBStore();
			connector.close();
		} catch (Exception e) {
			log.error("Unable to delete Jena SDB store", e);
		}
		return true;
	}
	
	/**
	 * Delete a model and all its statements.
	 * @param successDeleting true if the SDB store was deleted
	 */
	public boolean deleteModel(NamedModel namedModel) {
		
		//first remove the connection reference from the metarepository
		log.debug("Removing NamedModel: " + namedModel.getUri());
		Persister.remove(namedModel, getMetarepositoryModel());
		
		if (namedModel instanceof Dataset) {			
			//remove the model
			DBConnector connector = new DBConnector(getConnection(((Dataset)namedModel).getConnectionId()));
			boolean successDeleting = connector.deleteSDBStore();
			//DBConnection jenaConnection = connector.getJenaConnection();
	    //ModelMaker maker = ModelFactory.createModelRDBMaker(jenaConnection);
	    //try {
				//maker.removeModel(namedModel.getModelName());
			//} catch (Exception e) {
				//log.error("Unable to delete dataset '" + namedModel.getModelName() + "'");
			//}
	    
	    //close
	    //maker.close();
	    //try {
				//jenaConnection.close();
			//} catch (SQLException e) {
				//do not close if unable
			//}
	    connector.close();
	    return successDeleting;
		} else if (namedModel instanceof Datafile) {
			String filePath = FileUtils.toFilename(((Datafile)namedModel).getFileUrl());
			//not necessary to remove statements: modelToDelete.removeAll();
			
			//delete the file
			File fileToDelete = new File(filePath);
			return fileToDelete.delete();
		}
		return false;
	}
	
	/**
	 * Delete the Resource from the model, plus all references to it
	 * 
	 * @param nodeUri the URI of the Object object to be removed
	 * @param model the Jena Model containing the object to remove
	 */
	public static void remove(Object objectToDelete, Model model) {
		Bean2RDF deleter = new Bean2RDF(model);
		deleter.delete(objectToDelete);
	}

	/* *********************************************************************
	 * *** OTHER METHODS
	 * ********************************************************************* */
	
	/**
	 * Close the  repository model and any other open models, which this persister is managing
	 */
	public void close() {
		if (metarepositoryModel != null && ! metarepositoryModel.isClosed()) {
			metarepositoryModel.close();
		}
	}
	
	/**
	 * Does a resource exist of the provided URI, in the provided model?
	 * @param uri
	 * @param model
	 * @return
	 */
	public static boolean resourceExists(String uri, Model model) {
		Resource resource = ResourceFactory.createResource(uri);
		return model.containsResource(resource);
	}
	
	/* *********************************************************************
	 * *** DEPRECATED METHODS
	 * ********************************************************************* */
	/**
	 * Given the ID of a NamedModel, get the Jena Model object
	 * @param namedModelUri
	 * @param clazz the java class of the NamedModel subclass
	 * @return the OntModel
	 * @deprecated use getModel(String namedModelId) instead
	 */
//	public OntModel getModel(String namedModelId, Class<?> clazz) {
//		NamedModel namedModel = (NamedModel)reconstitute(clazz, namedModelId, getMetarepositoryModel(), true);
//		return getOntModel(namedModel);
//	}

}



/**
 * Gets the Model containing all repositories to which this server is connected
 * TODO permit this to work w/ this repository info stored in file
 * @return
 *
public OntModel getRepositoryModel() {
	
	Connection repositoryConnection = null;
	NamedModel repositoryNamedModel = getAppInfo().getRepositoryNamedModel();
	
	if (repositoryNamedModel instanceof Dataset) {
		repositoryConnection = ((Dataset)repositoryNamedModel).getConnection();
	}
	//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
	DBConnector connector = new DBConnector(repositoryConnection);
	log.debug("getRepositoryModel(): getting model of name:" + repositoryNamedModel.getModelName());
	
	return connector.getModel(repositoryNamedModel.getModelName());
}

	
	public List<NamedModel> listNamedModels() {
		RDF2Bean loader = new RDF2Bean(persister.getRepositoryOntModel());
		List<NamedModel> namedModels = new ArrayList<NamedModel>();
		for (Class<?> clazz: Persister.MODEL_CLASSES) {
			List<? extends NamedModel> models = (List<? extends NamedModel>) loader.load(clazz);
			namedModels.addAll(models);
		}
		return namedModels;
	}
	
	public List<String> listNamedModelIds() {
		RDF2Bean loader = new RDF2Bean(persister.getRepositoryOntModel());
		List<String> namedModelUris = new ArrayList<String>();
		for (Class<?> clazz: Persister.MODEL_CLASSES) {
			List<? extends NamedModel> models = (List<? extends NamedModel>) loader.load(clazz);
			for (NamedModel model: models) {
				namedModelUris.add(model.getId());
			}
		}
		return namedModelUris;
	}
*/


/**
 * If Persister is ever to manage models other than the metarepository, need to add tracking of which of these is open
 * 
 private List<Model> openModels = new ArrayList<Model>();
 * 
 * Add a model which we are managing, to be closed whenever the close() method is called
private void addOpenModel(Model model) {
	if (model.equals(metarepositoryModel)) {
		return;
	}
	if (! openModels.contains(model)) {
		openModels.add(model);
	}
}

also need to add closing of all such models in the close() method like this
	for (Model model: openModels) {
		if (! model.isClosed()) {
			model.close();
		}
	}
*/