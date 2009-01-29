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
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.DatabaseBackedDatamodel;
import org.inqle.data.rdf.jena.Datafile;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jena.UserDatamodel;
import org.inqle.data.rdf.jena.util.DatafileUtil;
import org.inqle.data.rdf.jenabean.cache.CacheTool;

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
import com.hp.hpl.jena.util.FileUtils;


/**
 * This class is of central importance to inqle.  It facilitates the following operations:
 * <ul><li>Storing Jenabean objects to a dataset, or retrieving them from a dataset</li>
 * <li>Creating new datasets</li>
 * <li>Retrieving the AppInfo object, where application-specific configuration info is stored</li>
 * </ul>
 * 
 * You can create a persister like this
 * <code>Persister persister = Persister.getInstance()</code>
 * @author David Donohue
 * December 5, 2007

 */
public class Persister {
	
	public static final String SYSTEM_PROPERTY_TEMP_DIR = "java.io.tmpdir";
	public static final String FILENAME_APPINFO = "assets/_private/AppInfo.ttl";
	public static final String TEMP_DIRECTORY = "assets/temp/";
	public static final Class<?>[] MODEL_CLASSES = {SystemDatamodel.class, UserDatamodel.class, Datafile.class};
	
	public static final String EXTENSION_POINT_DATASET = "org.inqle.data.datasets";
	public static final String METAREPOSITORY_DATAMODEL = "Metarepository";

//	public static final String EXTENSION_POINT_CACHE_DATASET = "org.inqle.data.cacheDatasets";
//	public static final String EXTENSION_SUBJECT_CLASS_CACHE = "org.inqle.cacheDatasets.subjectClass";
//	public static final String EXTENSION_ARC_CACHE = "org.inqle.cacheDatasets.arc";
	
	public static final String EXTENSION_POINT_DATASET_FUNCTIONS = "org.inqle.data.datasetFunctions";
	public static final String EXTENSION_DATASET_FUNCTION_DATA = "org.inqle.datasetFunctions.data";
	public static final String EXTENSION_DATASET_FUNCTION_SCHEMAS = "org.inqle.datasetFunctions.schemas";
	
	public static final String ATTRIBUTE_CACHE_MODEL = "cacheInMemory";
	public static final String ATTRIBUTE_TEXT_INDEX_TYPE = "textIndexType";
	public static final Object TEXT_INDEX_TYPE_SUBJECT = "subject";
	public static final Object TEXT_INDEX_TYPE_LITERAL = "literal";
	public static final String DATABASE_ROLE_ID_ATTRIBUTE = "targetDatabase";
//	private static final String EXTENSION_POINT_CONNECTION = "org.inqle.data.connection";
	public static final String CACHE_CONNECTION = "org.inqle.data.databases.cache";
	public static final String DATASET_SUBJECT_CLASSES_CACHE = "org.inqle.datasets.cache.subjectClass";
	public static final String DATASET_ARCS_CACHE = "org.inqle.datasets.cache.arc";
	
	private AppInfo appInfo = null;
//	private OntModel metarepositoryModel = null;
	private Model metarepositoryModel = null;
	//private OntModel logModel = null;
	private static Logger log = Logger.getLogger(Persister.class);
	public static int persisterId = 0;
	
//	private Map<String, Model> cachedModels = null;
//	private Map<String, SystemDatamodel> systemDatasets = null;
//	private Map<String, IDatabase> internalDatabases = null;
//	private Map<String, CacheDataset> cacheDatasets = null;
	private Map<String, IndexBuilderModel> indexBuilders;
//	private IndexLARQ schemaFilesSubjectIndex;
//	private OntModel schemaFilesOntModel;
	private Model prefixesModel;
	private boolean systemDatamodelsInitialized = false;
	
	
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
	 * *** INITIALIZATION METHODS
	 * ********************************************************************* */
	public void initialize() {
		if (! systemDatamodelsInitialized) {
			initializeSystemDatamodels();
		}
	}
	
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
		return new File(System.getProperty(SYSTEM_PROPERTY_TEMP_DIR));
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
	
	/**
	 * Get the Jena model containing the AppInfo object
	 * @return
	 */
//	private static Model getAppInfoModel() {
//		Model appInfoModel = null;
//		try {
//			appInfoModel = FileManager.get().loadModel( getAppInfoFilePath() );
//		} catch (Exception e) {
//			log.error("Error getting Model for AppInfo:", e);
//		}
//		//log.info("Retrieved appInfoModel w/ " + appInfoModel.size() + " statements");
//		return appInfoModel;
//	}

	/* *********************************************************************
	 * *** JENA MODEL METHODS
	 * ********************************************************************* */
	
	/**
	 * Creates a new Jena SDB Model, 
	 * given a SDBDatabase object and the name of a model.
	 * 
	 * TODO Note that this might be different from a SDB data model
	 * @param dbConnectionInfo
	 * @param dbModelName
	 * @return
	 */
//	public static Model createDBModel(SDBDatabase connection, String dbModelName) {
	public static Model createDBModel(IDatabase database, String dbModelName) {
//		assert(connection != null && dbModelName != null && dbModelName.length() > 0);
//		SDBConnector dbConnector = new SDBConnector(connection);
		IDBConnector dbConnector = DBConnectorFactory.getDBConnector(database);

		log.debug("Creating Model of name '" + dbModelName + "'.");
		
		Model model = dbConnector.getModel(dbModelName);
		return model;
	}
	
