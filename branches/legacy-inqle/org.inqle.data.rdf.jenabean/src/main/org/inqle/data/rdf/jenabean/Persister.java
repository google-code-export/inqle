package org.inqle.data.rdf.jenabean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.DatabaseBackedJenamodel;
import org.inqle.data.rdf.jena.Datafile;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.PurposefulDatamodel;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jena.util.DatafileUtil;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.rdf.RDF;
import org.inqle.rdf.beans.IUniqueJenabean;

import thewebsemantic.Bean2RDF;
import thewebsemantic.NotFoundException;
import thewebsemantic.RDF2Bean;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.larq.IndexBuilderModel;
import com.hp.hpl.jena.query.larq.IndexBuilderString;
import com.hp.hpl.jena.query.larq.IndexBuilderSubject;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.util.FileManager;


/**
 * This class is of central importance to inqle.  It facilitates the following operations:
 * <ul><li>Storing Jenabean objects to a datamodel, or retrieving them from a datamodel</li>
 * <li>Creating new datamodels</li>
 * <li>Creating new databases</li>
 * <li>Retrieving the AppInfo object, where application-specific configuration info is stored</li>
 * </ul>
 * 
 * You can create a persister like this
 * <code>Persister persister = Persister.getInstance()</code>
 * @author David Donohue
 * December 5, 2007

 * @version 2.0: support many databases, each with its own "metarepository"
 */
public class Persister {
	
//	public static final String SYSTEM_PROPERTY_TEMP_DIR = "java.io.tmpdir";
	public static final String FILENAME_APPINFO = "assets/_private/AppInfo.ttl";
	public static final String TEMP_DIRECTORY = "assets/temp/";
	public static final Class<?>[] MODEL_CLASSES = {SystemDatamodel.class, PurposefulDatamodel.class, Datafile.class};
	
	public static final String EXTENSION_POINT_DATAMODEL = "org.inqle.data.datamodels";
	public static final String METAREPOSITORY_DATAMODEL = "_MetaRepository";

	public static final String EXTENSION_POINT_DATAMODEL_PURPOSES = "org.inqle.data.purposes";
	public static final String EXTENSION_DATAMODEL_PURPOSES_MINABLE_DATA = "org.inqle.data.purposes.minable";
	public static final String EXTENSION_DATAMODEL_PURPOSES = "org.inqle.data.purposes.schemas";
	
	public static final String ATTRIBUTE_CACHE_MODEL = "cacheInMemory";
	public static final String ATTRIBUTE_TEXT_INDEX_TYPE = "textIndexType";
	public static final String TEXT_INDEX_TYPE_SUBJECT = "subject";
	public static final String TEXT_INDEX_TYPE_LITERAL = "literal";
//	public static final String DATABASE_ROLE_ID_ATTRIBUTE = "targetDatabase";
	public static final String DATAMODEL_SUBJECT_CLASSES_CACHE = "_CacheSubjectClasses";
	public static final String DATAMODEL_ARCS_CACHE = "_CacheArcs";
	private AppInfo appInfo = null;
	private static Logger log = Logger.getLogger(Persister.class);
	public static int persisterId = 0;
	
	private Map<String, Map<String, Model>> modelCache = new HashMap<String, Map<String, Model>>();
		
	private Map<String, IndexBuilderModel> indexBuilders;
	private Model prefixesModel;
	private boolean persisterInitialized = false;
	
	
	/* *********************************************************************
	 * *** FACTORY METHODS
	 * ********************************************************************* */
	private Persister() {
		//ensure directories exist
		InqleInfo.createNeededDirectories();
	}
	