	/**
	 * Creates a new database-backed model
	 */
	public Model createDatabaseBackedModel(DatabaseBackedDatamodel datamodel) {
		//first see if a datamodel of that ID already exists
		if (datamodelExists(datamodel.getId())) {
			log.info("A Datamodel of ID: " + datamodel.getId() + " already exists.");
			return null;
		}
		
		//create the database-backed model
		IDBConnector dbConnector = DBConnectorFactory.getDBConnector(datamodel.getDatabaseId());
		log.info("Creating Model of name '" + datamodel.getId() + "'...");
		Model model = dbConnector.getModel(datamodel.getId());
		
		//store the associated Datamodel object
		persist(datamodel, getMetarepositoryModel());
		return model;
	}
	
	/**
	 * Given the URI or ID of an external Datamodel, get the Jena Model object.
	 * 
	 * Best practice is to close() the model after use.
	 * @param namedModelId This could be the ID of a Datamodel or a Datafile object
	 * @return the model, or null if no model found in the metarepository
	 */
	@Deprecated
	public Model getModel(String namedModelId) {
		assert(namedModelId != null && namedModelId.length() > 0);
		Datamodel namedModel = getDatamodel(namedModelId);
		if (namedModel != null) {
			return getModel(namedModel);
		}
		return null;
	}
	
	/**
	 * Given the URI or ID of a Datamodel in the System database, get the Jena Model object.
	 * 
	 * Best practice is to NOT close() the model after use.
	 * @param datasetId the role id of the internal Datamodel
	 * @return the model, or null if no model found in the metarepository
	 */
	public Model getSystemModel(String datamodelId) {
//		if (datasetId.equals(METAREPOSITORY_DATAMODEL)) {
//			return getMetarepositoryModel();
//		}
//		SystemDatamodel internalDataset = getSystemDataset(datasetId);
		IDBConnector connector = DBConnectorFactory.getDBConnector(InqleInfo.SYSTEM_DATABASE_ROOT);
//		if (internalDataset != null) {
//			return getIndexableModel(internalDataset);
//		}
		Model systemModel = connector.getModel(datamodelId);
		log.info("Retrieved system model '" + datamodelId + "' of size: " + systemModel.size());
		return systemModel;
	}
	
	public SystemDatamodel getSystemDatamodel(String datasetId) {
//		if (datasetId.equals(METAREPOSITORY_DATAMODEL)) {
//			return getAppInfo().getMetarepositoryDataset();
//		}
		if (!systemDatamodelsInitialized) {
			initializeSystemDatamodels();
		}
		SystemDatamodel datamodel = (SystemDatamodel) reconstitute(SystemDatamodel.class, datasetId, true);
//		return getInternalDatasets().get(datasetId);
		return datamodel;
	}
	
	/**
	 * Get the IndexBuilder for the key.
	 * @param indexBuilderKey either the id of the SystemDatamodel role 
	 * or the UserDatamodel function
	 * @return
	 */
	public IndexBuilderModel getIndexBuilder(String indexBuilderKey) {
		return getIndexBuilders().get(indexBuilderKey);
	}
	
	public IndexLARQ getIndex(String indexId) {
		Map<String, IndexBuilderModel> idxBuilders = getIndexBuilders();
		if (idxBuilders==null) {
			return null;
		}
		IndexBuilderModel indexBuilder = idxBuilders.get(indexId);
		if (indexBuilder == null) {
			return null;
		}
		indexBuilder.flushWriter();
		log.trace("Retrieved & flushed IndexBuilder:" + indexBuilder);
		return indexBuilder.getIndex();
	}
	
	public void initializeSystemDatamodels() {
	//get all internal dataset extensions
		List<IExtensionSpec> datasetExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATASET);
		
		//find or create the Datamodel for each.
		for (IExtensionSpec datasetExtension: datasetExtensions) {
			String datamodelId = datasetExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);

			//create the Datamodel
			SystemDatamodel systemDatamodel = new SystemDatamodel();
//			internalDataset.setDatasetRole(datasetId);
			systemDatamodel.setId(datamodelId);
			systemDatamodel.setDatabaseId(InqleInfo.SYSTEM_DATABASE_ROOT);
			createDatabaseBackedModel(systemDatamodel);
//			persist(systemDataModel);
			log.info("CCCCCCCCCCCCCCCCCCCCCCCCCCC Created & stored new SystemDatamodel of ID: " + datamodelId + ":\n" + JenabeanWriter.toString(systemDatamodel));
		}
		
		//having created the Datasets and Models, make sure any text indexes have been created
		getIndexBuilders();
		
		log.info("SSSSSSSSSSSSSSSSSSSSSSSSSSSS System datamodels initialized");
		systemDatamodelsInitialized  = true;
	}
	
	/**
	 * Get the map of internal datasets.  If this does not exist yet (i.e. on first execution
	 * of this method) then create it.  To create it, first load what exists in the Metarepository.
	 * Next, get all internal dataset plugins and confirm that each plugin exists.  If it does not, create it.
	 * As a side-effect, this method populates the in-memory representation
	 * of any Model objects for whom the corresponding Datamodel has cacheInMemory set to true
	 * 
	 * @return
	 * 
	 * TODO include a refresh persister method, which sets systemDatasets to null and therefore will reload
	 * this info.  This would allow new plugins to be found and their datasets to be created
	 */
//	@Deprecated
//	public Map<String, SystemDatamodel> getSystemDatasets() {
//		if (systemDatasets != null) {
//			return systemDatasets;
//		}
//		
////		if (internalConnections == null) {
////			getInternalConnections();
////		}
//		
//		systemDatasets = new HashMap<String, SystemDatamodel>();
//		//load all saved InternalDatasets
//		RDF2Bean reader = new RDF2Bean(getMetarepositoryModel());
//		Collection<SystemDatamodel> savedInternalDatasets = new ArrayList<SystemDatamodel>();
//		savedInternalDatasets = reader.load(SystemDatamodel.class);
//		if (savedInternalDatasets == null || savedInternalDatasets.size()==0) {
//			log.warn("Unable to load SystemDatamodel objects.  Perhaps they do not yet exist.");
//		} else {
//			//add each to the systemDatasets in-memory Map
//			for (SystemDatamodel savedInternalDataset: savedInternalDatasets) {
////				systemDatasets.put(savedInternalDataset.getDatasetRole(), savedInternalDataset);
//				systemDatasets.put(savedInternalDataset.getId(), savedInternalDataset);
//			}
//		}
//		
//		//get all internal dataset extensions
//		List<IExtensionSpec> datasetExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATASET);
//		
//		//find or create the Datamodel for each.  Store models in cachedModels, where signalled to do so
////		cachedModels = new HashMap<String, Model>();
////		SDBDatabase defaultInternalConnection = getAppInfo().getInternalConnection();
////		IDBConnector connector = DBConnectorFactory.getDBConnector(InqleInfo.DEFAULT_INTERNAL_DATABASE_ID);
//		IDatabase defaultDatabase = new LocalFolderDatabase();
//		defaultDatabase.setId(InqleInfo.SYSTEM_DATABASE_ROOT);
//		for (IExtensionSpec datasetExtension: datasetExtensions) {
//			
////			String datasetId = datasetExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
//			String datasetId = datasetExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
//			String cacheModelString = datasetExtension.getAttribute(ATTRIBUTE_CACHE_MODEL);
//			String databaseId = datasetExtension.getAttribute(DATABASE_ROLE_ID_ATTRIBUTE);
//			IDatabase database = defaultDatabase;
//			
//			if (databaseId != null) {
//				database = getInternalDatabases().get(databaseId);
//				if (database==null) {
//					log.error("Unable to find Database: " + database + ". Not creating dataset of ID:" + datasetId);
//					continue;
//				}
//			}
//			
//			boolean cacheModel = Boolean.getBoolean(cacheModelString);
//			
//			if (systemDatasets.containsKey(datasetId)) {
//				continue;
//			}
//			//create the Datamodel
//			SystemDatamodel internalDataset = new SystemDatamodel();
////			internalDataset.setDatasetRole(datasetId);
//			internalDataset.setId(datasetId);
//			internalDataset.setDatabaseId(database.getId());
//			persist(internalDataset);
//			log.trace("Created & stored new SystemDatamodel for role " + datasetId + ":\n" + JenabeanWriter.toString(internalDataset));
//			systemDatasets.put(datasetId, internalDataset);
//			
//			//create the underlying model in the database
//			Model internalModel = createDBModel(database, internalDataset.getId());
//			log.trace("Created new Model of ID: " + datasetId + ", of size: " + internalModel.size());
//			if (cacheModel) {
//				log.info("Caching model for role " + datasetId);
//				cachedModels.put(datasetId, internalModel);
//			}
//		}
//		
//		//having created the Datasets and Models, make sure any text indexes have been created
//		getIndexBuilders();
//		
//		return systemDatasets;
//	}
	
//	@SuppressWarnings("unchecked")
//	private Map<String, IDatabase> getInternalDatabases() {
//		if (internalDatabases==null) {
//			log.info("Internal connections=null");
//			internalDatabases = new HashMap<String, IDatabase>();
//			Collection<LocalFolderDatabase> internalConnectionColl = (Collection<LocalFolderDatabase>) reconstituteAll(LocalFolderDatabase.class);
//
//			for (LocalFolderDatabase localFolderDatabase: internalConnectionColl) {
//				log.info("Adding internalConnection=" + localFolderDatabase);
//				internalDatabases.put(localFolderDatabase.getId(), localFolderDatabase);
//			}
//		}
//		return internalDatabases;
//	}