	/**
	* PersisterHolder is loaded on the first execution of Persister.getInstance() 
	* or the first access to PersisterHolder, not before.
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

	/* *********************************************************************
	 * *** INITIALIZATION METHODS
	 * ********************************************************************* */
//	private void initialize() {
//		if (! persisterInitialized) {
//			initializeIndexBuilders();
//			persisterInitialized = true;
////			initializeSystemDatamodels();
//		}
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
			loadedAppInfo = (AppInfo)reader.loadDeep(AppInfo.class, AppInfo.APPINFO_INSTANCE_ID);
			log.trace("Retrieved appInfo:" + JenabeanWriter.toString(loadedAppInfo));
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
//		return new File(System.getProperty(SYSTEM_PROPERTY_TEMP_DIR));
		return new File(InqleInfo.getTempDirectory());
	}
	
	/**
	 * Get the Jena model containing the AppInfo object
	 * @return
	 */
	private static Model getAppInfoModel() {
		Model appInfoModel = null;
		try {
			appInfoModel = ModelFactory.createDefaultModel();
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
	 * Creates a new data Model, 
	 * given a database ID and the name of a model.
	 * 
	 * @param databaseId
	 * @param dbModelName
	 * @return
	 */
	public static void createModel(String modelType, String databaseId, String dbModelName) {
		IDBConnector dbConnector = DBConnectorFactory.getDBConnector(databaseId);

		log.debug("Creating Model of name type: " + modelType + "and name: " + dbModelName);
		
		dbConnector.getModel(modelType, dbModelName);
	}
	
	/**
	 * Creates a new database-backed model, given a DatabaseBackedDatamodel.
	 * Also stores the new datamodel in the metarepository
	 * @version 2
	 */
	public <T extends DatabaseBackedJenamodel> void createDatabaseBackedModel(T datamodel) {
		
		//see if a datamodel of that ID already exists
		if (modelExists(datamodel.getId())) {
			log.info("A Datamodel of ID: " + datamodel.getId() + " already exists.");
			return;
		}
		
		//create the database-backed model
		IDBConnector dbConnector = DBConnectorFactory.getDBConnector(datamodel.getDatabaseId());
		log.info("Creating Model of name '" + datamodel.getName() + "'...");
		Model model = dbConnector.getModel(datamodel.getModelType(), datamodel.getName());
		getModelCache(datamodel.getModelType()).put(datamodel.getId(), model);
		log.info("Created and cached Model of name '" + datamodel.getId() + "'...");
		
//		persist(datamodel, getTargetDatamodelId(datamodel.getClass(), datamodel.getDatabaseId()));
		persist(datamodel, getMetarepositoryModel(datamodel.getDatabaseId()));
		log.info("Persisted datamodel: " + datamodel.getId());
		return;
	}
	
	private Map<String, Model> getModelCache(String modelType) {
		Map<String, Model> targetModelCache = modelCache.get(modelType);
		if (targetModelCache==null) {
			targetModelCache = new HashMap<String, Model>();
			modelCache.put(modelType, targetModelCache);
		}
		return targetModelCache;
	}

	/**
	 * Given the ID of DatabaseBackedDatamodel, get the Jena Model object.  
	 * This ID is in the format databse_id/datamodel_name
	 * First check the cached models
	 * then load the model from the database.
	 * If the model and/or the database foes not exist, create it
	 * Best practice to never close the model.
	 * @param datamodelId the id of the DatabaseBackedDatamodel object, 
	 * @return the model, or null if no model found
	 * 
	 * TODO consider memory impact if we cache thousands of models
	 */
//	public Model getDatabaseBackedModel(String datamodelId) {
	public Model getModel(String datamodelId) {
		String modelType = DatabaseBackedJenamodel.getModelTypeFromDatamodelId(datamodelId);
		Model cachedModel = getModelCache(modelType).get(datamodelId);
		if (cachedModel != null) {
			return cachedModel;
		}
//		log.info("Getting model: " + datamodelId);
		String databaseId = DatabaseBackedJenamodel.getDatabaseIdFromDatamodelId(datamodelId);
		if (databaseId==null || databaseId.length()==0 || databaseId.equals("null")) {
			log.error("databaseId is " + databaseId);
			return null;
		}
		String modelName = DatabaseBackedJenamodel.getModelNameFromDatamodelId(datamodelId);
		IDBConnector connector = DBConnectorFactory.getDBConnector(databaseId);
		Model model = connector.getModel(modelType, modelName);
		getModelCache(modelType).put(datamodelId, model);
		return model;
	}
	
//	public String getCoreDatamodelId(String datamodelName) {
//		return CORE_DATABASE_ID + "/" + datamodelName;
//	}
	
//	/**
//	 * Given the URI or ID of a Datamodel in the System database, get the Jena Model object.
//	 * 
//	 * Best practice is to NOT close() the model after use.
//	 * @param datamodelId the role id of the internal Datamodel
//	 * @return the model, or null if no model found in the metarepository
//	 */
//	@Deprecated
//	public Model getSystemModel(String datamodelId) {
//		Model systemModel = cachedModels.get(datamodelId);
//		if (systemModel == null) {
//			IDBConnector connector = DBConnectorFactory.getDBConnector(InqleInfo.SYSTEM_DATABASE_ID);
//			systemModel = connector.getModel(Persister.METAREPOSITORY_DATAMODEL);
//			cachedModels.put(datamodelId, systemModel);
//		}
//		return systemModel;
//	}
	
	@Deprecated
	public SystemDatamodel getSystemDatamodel(String datamodelId) {
		SystemDatamodel datamodel = reconstitute(SystemDatamodel.class, datamodelId, getModel(datamodelId), true);
		return datamodel;
	}
	
//	@Deprecated
//	public void initializeSystemDatamodels() {
//		//get all system datamodel extensions
//		List<IExtensionSpec> datamodelExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATAMODEL);
//		
//		//find or create the Datamodel for each.
//		for (IExtensionSpec datamodelExtension: datamodelExtensions) {
//			String datamodelName = datamodelExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
//
//			//if the model is already cached, do not recreate it
//			if (cachedModels.containsKey(datamodelName)) {
//				continue;
//			}
//			
//			//create the Datamodel
//			SystemDatamodel systemDatamodel = new SystemDatamodel();
//			systemDatamodel.setName(datamodelName);
//			systemDatamodel.setDatabaseId(InqleInfo.SYSTEM_DATABASE_ID);
//			createDatabaseBackedModel(systemDatamodel);
//			log.info("Created & stored new SystemDatamodel of ID: " + datamodelName + ":\n" + JenabeanWriter.toString(systemDatamodel));
//		}
//		
//		//having created the Datamodels and Models, make sure any text indexes have been created
//		initializeIndexBuilders();
//		
//		log.info("System datamodels initialized");
//		systemDatamodelsInitialized  = true;
//	}
	
	/**
	 * Flush any Lucene text indexes for the Datamodel
	 */
	public void flushIndexes(Jenamodel namedModel) {
		if (namedModel instanceof SystemDatamodel) {
			SystemDatamodel systemDatamodel = (SystemDatamodel) namedModel;
//			String datamodelRole = internalDatamodel.getDatamodelRole();
			String datamodelId = systemDatamodel.getId();
			IndexBuilderModel builder = getIndexBuilder(datamodelId);
			if (builder != null) {
				//log.info("Flushing index builder: " + builder + " for datamodel role:" + datamodelRole + "...");
				builder.flushWriter();
			}
		} else if (namedModel instanceof PurposefulDatamodel) {
			PurposefulDatamodel userDatamodel = (PurposefulDatamodel) namedModel;
			Collection<String> purposes = userDatamodel.getDatamodelPurposes();
			if (purposes != null) {
				for (String purpose: purposes) {
					IndexBuilderModel builder = getIndexBuilder(purpose);
					if (builder == null) continue;
					//log.info("Flushing index builder: " + builder + " for purpose:" + purpose + "...");
					builder.flushWriter();
				}
			}
		}
	}
	
	public Model getPrefixesModel() {
		if (prefixesModel == null) {
			prefixesModel = DatafileUtil.getModel(InqleInfo.getUriPrefixesDirectory());
		}
		return prefixesModel;
	}

	/**
	 * Given a Datamodel, retrieves a Model which has
	 * had its text indexers registered
	 * @param indexableDatamodel
	 * @return
	 */
	public Model getIndexableModel(Jenamodel indexableDatamodel) {
		if (indexableDatamodel==null) return null;
		Model model = getModel(indexableDatamodel.getId());
		log.info("PERSISTER: GIGIGIGIGIGIGIGIGIGIGIGI Got indexable model of id:" + indexableDatamodel.getId());
		log.info("PERSISTER: ...which has " + model.size() + " statements.");
		if (indexableDatamodel instanceof PurposefulDatamodel) {
			PurposefulDatamodel userDatamodel = (PurposefulDatamodel)indexableDatamodel;
			Collection<String> purposes = userDatamodel.getDatamodelPurposes();
			if (purposes != null) {
				for (String purpose: purposes) {
					//TODO make new IndexBuilder subclass which allows indexing of skos:altLabel
					IndexBuilderModel builder = getIndexBuilder(purpose);
					if (builder == null) continue;
					log.info("Registering index builder: " + builder + " for purpose:" + purpose);
					model.register(builder);
				}
			}
		} else if (indexableDatamodel instanceof SystemDatamodel) {
			SystemDatamodel systemDatamodel = (SystemDatamodel)indexableDatamodel;
			IndexBuilderModel builder = getIndexBuilder(systemDatamodel.getId());
			log.info("Got IndexBuilder: " + builder);
			if (builder != null) {
				model.register(builder);
			}
		}
		return model;
	}
	
	/**
	 * Given the ID of a model, retrieves a Model which has
	 * had its text indexers registered.
	 * @param modelId
	 * @return
	 */
	public Model getIndexableModel(String modelId) {
		//see if it is a purposeful model
		PurposefulDatamodel purposefulDatamodel = getDatabaseBackedDatamodel(PurposefulDatamodel.class, modelId);
		if (purposefulDatamodel != null) return getIndexableModel(purposefulDatamodel);
		//otherwise it is a system model
		SystemDatamodel systemDatamodel = new SystemDatamodel();
		systemDatamodel.setId(modelId);
		return getIndexableModel(systemDatamodel);
	}
	
	
	/**
	 * Loads an RDF file from local filesystem or remote (HTTP) source
	 * @param filePath the file path or URL
	 * @return the resulting Model
	 */
	public static Model getModelFromFile(String filePath) {
		try {
			Model model = FileManager.get().loadModel( filePath );
			return model;
		} catch (Exception e) {
			log.error("Unable to retrieve model from " + filePath + ".  It may not exist.", e);
			return null;
		}
	}
	
	/**
	 * Does this server already have a datamodel of the provided ID?
	 * @param datamodelId
	 * @return
	 */
	public boolean modelExists(String datamodelId) {
		String databaseId = DatabaseBackedJenamodel.getDatabaseIdFromDatamodelId(datamodelId);
		String modelType = DatabaseBackedJenamodel.getModelTypeFromDatamodelId(datamodelId);
		String modelName = DatabaseBackedJenamodel.getModelNameFromDatamodelId(datamodelId);
		IDBConnector connector = DBConnectorFactory.getDBConnector(databaseId);
		return connector.modelExists(modelType, modelName);
	}
	
	/* *********************************************************************
	 * *** TEXT INDEX METHODS
	 * ********************************************************************* */
	/**
	 * Get the IndexBuilder for the indexId.
	 * @param indexBuilderKey either the id of the SystemDatamodel role 
	 * or the UserDatamodel purpose
	 * @return
	 */
	public IndexBuilderModel getIndexBuilder(String indexId) {
		indexId = sanitizeIndexId(indexId);
		IndexBuilderModel indexBuilder = getIndexBuilders().get(indexId);
		if (indexBuilder!=null) return indexBuilder;
//		initializeIndexBuilders();
		indexBuilder = attachIndexBuilder(indexId);
		indexBuilders.put(indexId, indexBuilder);
//		indexBuilder = indexBuilders.get(indexId);
		return indexBuilder;
	}
	
	public IndexLARQ getIndex(String indexId) {
		indexId = sanitizeIndexId(indexId);
		IndexBuilderModel indexBuilder = getIndexBuilder(indexId);
		if (indexBuilder==null) return null;
		indexBuilder.flushWriter();
		log.trace("Retrieved & flushed IndexBuilder:" + indexBuilder);
		return indexBuilder.getIndex();
	}
	
	/**
	 * Create or attach to an index builder, using the default index type (subjects)
	 * @param indexId
	 * @param textIndexType
	 * @return
	 */
	public IndexBuilderModel attachIndexBuilder(String indexId) {
		return attachIndexBuilder(indexId, TEXT_INDEX_TYPE_SUBJECT);
	}
	
	/**
	 * Create or attach to an index builder
	 * @param indexId
	 * @param textIndexType
	 * @return
	 */
	public IndexBuilderModel attachIndexBuilder(String indexId, String textIndexType) {
		indexId = sanitizeIndexId(indexId);
		String indexFilePath = InqleInfo.getRdfDirectory() + InqleInfo.INDEXES_FOLDER + "/" + indexId;
		//if possible, retrieve the Lucene IndexWriter, such that existing index can be used
		
		//first unlock the directory, if locked
		try {
			if (IndexReader.isLocked(indexFilePath)) {
				log.info("Index is locked.  Unlocking...");
				FSDirectory dir = FSDirectory.getDirectory(indexFilePath);
				IndexReader.unlock(dir);
			}
		} catch (Exception e) {
			log.error("Unable to test or unlock the Lucene index. Skipping this step.", e);
		}
		
		IndexWriter indexWriter = null;
		try {
			log.info("Attaching or creating index: " + indexId);
			indexWriter = new IndexWriter(indexFilePath, new StandardAnalyzer());
		} catch (Exception e) {
			log.error("Unable to connect to existing Lucene index or to create new Lucene index", e);
		}
		
		textIndexType = textIndexType.toLowerCase();
		IndexBuilderModel larqBuilder = null;
		
//		Model internalModel = getSystemModel(datamodelId);
//		log.info("got internalmodel for " + datamodelId + ".  Is null?" + (internalModel==null));
		if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_SUBJECT)) {
			larqBuilder = new IndexBuilderSubject(indexWriter);
			
		} else if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_LITERAL)) {
//			larqBuilder = new IndexBuilderString(indexFilePath);
			larqBuilder = new IndexBuilderString(indexWriter);
		}
		return larqBuilder;
	}
	

	private String sanitizeIndexId(String indexId) {
		if (indexId.indexOf("/") < 0) return indexId;
		String newIndexId = indexId.replaceAll("/", "--");
		return newIndexId;
	}

	public Map<String, IndexBuilderModel> getIndexBuilders() {
		if (indexBuilders!=null && indexBuilders.size() > 0) {
			return indexBuilders;
		}
		indexBuilders = new HashMap<String, IndexBuilderModel>();
		
//		//loop thru system datamodels extensions, and create each index
//		List<IExtensionSpec> datamodelExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATAMODEL);
//		for (IExtensionSpec datamodelExtension: datamodelExtensions) {
//			log.trace("datamodelExtension=" + datamodelExtension);
//			String datamodelId = datamodelExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
//			String textIndexType = datamodelExtension.getAttribute(ATTRIBUTE_TEXT_INDEX_TYPE);
//			
//			//if directed to do so, build & store an index for this Model
//			if (textIndexType != null) {
//				log.info("Creating IndexBuilder for datamodel of ID or purpose: " + datamodelId + "; textIndexType=" + textIndexType);
//
//				IndexBuilderModel larqBuilder = getIndexBuilder(datamodelId, textIndexType);
//				if (larqBuilder != null) {
//					log.info("Retrieving Index for Datamodel of ID: " + datamodelId + "...");
//					if (larqBuilder.getIndex() == null) {
//						log.warn("No text index exists for datamodel role " + datamodelId);
//					}
//					indexBuilders.put(datamodelId, larqBuilder);
//				}
//				
//			}
//		}
		
		//add any external datamodel purposes which are supposed to be indexed
		List<IExtensionSpec> datamodelPurposeExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATAMODEL_PURPOSES);
		for (IExtensionSpec datamodelPurposeExtension: datamodelPurposeExtensions) {
			
			String datamodelPurposeId = datamodelPurposeExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
			String textIndexType = datamodelPurposeExtension.getAttribute(ATTRIBUTE_TEXT_INDEX_TYPE);
			
//			log.info("FFFFFFFFFFFFFFFFFFdatamodelPurposeId=" + datamodelPurposeId + "; textIndexType=" + textIndexType);
			//if directed to do so, build & store an index for this Model
			if (textIndexType != null) {
				IndexBuilderModel larqBuilder = attachIndexBuilder(datamodelPurposeId, textIndexType);

				if (larqBuilder != null) {
					IndexLARQ theIndex = null;
					try {
						theIndex = larqBuilder.getIndex();
					} catch (Exception e) {
						log.info("Unable to retrieve index for datamodel purpose " + datamodelPurposeId);
					}
					if (theIndex == null) {
						log.warn("No text index exists yet for datamodel purpose " + datamodelPurposeId + "Proceeding w/ blank index.");
					}
					indexBuilders.put(datamodelPurposeId, larqBuilder);
					log.info("Added indexbuilder for purpose " + datamodelPurposeId);
				}
			}
		}
		
		log.trace("assembled list of index builders:" + indexBuilders);
		return indexBuilders;
	}
	
	/* *********************************************************************
	 * *** INTERNAL MODEL METHODS
	 * ********************************************************************* */
	
//	public OntModel getMetarepositoryModel() {
//		if (metarepositoryModel != null) {
//			//log.info("#" + persisterId + ":getRepositoryModel(): return saved metarepository");
//			return this.metarepositoryModel;
//		}
//		//log.info("#" + persisterId + ":getRepositoryModel(): get new metarepository");
//		Datamodel metarepositoryRDBModel = getAppInfo().getMetarepositoryDatamodel();
//		SDBDatabase metarepositoryConnection = getAppInfo().getDefaultInternalConnection();
//		
//		//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
//		SDBConnector connector = new SDBConnector(metarepositoryConnection);
//		//log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + repositoryDatamodel.getModelName());
//		log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + metarepositoryConnection.getId());
//
//		//this.metarepositoryModel = connector.getOntModel(repositoryDatamodel.getModelName());
//		this.metarepositoryModel = connector.getMemoryOntModel(metarepositoryRDBModel.getId());
//		return this.metarepositoryModel;
//	}
	
	/**
	 * Get the model for storing info about the repositories, within the specified database
	 */
	public Model getMetarepositoryModel(String databaseId) {
		return getModel(RDF.getDatamodelId(databaseId, RDF.SUBDATABASE_SYSTEM, METAREPOSITORY_DATAMODEL));
	}
	
	
	/* *********************************************************************
	 * *** CONNECTION METHODS
	 * ********************************************************************* */
	