//	@Deprecated
//	public Map<String, Model> getCachedModels() {
//		if (cachedModels == null) {
//			getInternalDatasets();
//		}
//		return cachedModels;
//	}

	public Map<String, IndexBuilderModel> getIndexBuilders() {
		if (indexBuilders != null) {
			return indexBuilders;
		}
		
		indexBuilders = new HashMap<String, IndexBuilderModel>();
		//loop thru internal datasets extensions, and create each index
		List<IExtensionSpec> datasetExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATASET);
		for (IExtensionSpec datasetExtension: datasetExtensions) {
			log.trace("datasetExtension=" + datasetExtension);
			String datasetId = datasetExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
			String textIndexType = datasetExtension.getAttribute(ATTRIBUTE_TEXT_INDEX_TYPE);
			
			//if directed to do so, build & store an index for this Model
			if (textIndexType != null) {
				log.info("Creating IndexBuilder for datamodel of ID or function: " + datasetId + "; textIndexType=" + textIndexType);
				String indexFilePath = InqleInfo.getRdfDirectory() + InqleInfo.INDEXES_FOLDER + "/" + datasetId;
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
					indexWriter = new IndexWriter(indexFilePath, new StandardAnalyzer());
				} catch (Exception e) {
					log.error("Unable to connect to existing Lucene index or to create new Lucene index", e);
				}
				
				textIndexType = textIndexType.toLowerCase();
				IndexBuilderModel larqBuilder = null;
				
				Model internalModel = getSystemModel(datasetId);
				log.info("got internalmodel for " + datasetId + ".  Is null?" + (internalModel==null));
				if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_SUBJECT)) {
//					larqBuilder = new IndexBuilderSubject(indexFilePath);
					larqBuilder = new IndexBuilderSubject(indexWriter);
					
				} else if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_LITERAL)) {
//					larqBuilder = new IndexBuilderString(indexFilePath);
					larqBuilder = new IndexBuilderString(indexWriter);
				}
				if (larqBuilder != null) {
					log.info("Retrieving Index for Datamodel of ID: " + datasetId + "...");
//					larqBuilder.indexStatements(internalModel.listStatements()) ;
					//this does not work because listener does not listen across JVMs:
//					log.info("Registering Index for role " + datasetId + "...");
//					internalModel.register(larqBuilder);
					//save this larqBuilder
					if (larqBuilder.getIndex() == null) {
						log.warn("No text index exists for dataset role " + datasetId);
					}
					indexBuilders.put(datasetId, larqBuilder);
				}
			}
		}
		
		//add any external dataset functions which are supposed to be indexed
		List<IExtensionSpec> datasetFunctionExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATASET_FUNCTIONS);
		for (IExtensionSpec datasetFunctionExtension: datasetFunctionExtensions) {
			
			String datasetFunctionId = datasetFunctionExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
			String textIndexType = datasetFunctionExtension.getAttribute(ATTRIBUTE_TEXT_INDEX_TYPE);
			
//			log.info("FFFFFFFFFFFFFFFFFFdatasetFunctionId=" + datasetFunctionId + "; textIndexType=" + textIndexType);
			//if directed to do so, build & store an index for this Model
			if (textIndexType != null) {
				log.info("Making IndexBuilder for external dataset: datasetExtension=" + datasetFunctionExtension);
				String indexFilePath = InqleInfo.getRdfDirectory() + InqleInfo.INDEXES_FOLDER + "/" + datasetFunctionId;
				
				IndexWriter indexWriter = null;
				
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
				
				try {
					indexWriter = new IndexWriter(indexFilePath, new StandardAnalyzer());
					//log.info("created IndexWriter");
				} catch (Exception e) {
					log.error("Unable to connect to existing Lucene index or to create new Lucene index", e);
				}
				
				textIndexType = textIndexType.toLowerCase();
				IndexBuilderModel larqBuilder = null;
				
//				Model internalModel = getInternalModel(datasetFunctionId);
//				log.info("got internalmodel for " + datasetFunctionId + ".  Is null?" + (internalModel==null));
				if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_SUBJECT)) {
//					larqBuilder = new IndexBuilderSubject(indexFilePath);
					larqBuilder = new IndexBuilderSubject(indexWriter);
				} else if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_LITERAL)) {
//					larqBuilder = new IndexBuilderString(indexFilePath);
					larqBuilder = new IndexBuilderString(indexWriter);
				}
				//if this dataset function is a type to be indexed and if it has an index, load it.
				if (larqBuilder != null) {
					//log.info("Created indexBuilder for function " + datasetFunctionId + ".  Retrieving index if available...");
//					larqBuilder.indexStatements(internalModel.listStatements()) ;
					//this does not work because listener does not listen across JVMs:
//					log.info("Registering Index for role " + datasetId + "...");
//					internalModel.register(larqBuilder);
					//save this larqBuilder
					IndexLARQ theIndex = null;
					try {
						theIndex = larqBuilder.getIndex();
					} catch (Exception e) {
						log.info("Unable to retrieve index for dataset function " + datasetFunctionId);
					}
					if (theIndex == null) {
						log.warn("No text index exists yet for dataset function " + datasetFunctionId + "Proceeding w/ blank index.");
					}
					indexBuilders.put(datasetFunctionId, larqBuilder);
					log.info("Added indexbuilder for function " + datasetFunctionId);
				}
			}
		}
		
		log.trace("assembled list of index builders:" + indexBuilders);
		return indexBuilders;
	}
	
	/**
	 * Return an index containing all RDF data files in the Schema files area
	 * @return the index
	 * 
	 * TODO consider storing this as a file
	 * TODO consider not saving in memory
	 * TODO consider index the OntModel instead (adds extraneous statements?)
	 */