//	public SDBDatabase getConnection(String connectionId) {
//		Object connectionObj = Persister.reconstitute(SDBDatabase.class, connectionId, getMetarepositoryModel(), true);
//		return (SDBDatabase)connectionObj;
//	}

	/**
	 * Store a new IDBDatabase object in the metarepository and
	 * create the DB store in the actual database.
	 * @param testConnectionInfo
	 */
	public <T extends IDatabase> boolean createNewDatabase(T database) {
		log.info("Will try to create a new Database:\n" + JenabeanWriter.toString(database));
		//first create the IDBConnector and use it to create the DB store in the database
		IDBConnector connector = DBConnectorFactory.getDBConnector(database.getId());
		boolean success = connector.createDatabase();
		log.info("Tried to create new DB store, with success?" + success);
		
		//next register the new DB in the repositories namedModel, within this new database
		//TODO: when deleting works, remove the below " || status == SDBConnector.STORE_IS_BLANK"
		if (success) {
			persist(database, RDF.getTargetDatamodelId(database.getClass(), database.getId()));
		}
		
		return success;
	}
	
	/* *********************************************************************
	 * *** DATAMODEL METHODS
	 * ********************************************************************* */
	
	/**
	 * Get the datamodel object
	 */
	public <T extends DatabaseBackedJenamodel> T getDatabaseBackedDatamodel(Class<T> clazz, String datamodelId) {
		String databaseId = DatabaseBackedJenamodel.getDatabaseIdFromDatamodelId(datamodelId);
		Model metarepository = getMetarepositoryModel(databaseId);
		T datamodel = reconstitute(clazz, datamodelId, metarepository, true);
		return datamodel;
	}
	
	/**
	 * List all model names for a given database and type
	 * @param databaseId
	 * @return
	 */
	public List<String> listModelNames(String databaseId, String modelType) {
		IDBConnector connector = DBConnectorFactory.getDBConnector(databaseId);
		return connector.listModelNames(modelType);
	}
	
	/* *********************************************************************
	 * *** TARGET ANNOTATION METHODS
	 * ********************************************************************* */
	
	/**
	 * Persist a Object object to an SDB store as RDF
	 * @param persistableObj the Jenabean object
	 * @param model the Jena model into which to persist
	 * @param persistMembers if true, all Object members (and their members, recursively)
	 * will be persisted.  If false, they will not.
	 */
	public void persist(Object persistableObj, Model model, boolean persistMembers) {
//		log.trace("Persister.persist():" + JenabeanWriter.toString(persistableObj));
		if (persistableObj instanceof IUniqueJenabean) {
			((IUniqueJenabean)persistableObj).setUpdateDate(new Date());
		}
//		log.info("Persisting...");
		Bean2RDF writer = new Bean2RDF(model);
		if (persistMembers) {
			writer.saveDeep(persistableObj);
		} else {
			writer.save(persistableObj);
		}
//		model.close();
		model.commit();
		//log.info("Saved");
	}

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
	 * Persist a Jenabean object to the datamodel of ID
	 * @param persistableObj
	 * @param datamodelId
	 */
	public void persist(Object persistableObj, String datamodelId) {
		persist(persistableObj, getModel(datamodelId));
	}
	
	/**
	 * Persist a Jenabean object to the default target datamodel.
	 * @param <T>
	 * @param persistableObj
	 */
	public <T> void persist(T persistableObj) {
		String datamodelId = RDF.getTargetDatamodelId(persistableObj.getClass());
		persist(persistableObj, datamodelId);
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
		File file = new File(fileName);
		File parentFile = file.getParentFile();
		parentFile.mkdirs();
		
		OntModel persistableAsModel = ModelFactory.createOntologyModel();
		Bean2RDF converter = new Bean2RDF(persistableAsModel);
		converter.save(persistableObj);
		//the statements to add
		
		log.info("Saving model as file:" + JenabeanWriter.modelToString(persistableAsModel));
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
//	/**
//	 * reconstitue the object of the specified class, from the specified datamodel
//	 * If not annotation is present, return null
//	 */
//	public <T> T reconstitute(String datamodelId, Class<T> persistedClass, String objectId, boolean reconstituteMembers) {
//		String targetDatamodelName = getTargetDatamodelName(persistedClass);
//		if (targetDatamodelName==null) {
//			log.warn("Unable to reconsitute object of ID " + objectId + ".  Its class " + persistedClass + " has no TargetDatamodel annotation.");
//			return null;
//		}
//		Model targetModel = getModel(targetDatabaseId + "/" + targetDatamodelName);
//		Model targetModel = getModel(datamodelId);
//		return Persister.reconstitute(persistedClass, objectId, targetModel, reconstituteMembers);
//	}
	
	/**
	 * tell whether the Jenabean exists
	 * @param clazz the class of the object
	 * @param objectId the ID or relative URI of the object to test
	 * @param model which contains the object
	 * @param reconstituteMembers if true, do deep reconstitute 
	 * @return
	 */
	public static boolean exists(Class<?> clazz, String objectId, Model model) {
		RDF2Bean reader = new RDF2Bean(model);
		return reader.exists(clazz, objectId);
	}
	
	/**
	 * Reconstitute an object from Jena Model
	 * @param clazz the class of the object to reconstitute
	 * @param objectId the ID or relative URI of the object to reconstitute
	 * @param model which contains the object
	 * @param reconstituteMembers if true, do deep reconstitute 
	 * @return
	 */
	public static <T> T reconstitute(Class<T> clazz, String objectId, Model model, boolean reconstituteMembers) {
		//OntModel ontModel = ModelFactory.createOntologyModel();
		//ontModel.add(model);
		if (model==null) return null;
		RDF2Bean reader = new RDF2Bean(model);
		
		//the object to create and return
		T reconstitutedObj = null;
		try {
			if (reconstituteMembers) {
				log.debug("reader.loadDeep(" + clazz + ", " + objectId + ")");
				reconstitutedObj = reader.loadDeep(clazz, objectId);
//				log.debug("Reconstituted " + JenabeanWriter.toString(reconstitutedObj));
			} else {
				log.debug("reader.load(" + clazz + ", " + objectId + ")");
				reconstitutedObj = reader.load(clazz, objectId);
			}
		} catch (Exception e) {
			//unable to reconstitute: return null
			log.debug("Error reconstituting object of class " + clazz + " with ID of " + objectId, e);
		}
		return reconstitutedObj;
	}
	
	/**
	 * Reconstitute an object from Jena Model
	 * @param clazz the class of the object to reconstitute
	 * @param objectId the ID or relative URI of the object to reconstitute
	 * @param datamodelId which contains the object
	 * @param reconstituteMembers if true, do deep reconstitute 
	 * @return
	 */
	public <T> T reconstitute(Class<T> clazz, String objectId, String datamodelId, boolean reconstituteMembers) {
		return reconstitute(clazz, objectId, getModel(datamodelId), reconstituteMembers);
	}
	
	/**
	 * Reconstitute an object from its target Jena model.  This only works when the class
	 * specifies both TargetDatabaseId and TargetDatamodelName annotations.
	 * @param <T>
	 * @param clazz
	 * @param objectId
	 * @param model
	 * @param reconstituteMembers
	 * @return
	 */
	public <T> T reconstitute(Class<T> persistableClass, String objectId, boolean reconstituteMembers) {
		String datamodelId = RDF.getTargetDatamodelId(persistableClass);
		return reconstitute(persistableClass, objectId, datamodelId, reconstituteMembers);
	}
	
	/**
	 * Retrieve a Collection of jenabeans from the appropriate system datamodel,
	 * as specified in the TargetDatabase and TargetDatamodel annotations of the persistableClass
	 * @param <T>
	 * @param persistableClass the class to reconstitute
	 * @return a Collection of objects of that class, or null if the class does not have a 
	 * TargetDatamodel defined
	 * 
	 * @version 2
	 */
	public <T> Collection<T> reconstituteAll(Class<T> persistableClass) {
		String databaseId = RDF.getTargetDatabaseId(persistableClass);
		return reconstituteAll(databaseId, persistableClass);
	}
	
	/**
	 * Retrieve a Collection of jenabeans from the appropriate system datamodel,
	 * as specified in the TargetDatamodel annotation of the persistableClass
	 * @param <T>
	 * @param persistableClass the class to reconstitute
	 * @return a Collection of objects of that class, or null if the class does not have a 
	 * TargetDatamodel defined
	 * 
	 * @version 2
	 */
	public <T> Collection<T> reconstituteAll(String databaseId, Class<T> persistableClass) {
		String modelName = RDF.getTargetModelName(persistableClass);
		String modelType = RDF.getTargetModelType(persistableClass);
		if (modelName == null) {
			return null;
		}
		Model model = getModel(RDF.getDatamodelId(databaseId, modelType, modelName));
		Collection<T> results = reconstituteAll(persistableClass, model);
		return results;
	}
	
	/**
	 * Retrieve a Collection of jenabeans from the specified model
	 * @param clazz
	 * @param model
	 * @return
	 * 
	 * @version 2
	 */
	public <T> Collection<T> reconstituteAll(Class<T> clazz, Model model) {
		RDF2Bean loader = new RDF2Bean(model);
		Collection<T> objects = loader.loadDeep(clazz);
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
	 * 
	 * @version 2
	 */
	public boolean deleteDatabase(IDatabase database) {
		//first remove the connection reference from the metarepository
		log.debug("Removing connection: " + database.getId());
		Model metarepositoryModel = getMetarepositoryModel(database.getId());
		Persister.remove(database, metarepositoryModel);
//		metarepositoryModel.close();
		//try to delete the connection
		try {
//			SDBConnector connector = new SDBConnector(connection);
			IDBConnector connector = DBConnectorFactory.getDBConnector(database.getId());
			connector.deleteDatabase();
			connector.close();
		} catch (Exception e) {
			log.error("Unable to delete Jena SDB store", e);
		}
		return true;
	}
	
	/**
	 * Delete all statements from a model.
	 * @return successClearing true if the store was emptied
	 */
	public boolean emptyModel(Jenamodel namedModel) {
		
		//remove all statements and all index info for the Datamodel
		Model modelToBeDeleted = getIndexableModel(namedModel);

		modelToBeDeleted.removeAll();
//		modelToBeDeleted.close();
//		DonohueUtil.removeAllStatements(modelToBeDeleted, (Resource)null, (Property)null, (RDFNode)null);
		return true;
	}
	
//	/**
//	 * Delete a model and all its statements.
//	 * @return successDeleting true if the SDB store was deleted
//	 * TODO this does not work in TDB
//	 */
//	public boolean deleteModel(Datamodel datamodel) {
//		
//		boolean successDeleting = false;
//		if (datamodel instanceof DatabaseBackedDatamodel) {		
//			
//			//remove the model
//			DatabaseBackedDatamodel dbDatamodel = (DatabaseBackedDatamodel)datamodel;
//			IDBConnector connector = DBConnectorFactory.getDBConnector(dbDatamodel.getDatabaseId());
//			
//			successDeleting = connector.deleteModel(datamodel.getId());
//			log.info("Success deleting the DB model? " + successDeleting);
//	    connector.close();
//	    
//	    if (successDeleting) {
//	    	//invalidate the cache
//	    	CacheTool.invalidateDataCache(dbDatamodel.getId());
//	    	
//	    	if (cachedModels.containsKey(datamodel.getId())) {
//	    		cachedModels.remove(datamodel.getId());
//	    	}
//	    }
//	    
//	    return successDeleting;
//		} else if (datamodel instanceof Datafile) {
//			Model modelToDelete = getModel(datamodel);
//			modelToDelete.removeAll();
////			DonohueUtil.removeAllStatements(modelToDelete, (Resource)null, (Property)null, (RDFNode)null);
//			modelToDelete.close();
//			//delete the file
//			String filePath = FileUtils.toFilename(((Datafile)datamodel).getFileUrl());
//			File fileToDelete = new File(filePath);
//			successDeleting = fileToDelete.delete();
//		}
//		if (successDeleting) {
//			Model metarepositoryModel = getMetarepositoryModel(datamodel.getId());
//			Persister.remove(datamodel, metarepositoryModel);
////			metarepositoryModel.close();
//		}
//		return successDeleting;
//	}
	
//	/**
//	 * Delete the Resource from the database, plus all references to it
//	 * 
//	 */
//	public void remove(String datamodelId, Object objectToDelete) {
//		String targetDatamodelName = getTargetDatamodelName(objectToDelete);
//		if (targetDatamodelName==null) {
//			log.warn("Unable to delete object " + objectToDelete + ".  Its class has no TargetDatamodel annotation.");
//			return;
//		}
//		Model model = getModel(databaseId + "/" + targetDatamodelName);
//		remove(objectToDelete, model);
//		model.begin();
//		Bean2RDF deleter = new Bean2RDF(model);
//		deleter.delete(objectToDelete);
//		model.commit();
//	}
	
	/**
	 * Delete the Resource from the model, plus all references to it
	 * 
	 * @param objectToDelete the object to be removed
	 * @param model the Jena Model containing the object to remove
	 */
	public static void remove(Object objectToDelete, Model model) {
		log.info("Before delete: model has " + model.size() + " statements.");
		model.begin();
		Bean2RDF deleter = new Bean2RDF(model);
		deleter.delete(objectToDelete);
		model.commit();
		log.info("After delete: model has " + model.size() + " statements.");
	}
	
	/**
	 * Delete the Resource from the model, plus all references to it
	 * 
	 * @param objectToDelete the object to be removed
	 * @param modelId the ID of the datamodelJena Model containing the object to remove
	 */
	public void remove(Object objectToDelete, String datamodelId) {
		remove(objectToDelete, getModel(datamodelId));
	}
	
	/**
	 * Delete the Resource from the default target datamodel, plus all references to it
	 * This can only be used for Jenabean classes that have both the TargetDatabaseId and 
	 * TargetDatamodelName annotation
	 * 
	 * @param objectToDelete the object to be removed
	 * @param modelId the ID of the datamodelJena Model containing the object to remove
	 */
	public <T> void remove(T objectToDelete) {
		String datamodelId = RDF.getTargetDatamodelId(objectToDelete.getClass());
		remove(objectToDelete, getModel(datamodelId));
	}

	/* *********************************************************************
	 * *** OTHER METHODS
	 * ********************************************************************* */
	
	/**
	 * Close the  repository model and any other open models, which this persister is managing
	 */
//	public void close() {
//		if (metarepositoryModel != null && ! metarepositoryModel.isClosed()) {
//			metarepositoryModel.close();
//		}
//	}
	
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

//	public void registerInternalConnection(InternalConnection aConnection) {
//		persist(aConnection);
//		internalDatabases.put(aConnection.getConnectionRole(), aConnection);
//		
//	}

	
	/* *********************************************************************
	 * *** DEPRECATED METHODS
	 * ********************************************************************* */
	/**
	 * Given the ID of a Datamodel, get the Jena Model object
	 * @param namedModelUri
	 * @param clazz the java class of the Datamodel subclass
	 * @return the OntModel
	 * @deprecated use getModel(String namedModelId) instead
	 */
//	public OntModel getModel(String namedModelId, Class<?> clazz) {
//		Datamodel namedModel = (Datamodel)reconstitute(clazz, namedModelId, getMetarepositoryModel(), true);
//		return getOntModel(namedModel);
//	}

}



/**
 * Gets the Model containing all repositories to which this server is connected
 * TODO permit this to work w/ this repository info stored in file
 * @return
 *
public OntModel getRepositoryModel() {
	
	SDBDatabase repositoryConnection = null;
	Datamodel repositoryDatamodel = getAppInfo().getRepositoryDatamodel();
	
	if (repositoryDatamodel instanceof Datamodel) {
		repositoryConnection = ((Datamodel)repositoryDatamodel).getConnection();
	}
	//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
	SDBConnector connector = new SDBConnector(repositoryConnection);
	log.debug("getRepositoryModel(): getting model of name:" + repositoryDatamodel.getModelName());
	
	return connector.getModel(repositoryDatamodel.getModelName());
}

	
	public List<Datamodel> listDatamodels() {
		RDF2Bean loader = new RDF2Bean(persister.getRepositoryOntModel());
		List<Datamodel> namedModels = new ArrayList<Datamodel>();
		for (Class<?> clazz: Persister.MODEL_CLASSES) {
			List<? extends Datamodel> models = (List<? extends Datamodel>) loader.load(clazz);
			namedModels.addAll(models);
		}
		return namedModels;
	}
	
	public List<String> listDatamodelIds() {
		RDF2Bean loader = new RDF2Bean(persister.getRepositoryOntModel());
		List<String> namedModelUris = new ArrayList<String>();
		for (Class<?> clazz: Persister.MODEL_CLASSES) {
			List<? extends Datamodel> models = (List<? extends Datamodel>) loader.load(clazz);
			for (Datamodel model: models) {
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