//	public IndexLARQ getSchemaFilesSubjectIndex() {
//		if (schemaFilesSubjectIndex != null) {
//			return schemaFilesSubjectIndex;
//		}
//		Model schemaFilesModel = DatafileUtil.getModel(InqleInfo.getRdfSchemaFilesDirectory());
//		IndexBuilderModel larqBuilder = new IndexBuilderSubject();
//		log.trace("Persister.getSchemaFilesSubjectIndex(): indexing model of " + schemaFilesModel.size() + " statements...");
//		larqBuilder.indexStatements(schemaFilesModel.listStatements());
//		log.trace("...done");
//		schemaFilesSubjectIndex = larqBuilder.getIndex();
//		return schemaFilesSubjectIndex;
//	}
	
	/**
	 * Flush any Lucene text indexes for the Datamodel
	 */
	public void flushIndexes(Datamodel namedModel) {
		if (namedModel instanceof SystemDatamodel) {
			SystemDatamodel systemDatamodel = (SystemDatamodel) namedModel;
//			String datasetRole = internalDataset.getDatasetRole();
			String datasetId = systemDatamodel.getId();
			IndexBuilderModel builder = getIndexBuilder(datasetId);
			if (builder != null) {
				//log.info("Flushing index builder: " + builder + " for dataset role:" + datasetRole + "...");
				builder.flushWriter();
			}
		} else if (namedModel instanceof UserDatamodel) {
			UserDatamodel userDatamodel = (UserDatamodel) namedModel;
			Collection<String> functions = userDatamodel.getDatasetFunctions();
			if (functions != null) {
				for (String function: functions) {
					IndexBuilderModel builder = getIndexBuilder(function);
					if (builder == null) continue;
					//log.info("Flushing index builder: " + builder + " for function:" + function + "...");
					builder.flushWriter();
				}
			}
		}
		
	}
	
	/**
	 * retrieves the OntModel (stored in memory) which contains the RDF Schema files
	 * @return
	 */
//	public OntModel getSchemaFilesOntModel() {
//		if (schemaFilesOntModel == null) {
//			schemaFilesOntModel = DatafileUtil.getOntModel(InqleInfo.getRdfSchemaFilesDirectory());
//		}
//		return schemaFilesOntModel;
//	}
	
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
	public Model getIndexableModel(Datamodel indexableDatamodel) {
//		log.info("Persister.getIndexableModel(Datamodel of ID=" + indexableDataset.getId() + ")...");
		Model model = getModel(indexableDatamodel);
		if (indexableDatamodel instanceof UserDatamodel) {
			UserDatamodel userDatamodel = (UserDatamodel)indexableDatamodel;
			Collection<String> functions = userDatamodel.getDatasetFunctions();
			if (functions != null) {
				for (String function: functions) {
					IndexBuilderModel builder = getIndexBuilder(function);
					if (builder == null) continue;
					log.info("Registering index builder: " + builder + " for function:" + function);
					model.register(builder);
				}
			}
		} else if (indexableDatamodel instanceof SystemDatamodel) {
			SystemDatamodel systemDatamodel = (SystemDatamodel)indexableDatamodel;
//			IndexBuilderModel builder = getIndexBuilder(internalDataset.getDatasetRole());
			IndexBuilderModel builder = getIndexBuilder(systemDatamodel.getId());
			if (builder != null) {
				model.register(builder);
			}
		}
		return model;
	}
	/**
	 * Given an instance of a Datamodel, retrieve the Jena model
	 * @param datamodel
	 * @return
	 */
	public Model getModel(Datamodel datamodel) {
//		assert(namedModel != null);
		//Model repositoryModel = getMetarepositoryModel();
		if (datamodel instanceof DatabaseBackedDatamodel) {
			IDBConnector connector = DBConnectorFactory.getDBConnector(((DatabaseBackedDatamodel) datamodel).getDatabaseId());
			return connector.getModel(datamodel.getId());
		}
		
		if (datamodel instanceof Datafile){
			return Persister.getModelFromFile(((Datafile)datamodel).getFileUrl());
		}
		
		//unknown type of Datamodel: return null
		return null;
		
		//OLD CODE FOLLOWS:
		
		//log.info("#" + persisterId + ":getModel(" + namedModel.getId() + "): get new model");
		//otherwise the requested model is a regular data-containing model.  Retrieve it from the Repositories Model
//		Model model = null;
//		if (namedModel instanceof SystemDatamodel) {
//			SystemDatamodel internalDataset = (SystemDatamodel)namedModel;
			//if the model being requested is the Metarepository, return this special model
//			if (internalDataset.getId().equals(getAppInfo().getMetarepositoryDataset().getId())) {
//				return getMetarepositoryModel();
//			}
			
//			if (getCachedModels() != null && getCachedModels().containsKey(internalDataset.getDatasetRole())) {
//				return getCachedModels().get(namedModel.getId());
//			}
//			SystemDatamodel dataset = (SystemDatamodel)namedModel;
//			RDF2Bean reader = new RDF2Bean(getMetarepositoryModel());
//			
//			SDBDatabase theConnection;
//			if (dataset.getConnectionId().equals(getAppInfo().getInternalConnection().getId())) {
//				theConnection = getAppInfo().getInternalConnection();
//			} else {
//				
//				try {
//					theConnection = (InternalConnection)reader.load(InternalConnection.class, dataset.getConnectionId());
//				} catch (NotFoundException e) {
//					log.error("Unable to load SDBDatabase for Internal Datamodel: " + dataset.getConnectionId());
//					return null;
//				}
//			}
//			SDBConnector connector = new SDBConnector(theConnection);
			
			
//		} else if (namedModel instanceof UserDatamodel) {
//			UserDatamodel dataset = (UserDatamodel)namedModel;
//			RDF2Bean reader = new RDF2Bean(getMetarepositoryModel());
//			SDBDatabase dbConnectionInfo;
//			try {
//				//dbConnectionInfo = (SDBDatabase)reader.load(SDBDatabase.class, getConnection(rdbModel.getConnectionId()).getId());
//				dbConnectionInfo = (SDBDatabase)reader.load(SDBDatabase.class, dataset.getConnectionId());
//			} catch (NotFoundException e) {
//				log.error("Unable to load SDBDatabase for External Datamodel: " + getConnection(dataset.getConnectionId()).getId());
//				return null;
//			}
//			SDBConnector connector = new SDBConnector(dbConnectionInfo);
//			model = connector.getModel(namedModel.getId());
//		return model;
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
	 * Given the URI of a Datamodel, get the Jena Model object
	 * @param namedModelUri
	 * @return
	 */
//	public OntModel getOntModel(String namedModelId) {
//		Datamodel namedModel = getDatamodel(namedModelId);
//		if (namedModel != null) {
//			return getOntModel(namedModel);
//		}
//		return null;
//	}

	/**
	 * Given an instance of a Datamodel, retrieve the Jena model
	 * @param aModel
	 * 
	 * @return
	 * TODO Untested
	 */
//	@Deprecated
//	public OntModel getOntModel(Datamodel namedModel) {
//		OntModel repositoryOntModel = getMetarepositoryModel();
//		//if the model being requested is not in the Repositories model, retrieve that specially
//		if (namedModel.getId().equals(getAppInfo().getMetarepositoryDataset().getId())) {
//			return repositoryOntModel;
//		}
//		
//		//otherwise the requested model is a regular data-containing model.  Retrieve it from the Repositories Model
//		OntModel ontModel = null;
//		if (namedModel instanceof Datamodel) {
//			Datamodel rdbModel = (Datamodel)namedModel;
//			RDF2Bean reader = new RDF2Bean(repositoryOntModel);
//			SDBDatabase dbConnectionInfo;
//			try {
//				dbConnectionInfo = (SDBDatabase)reader.load(SDBDatabase.class, getConnection(rdbModel.getConnectionId()).getId());
//			} catch (NotFoundException e) {
//				log.error("Unable to load SDBDatabase info " + rdbModel.getConnectionId());
//				e.printStackTrace();
//				return null;
//			}
//			SDBConnector connector = new SDBConnector(dbConnectionInfo);
////			ontModel = connector.getOntModel(namedModel.getModelName());
//			ontModel = connector.getMemoryOntModel(namedModel.getId());
//			
//			/*if null, create a new model
//			if (model == null) {
//				log.debug("Creating Datamodel '" + namedModel.getModelName() + "'...");
//				model = createDBModel(dbConnectionInfo, namedModel.getModelName());
//			}
//			*/
//			
//			//close
//			//connector.close();
//			
//		} else if (namedModel instanceof Datafile){
//			ontModel = ModelFactory.createOntologyModel();
//			ontModel.add(getModelFromFile(((Datafile)namedModel).getFileUrl()));
//		}
//		
//		return ontModel;
//	}
	
	/**
	 * Does this server already have a dataset of the provided ID?
	 * @param datasetId
	 * @return
	 */
	public boolean externalDatasetExists(String externalDatasetId) {
		boolean hasDatasetId = false;
		try {
			Object existingDataset = reconstitute(UserDatamodel.class, externalDatasetId, getMetarepositoryModel(), false);
			if (existingDataset != null) {
				hasDatasetId = true;
			}
		} catch (Exception e) {
			//not found, leave as false
		}
		
		return hasDatasetId;
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
//		Datamodel metarepositoryRDBModel = getAppInfo().getMetarepositoryDataset();
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
	
	public Model getMetarepositoryModel() {
		return getSystemModel(METAREPOSITORY_DATAMODEL);
//		if (metarepositoryModel != null) {
//			//log.info("#" + persisterId + ":getRepositoryModel(): return saved metarepository");
//			return this.metarepositoryModel;
//		}
//		//log.info("#" + persisterId + ":getRepositoryModel(): get new metarepository");
//		Datamodel metarepositoryRDBModel = getAppInfo().getMetarepositoryDataset();
//		SDBDatabase metarepositoryConnection = getAppInfo().getInternalConnection();
//		
//		//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
//		SDBConnector connector = new SDBConnector(metarepositoryConnection);
//		//log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + repositoryDatamodel.getModelName());
//		log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + metarepositoryConnection.getId());
//
//		//worked: this.metarepositoryModel = connector.getMemoryOntModel(metarepositoryRDBModel.getId());
//		this.metarepositoryModel = connector.getModel(metarepositoryRDBModel.getId());
//		return this.metarepositoryModel;
	}
	
	
	/* *********************************************************************
	 * *** CONNECTION METHODS
	 * ********************************************************************* */
	
//	public SDBDatabase getConnection(String connectionId) {
//		Object connectionObj = Persister.reconstitute(SDBDatabase.class, connectionId, getMetarepositoryModel(), true);
//		return (SDBDatabase)connectionObj;
//	}

	/**
	 * Store a new SDBDatabase object in the metarepository and
	 * create the SDB store in the actual database.
	 * TODO: handle connection problems
	 * @param testConnectionInfo
	 */
	public int createNewDatabase(IDatabase database) {
		log.info("Will try to create a new Database:\n" + JenabeanWriter.toString(database));
		//first create the SDBConnector and use it to create the SDB store in the database
//		SDBConnector connector = new SDBConnector(connection);
		IDBConnector connector = DBConnectorFactory.getDBConnector(database);
		//int status = SDBConnector.STORE_CREATED;
		int status = connector.createDatabase();
		//connector.createSDBStore();
		log.info("Tried to create new DB store, with status=" + status);
		//next register the new DB in the repositories namedModel
		//TODO: when deleting works, remove the below " || status == SDBConnector.STORE_IS_BLANK"
		if (status == IDBConnector.STORE_CREATED || status == IDBConnector.STORE_IS_BLANK) {
			//log.info("Persisting new connectioninfo ... \n" + JenabeanWriter.toString(connectionInfo) + "\n...to the repository model...");
			//worked: OntModel metarepositoryOntModel = getMetarepositoryModel();
			//log.info("BEFORE: Repository model has " + metarepositoryOntModel.size() + " statements");
			//worked: persist(connection, metarepositoryOntModel, true);
			persist(database);
			//log.info("AFTER: Repository model has " + metarepositoryOntModel.size() + " statements");
			//metarepositoryModel.commit();
		}
		
		//TODO consider close() this object in Persister.close()
		//connector.close();
		
		return status;
	}
	
	/* *********************************************************************
	 * *** DATAMODEL METHODS
	 * ********************************************************************* */
	/**
	 * Gets the Datamodel matching the provided URI
	 * @return the Datamodel
	 * 
	 * TODO add support fo any other Datamodel subclasses e.g. UrlModel
	 */
	public Datamodel getDatamodel(String namedModelId) {
		//OntModel metarepositoryModel = getMetarepositoryModel();
		for (Class<?> clazz: MODEL_CLASSES) {
			Datamodel namedModel = (Datamodel)reconstitute(clazz, namedModelId, getMetarepositoryModel(), true);
			if (namedModel != null) {
				return namedModel;
			}
		}
		return null;
	}
	
	public boolean datamodelExists(String namedModelId) {
		//OntModel metarepositoryModel = getMetarepositoryModel();
		for (Class<?> clazz: MODEL_CLASSES) {
			boolean datamodelExists = exists(clazz, namedModelId, getMetarepositoryModel());
			if (datamodelExists) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * List all datamodels for a given database
	 * @param databaseId
	 * @return
	 */
	public List<Datamodel> listDatamodels(String databaseId) {
		IDBConnector connector = DBConnectorFactory.getDBConnector(databaseId);
		List<String> datamodelIds = connector.listModels();
		List<Datamodel> datamodels = new ArrayList<Datamodel>();
		for (String datamodelId: datamodelIds) {
			Datamodel datamodel = getDatamodel(datamodelId);
			datamodels.add(datamodel);
		}
		return datamodels;
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
		 * If the object has a TargetDatamodel annotation, persist it to the
		 * indicated internal dataset.  If not, do nothing.
		 */
		public void persist(Object persistableObj) {
			String targetDatasetRoleId = getTargetDatamodelId(persistableObj);
			if (targetDatasetRoleId==null) {
				log.warn("Unable to persist object " + persistableObj + ".  It has no TargetDatamodel annotation.");
				return;
			}
			log.info("Persisting to dataset of role:" + targetDatasetRoleId + "\npersistableObj=" + JenabeanWriter.toString(persistableObj));
			Model targetModel = getSystemModel(targetDatasetRoleId);
			persist(persistableObj, targetModel);
		}
	
		public static String getTargetDatamodelId(Object persistableObject) {
			Class<? extends Object> persistableClass = persistableObject.getClass();
			TargetDatamodel targetDatamodel = persistableClass.getAnnotation(TargetDatamodel.class);
			if (targetDatamodel == null) {
				log.warn("Unable to retrieve dataset role id for " + persistableObject + ".  Perhaps the class definition for class " + persistableClass.getCanonicalName() + " needs to have the TargetDatamodel annotation.");
				return null;
			}
			return targetDatamodel.value();
		}
		
		public Datamodel getTargetDatamodel(Class<?> persistableClass) {
			String datasetId = getTargetDatamodelId(persistableClass);
//			return getSystemDataset(roleId);
			return getSystemDatamodel(datasetId);
		}
		
		public static String getTargetDatamodelId(Class<?> persistableClass) {
			TargetDatamodel targetDatamodel = persistableClass.getAnnotation(TargetDatamodel.class);
			if (targetDatamodel == null) {
				log.warn("Unable to retrieve internal dataset role id for " + persistableClass + ".  Perhaps the class definition for this class lacks the TargetDatamodel annotation.");
				return null;
			}
			return targetDatamodel.value();
		}
		
//		public static String getCacheDatasetRoleId(Object persistableObject) {
//			Class<? extends Object> persistableClass = persistableObject.getClass();
//			TargetDatamodel targetDataset = persistableClass.getAnnotation(TargetDatamodel.class);
//			if (targetDataset == null) {
//				log.warn("Unable to retrieve internal dataset role id for " + persistableObject + ".  Perhaps the class definition for class " + persistableClass.getCanonicalName() + " needs to have the TargetDatamodel annotation.");
//				return null;
//			}
//			return targetDataset.value();
//		}
//		
//		public static String getCacheDatasetRoleId(Class<?> persistableClass) {
//			TargetCache targetCache = persistableClass.getAnnotation(TargetCache.class);
//			if (targetCache == null) {
//				log.warn("Unable to retrieve cache dataset role id for " + persistableClass + ".  Perhaps the class definition for this class lacks the TargetCache annotation.");
//				return null;
//			}
//			return targetDataset.value();
//		}
		
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
		//log.info("set update date");
		Bean2RDF writer = new Bean2RDF(model);
		if (persistMembers) {
			writer.saveDeep(persistableObj);
		} else {
			writer.save(persistableObj);
		}
		model.close();
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
	/**
	 * reconstitue the object of the specified class, from the default TargetDatamodel (as indicated in
	 * the classes annotation TargetDatamodel("org.whatever.dataset.role.id")
	 * If not annotation is present, return null
	 */
	public Object reconstitute(Class<?> persistedClass, String objectId, boolean reconstituteMembers) {
		String targetDatasetRoleId = getTargetDatamodelId(persistedClass);
		if (targetDatasetRoleId==null) {
			log.warn("Unable to reconsitute object of ID " + objectId + ".  Its class " + persistedClass + " has no TargetDatamodel annotation.");
			return null;
		}
		Model targetModel = getSystemModel(targetDatasetRoleId);
		return Persister.reconstitute(persistedClass, objectId, targetModel, reconstituteMembers);
	}
	
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
//				log.debug("Reconstituted " + JenabeanWriter.toString(reconstitutedObj));
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
	
	/**
	 * Retrieve a Collection of jenabeans from the appropriate internal dataset,
	 * as specified in the TargetDatamodel annotation of the persistableClass
	 * @param persistableClass the class to reconstitute
	 * @return a Collection of objects of that class, or null if the class does not have a 
	 * TargetDatamodel defined
	 */
	public Collection<?> reconstituteAll(Class<?> persistableClass) {
		String datasetId = getTargetDatamodelId(persistableClass);
		if (datasetId == null) {
			return null;
		}
		return reconstituteAll(persistableClass, getSystemModel(datasetId));
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
	public boolean deleteDatabase(IDatabase database) {
		//first remove the connection reference from the metarepository
		log.debug("Removing connection: " + database.getId());
		Persister.remove(database, getMetarepositoryModel());
		
		//try to delete the connection
		try {
//			SDBConnector connector = new SDBConnector(connection);
			IDBConnector connector = DBConnectorFactory.getDBConnector(database);
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
	public boolean emptyModel(Datamodel namedModel) {
		
		//remove all statements and all index info for the Datamodel
		Model modelToBeDeleted = getIndexableModel(namedModel);
		modelToBeDeleted.removeAll();
		
		return true;
	}
	
	/**
	 * Delete a model and all its statements.
	 * @return successDeleting true if the SDB store was deleted
	 */
	public boolean deleteModel(Datamodel datamodel) {
		
		//remove all statements and all index info for the Datamodel
		Model modelToBeDeleted = getIndexableModel(datamodel);
		modelToBeDeleted.removeAll();
		
		//remove the reference to the Datamodel from the metarepository
		log.info("Removing Datamodel: " + datamodel.getUri() + "...");
		Persister.remove(datamodel, getMetarepositoryModel());
		
		if (datamodel instanceof DatabaseBackedDatamodel) {		
			
			//remove the model
//			SDBConnector connector = new SDBConnector(getConnection(((Datamodel)namedModel).getConnectionId()));
			DatabaseBackedDatamodel dbDatamodel = (DatabaseBackedDatamodel)datamodel;
			IDBConnector connector = DBConnectorFactory.getDBConnector(dbDatamodel.getDatabaseId());
			boolean successDeleting = connector.deleteDatabase();
			log.info("Success deleting the DB store? " + successDeleting);
	    connector.close();
	    
	    //invalidate the cache
	    CacheTool.invalidateDataCache(dbDatamodel.getId());
	    
	    return successDeleting;
		} else if (datamodel instanceof Datafile) {
			Model modelToDelete = getModel(datamodel);
			modelToDelete.removeAll();
			
			//delete the file
			String filePath = FileUtils.toFilename(((Datafile)datamodel).getFileUrl());
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
	public void remove(Object objectToDelete) {
		String targetDatasetRoleId = getTargetDatamodelId(objectToDelete);
		if (targetDatasetRoleId==null) {
			log.warn("Unable to delete object " + objectToDelete + ".  Its class has no TargetDatamodel annotation.");
			return;
		}
		Model model = getSystemModel(targetDatasetRoleId);
		model.begin();
		Bean2RDF deleter = new Bean2RDF(model);
		deleter.delete(objectToDelete);
		model.commit();
	}
	
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