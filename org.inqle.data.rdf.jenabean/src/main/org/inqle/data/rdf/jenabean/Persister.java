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
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.Datafile;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.data.rdf.jena.InternalDataset;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jena.sdb.DBConnector;
import org.inqle.data.rdf.jena.util.DatafileUtil;

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
	public static final Class<?>[] MODEL_CLASSES = {ExternalDataset.class, Datafile.class};
	public static final String EXTENSION_POINT_DATASET = "org.inqle.data.datasets";
	public static final String METAREPOSITORY_DATASET = "org.inqle.datasets.metaRepository";

	public static final String EXTENSION_DATASET_FUNCTION_DATA = "org.inqle.datasetFunctions.data";
	public static final String EXTENSION_DATASET_FUNCTION_SCHEMAS = "org.inqle.datasetFunctions.schemas";
	
	private static final String ATTRIBUTE_CACHE_MODEL = "cacheInMemory";
	private static final String ATTRIBUTE_TEXT_INDEX_TYPE = "textIndexType";
	private static final Object TEXT_INDEX_TYPE_SUBJECT = "subject";
	private static final Object TEXT_INDEX_TYPE_LITERAL = "literal";
	
	private AppInfo appInfo = null;
//	private OntModel metarepositoryModel = null;
	private Model metarepositoryModel = null;
	//private OntModel logModel = null;
	private static Logger log = Logger.getLogger(Persister.class);
	public static int persisterId = 0;
	
	private Map<String, Model> cachedModels = null;
	private Map<String, InternalDataset> internalDatasets = null;
	private Map<String, IndexBuilderModel> indexBuilders;
	private IndexLARQ schemaFilesSubjectIndex;
	private OntModel schemaFilesOntModel;
	private Model prefixesModel;
	public static final String EXTENSION_POINT_DATASET_FUNCTIONS = "org.inqle.data.datasetFunctions";
	
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
	public Model createDBModel(Dataset dataset) {
		Connection connection = getConnection(dataset.getConnectionId());
		//String dbModelName = rdbModel.getModelName();
		String dbModelName = dataset.getId();
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
		persist(dataset, getMetarepositoryModel());
		return model;
	}
	
	/**
	 * Given the URI or ID of an external NamedModel, get the Jena Model object.
	 * 
	 * Best practice is to close() the model after use.
	 * @param namedModelId This could be the ID of a Dataset or a Datafile object
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
	 * Given the URI or ID of a NamedModel, get the Jena Model object.
	 * 
	 * Best practice is to NOT close() the model after use.
	 * @param datasetRoleId the role id of the internal Dataset
	 * @return the model, or null if no model found in the metarepository
	 */
	public Model getInternalModel(String datasetRoleId) {
		if (datasetRoleId.equals(METAREPOSITORY_DATASET)) {
			return getMetarepositoryModel();
		}
		InternalDataset internalDataset = getInternalDataset(datasetRoleId);
		
		if (internalDataset != null) {
			return getIndexableModel(internalDataset);
		}
		return null;
	}
	
	public InternalDataset getInternalDataset(String datasetRoleId) {
		if (datasetRoleId.equals(METAREPOSITORY_DATASET)) {
			return getAppInfo().getMetarepositoryDataset();
		}
		return getInternalDatasets().get(datasetRoleId);
	}
	
	/**
	 * Get the IndexBuilder for the key.
	 * @param indexBuilderKey either the id of the InternalDataset role 
	 * or the ExternalDataset function
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
	
	/**
	 * Get the map of internal datasets.  If this does not exist yet (i.e. on first execution
	 * of this method) then create it.  To create it, first load what exists in the Metarepository.
	 * Next, get all internal dataset plugins and confirm that each plugin exists.  If it does not, create it.
	 * As a side-effect, this method populates the in-memory representation
	 * of any Model objects for whom the corresponding Dataset has cacheInMemory set to true
	 * 
	 * @return
	 * 
	 * TODO include a refresh persister method, which sets internalDatasets to null and therefore will reload
	 * this info.  This would allow new plugins to be found and their datasets to be created
	 */
	public Map<String, InternalDataset> getInternalDatasets() {
		if (internalDatasets != null) {
			return internalDatasets;
		}
		
		internalDatasets = new HashMap<String, InternalDataset>();
		//load all saved InternalDatasets
		RDF2Bean reader = new RDF2Bean(getMetarepositoryModel());
		Collection<InternalDataset> savedInternalDatasets = new ArrayList<InternalDataset>();
		savedInternalDatasets = reader.load(InternalDataset.class);
		if (savedInternalDatasets == null || savedInternalDatasets.size()==0) {
			log.warn("Unable to load InternalDataset objects.  Perhaps they do not yet exist.");
		} else {
			//add each to the internalDatasets in-memory Map
			for (InternalDataset savedInternalDataset: savedInternalDatasets) {
				internalDatasets.put(savedInternalDataset.getDatasetRole(), savedInternalDataset);
			}
		}
		
		//get all internal dataset extensions
		List<IExtensionSpec> datasetExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATASET);
		
		//find or create the Dataset for each.  Store models in cachedModels, where signalled to do so
		cachedModels = new HashMap<String, Model>();
		Connection defaultInternalConnection = getAppInfo().getInternalConnection();
		for (IExtensionSpec datasetExtension: datasetExtensions) {
			
			String datasetRoleId = datasetExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
			String cacheModelString = datasetExtension.getAttribute(ATTRIBUTE_CACHE_MODEL);
			boolean cacheModel = Boolean.getBoolean(cacheModelString);
			
			if (internalDatasets.containsKey(datasetRoleId)) {
				continue;
			}
			//create the Dataset
			InternalDataset internalDataset = new InternalDataset();
			internalDataset.setDatasetRole(datasetRoleId);
			internalDataset.setConnectionId(defaultInternalConnection.getId());
			persist(internalDataset);
			log.trace("Created & stored new InternalDataset for role " + datasetRoleId + ":\n" + JenabeanWriter.toString(internalDataset));
			internalDatasets.put(datasetRoleId, internalDataset);
			
			//create the underlying model in the SDB database
			Model internalModel = createDBModel(defaultInternalConnection, internalDataset.getId());
			log.trace("Created new Model for role " + datasetRoleId + " of size " + internalModel.size());
			if (cacheModel) {
				log.info("Caching model for role " + datasetRoleId);
				cachedModels.put(datasetRoleId, internalModel);
			}
		}
		
		//having created the Datasets and Models, make sure any text indexes have been created
		getIndexBuilders();
		
		return internalDatasets;
	}

	public Map<String, Model> getCachedModels() {
		if (cachedModels == null) {
			getInternalDatasets();
		}
		return cachedModels;
	}

	public Map<String, IndexBuilderModel> getIndexBuilders() {
		if (indexBuilders != null) {
			return indexBuilders;
		}
		
		indexBuilders = new HashMap<String, IndexBuilderModel>();
		//loop thru internal datasets extensions, and create each index
		List<IExtensionSpec> datasetExtensions = ExtensionFactory.getExtensionSpecs(EXTENSION_POINT_DATASET);
		for (IExtensionSpec datasetExtension: datasetExtensions) {
			log.trace("datasetExtension=" + datasetExtension);
			String datasetRoleId = datasetExtension.getAttribute(InqleInfo.ID_ATTRIBUTE);
			String textIndexType = datasetExtension.getAttribute(ATTRIBUTE_TEXT_INDEX_TYPE);
			
			//if directed to do so, build & store an index for this Model
			if (textIndexType != null) {
				log.info("Creating IndexBuilder for internal datasetRoleId=" + datasetRoleId + "; textIndexType=" + textIndexType);
				String indexFilePath = InqleInfo.getRdfDirectory() + InqleInfo.INDEXES_FOLDER + "/" + datasetRoleId;
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
				
				Model internalModel = getInternalModel(datasetRoleId);
				log.info("got internalmodel for " + datasetRoleId + ".  Is null?" + (internalModel==null));
				if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_SUBJECT)) {
//					larqBuilder = new IndexBuilderSubject(indexFilePath);
					larqBuilder = new IndexBuilderSubject(indexWriter);
					
				} else if (indexWriter != null && textIndexType.equals(TEXT_INDEX_TYPE_LITERAL)) {
//					larqBuilder = new IndexBuilderString(indexFilePath);
					larqBuilder = new IndexBuilderString(indexWriter);
				}
				if (larqBuilder != null) {
					log.info("Retrieving Index for role " + datasetRoleId + "...");
//					larqBuilder.indexStatements(internalModel.listStatements()) ;
					//this does not work because listener does not listen across JVMs:
//					log.info("Registering Index for role " + datasetRoleId + "...");
//					internalModel.register(larqBuilder);
					//save this larqBuilder
					if (larqBuilder.getIndex() == null) {
						log.warn("No text index exists for dataset role " + datasetRoleId);
					}
					indexBuilders.put(datasetRoleId, larqBuilder);
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
//					log.info("Registering Index for role " + datasetRoleId + "...");
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
	 * Flush any Lucene text indexes for the NamedModel
	 */
	public void flushIndexes(NamedModel namedModel) {
		if (namedModel instanceof InternalDataset) {
			InternalDataset internalDataset = (InternalDataset) namedModel;
			String datasetRole = internalDataset.getDatasetRole();
			IndexBuilderModel builder = getIndexBuilder(datasetRole);
			if (builder != null) {
				//log.info("Flushing index builder: " + builder + " for dataset role:" + datasetRole + "...");
				builder.flushWriter();
			}
		} else if (namedModel instanceof ExternalDataset) {
			ExternalDataset externalDataset = (ExternalDataset) namedModel;
			Collection<String> functions = externalDataset.getDatasetFunctions();
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
	 * Given a Dataset, retrieves a Model which has
	 * had its text indexers registered
	 * @param indexableDataset
	 * @return
	 */
	public Model getIndexableModel(NamedModel indexableDataset) {
//		log.info("Persister.getIndexableModel(Dataset of ID=" + indexableDataset.getId() + ")...");
		Model model = getModel(indexableDataset);
		if (indexableDataset instanceof ExternalDataset) {
			ExternalDataset externalDataset = (ExternalDataset)indexableDataset;
			Collection<String> functions = externalDataset.getDatasetFunctions();
			if (functions != null) {
				for (String function: functions) {
					IndexBuilderModel builder = getIndexBuilder(function);
					if (builder == null) continue;
					log.info("Registering index builder: " + builder + " for function:" + function);
					model.register(builder);
				}
			}
		} else if (indexableDataset instanceof InternalDataset) {
			InternalDataset internalDataset = (InternalDataset)indexableDataset;
			IndexBuilderModel builder = getIndexBuilder(internalDataset.getDatasetRole());
			if (builder != null) {
				model.register(builder);
			}
		}
		return model;
	}
	/**
	 * Given an instance of a NamedModel, retrieve the Jena model
	 * @param namedModel
	 * @return
	 */
	public Model getModel(NamedModel namedModel) {
		assert(namedModel != null);
		//Model repositoryModel = getMetarepositoryModel();
		
		//log.info("#" + persisterId + ":getModel(" + namedModel.getId() + "): get new model");
		//otherwise the requested model is a regular data-containing model.  Retrieve it from the Repositories Model
		Model model = null;
		if (namedModel instanceof InternalDataset) {
			InternalDataset internalDataset = (InternalDataset)namedModel;
			//if the model being requested is the Metarepository, return this special model
			if (internalDataset.getId().equals(getAppInfo().getMetarepositoryDataset().getId())) {
				return getMetarepositoryModel();
			}
			
			if (getCachedModels() != null && getCachedModels().containsKey(internalDataset.getDatasetRole())) {
				return getCachedModels().get(namedModel.getId());
			}
			InternalDataset dataset = (InternalDataset)namedModel;
			RDF2Bean reader = new RDF2Bean(getMetarepositoryModel());
			Connection theConnection;
			if (dataset.getConnectionId().equals(getAppInfo().getInternalConnection().getId())) {
				theConnection = getAppInfo().getInternalConnection();
			} else {
				try {
					theConnection = (Connection)reader.load(Connection.class, dataset.getConnectionId());
				} catch (NotFoundException e) {
					log.error("Unable to load Connection for Internal Dataset: " + dataset.getConnectionId());
					return null;
				}
			}
			DBConnector connector = new DBConnector(theConnection);
			model = connector.getModel(namedModel.getId());
			
		} else if (namedModel instanceof ExternalDataset) {
			ExternalDataset dataset = (ExternalDataset)namedModel;
			RDF2Bean reader = new RDF2Bean(getMetarepositoryModel());
			Connection dbConnectionInfo;
			try {
				//dbConnectionInfo = (Connection)reader.load(Connection.class, getConnection(rdbModel.getConnectionId()).getId());
				dbConnectionInfo = (Connection)reader.load(Connection.class, dataset.getConnectionId());
			} catch (NotFoundException e) {
				log.error("Unable to load Connection for External Dataset: " + getConnection(dataset.getConnectionId()).getId());
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
	 * Given the URI of a NamedModel, get the Jena Model object
	 * @param namedModelUri
	 * @return
	 */
//	public OntModel getOntModel(String namedModelId) {
//		NamedModel namedModel = getNamedModel(namedModelId);
//		if (namedModel != null) {
//			return getOntModel(namedModel);
//		}
//		return null;
//	}

	/**
	 * Given an instance of a NamedModel, retrieve the Jena model
	 * @param aModel
	 * 
	 * @return
	 * TODO Untested
	 */
//	@Deprecated
//	public OntModel getOntModel(NamedModel namedModel) {
//		OntModel repositoryOntModel = getMetarepositoryModel();
//		//if the model being requested is not in the Repositories model, retrieve that specially
//		if (namedModel.getId().equals(getAppInfo().getMetarepositoryDataset().getId())) {
//			return repositoryOntModel;
//		}
//		
//		//otherwise the requested model is a regular data-containing model.  Retrieve it from the Repositories Model
//		OntModel ontModel = null;
//		if (namedModel instanceof Dataset) {
//			Dataset rdbModel = (Dataset)namedModel;
//			RDF2Bean reader = new RDF2Bean(repositoryOntModel);
//			Connection dbConnectionInfo;
//			try {
//				dbConnectionInfo = (Connection)reader.load(Connection.class, getConnection(rdbModel.getConnectionId()).getId());
//			} catch (NotFoundException e) {
//				log.error("Unable to load Connection info " + rdbModel.getConnectionId());
//				e.printStackTrace();
//				return null;
//			}
//			DBConnector connector = new DBConnector(dbConnectionInfo);
////			ontModel = connector.getOntModel(namedModel.getModelName());
//			ontModel = connector.getMemoryOntModel(namedModel.getId());
//			
//			/*if null, create a new model
//			if (model == null) {
//				log.debug("Creating Dataset '" + namedModel.getModelName() + "'...");
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
			Object existingDataset = reconstitute(ExternalDataset.class, externalDatasetId, getMetarepositoryModel(), false);
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
//		Dataset metarepositoryRDBModel = getAppInfo().getMetarepositoryDataset();
//		Connection metarepositoryConnection = getAppInfo().getDefaultInternalConnection();
//		
//		//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
//		DBConnector connector = new DBConnector(metarepositoryConnection);
//		//log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + repositoryNamedModel.getModelName());
//		log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + metarepositoryConnection.getId());
//
//		//this.metarepositoryModel = connector.getOntModel(repositoryNamedModel.getModelName());
//		this.metarepositoryModel = connector.getMemoryOntModel(metarepositoryRDBModel.getId());
//		return this.metarepositoryModel;
//	}
	
	public Model getMetarepositoryModel() {
		if (metarepositoryModel != null) {
			//log.info("#" + persisterId + ":getRepositoryModel(): return saved metarepository");
			return this.metarepositoryModel;
		}
		//log.info("#" + persisterId + ":getRepositoryModel(): get new metarepository");
		Dataset metarepositoryRDBModel = getAppInfo().getMetarepositoryDataset();
		Connection metarepositoryConnection = getAppInfo().getInternalConnection();
		
		//log.info("getRepositoryModel(): retrieved repositoryConnection: " + JenabeanWriter.toString(repositoryConnection));
		DBConnector connector = new DBConnector(metarepositoryConnection);
		//log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + repositoryNamedModel.getModelName());
		log.debug("#" + persisterId + ":getRepositoryModel(): getting model of name:" + metarepositoryConnection.getId());

		//worked: this.metarepositoryModel = connector.getMemoryOntModel(metarepositoryRDBModel.getId());
		this.metarepositoryModel = connector.getModel(metarepositoryRDBModel.getId());
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
			//worked: OntModel metarepositoryOntModel = getMetarepositoryModel();
			//log.info("BEFORE: Repository model has " + metarepositoryOntModel.size() + " statements");
			//worked: persist(connection, metarepositoryOntModel, true);
			persist(connection);
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
		//OntModel metarepositoryModel = getMetarepositoryModel();
		for (Class<?> clazz: MODEL_CLASSES) {
			NamedModel namedModel = (NamedModel)reconstitute(clazz, namedModelId, getMetarepositoryModel(), true);
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
		 * If the object has a TargetDataset annotation, persist it to the
		 * indicated internal dataset.  If not, do nothing.
		 */
		public void persist(Object persistableObj) {
			String targetDatasetRoleId = getDatasetRoleId(persistableObj);
			if (targetDatasetRoleId==null) {
				log.warn("Unable to persist object " + persistableObj + ".  It has no TargetDataset annotation.");
				return;
			}
//			log.info("Persisting to dataset of role:" + targetDatasetRoleId + "\npersistableObj=" + JenabeanWriter.toString(persistableObj));
			Model targetModel = getInternalModel(targetDatasetRoleId);
			persist(persistableObj, targetModel);
		}
	
		public static String getDatasetRoleId(Object persistableObject) {
			Class<? extends Object> persistableClass = persistableObject.getClass();
			TargetDataset targetDataset = persistableClass.getAnnotation(TargetDataset.class);
			if (targetDataset == null) {
				log.warn("Unable to retrieve dataset role id for " + persistableObject + ".  Perhaps the class definition for class " + persistableClass.getCanonicalName() + " needs to have the TargetDataset annotation.");
				return null;
			}
			return targetDataset.value();
		}
		
		public static String getDatasetRoleId(Class<?> persistableClass) {
			TargetDataset targetDataset = persistableClass.getAnnotation(TargetDataset.class);
			if (targetDataset == null) {
				log.warn("Unable to retrieve dataset role id for " + persistableClass + ".  Perhaps the class definition for this class lacks the TargetDataset annotation.");
				return null;
			}
			return targetDataset.value();
		}
		
	/**
	 * Persist a Object object to an SDB store as RDF
	 * @param persistableObj the Jenabean object
	 * @param model the Jena model into which to persist
	 * @param persistMembers if true, all Object members (and their members, recursively)
	 * will be persisted.  If false, they will not.
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
	 * reconstitue the object of the specified class, from the default TargetDataset (as indicated in
	 * the classes annotation TargetDataset("org.whatever.dataset.role.id")
	 * If not annotation is present, return null
	 */
	public Object reconstitute(Class<?> persistedClass, String objectId, boolean reconstituteMembers) {
		String targetDatasetRoleId = getDatasetRoleId(persistedClass);
		if (targetDatasetRoleId==null) {
			log.warn("Unable to reconsitute object of ID " + objectId + ".  Its class " + persistedClass + " has no TargetDataset annotation.");
			return null;
		}
		Model targetModel = getInternalModel(targetDatasetRoleId);
		return Persister.reconstitute(persistedClass, objectId, targetModel, reconstituteMembers);
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
		namedModels.addAll((Collection<ExternalDataset>) reconstituteAll(ExternalDataset.class));
		namedModels.addAll((Collection<Datafile>) reconstituteAll(Datafile.class));
		return namedModels;
	}
	
	/**
	 * Retrieve a Collection of jenabeans from the appropriate internal dataset,
	 * as specified in the TargetDataset annotation of the persistableClass
	 * @param persistableClass the class to reconstitute
	 * @return a Collection of objects of that class, or null if the class does not have a 
	 * TargetDataset defined
	 */
	public Collection<?> reconstituteAll(Class<?> persistableClass) {
		String datasetRoleId = getDatasetRoleId(persistableClass);
		if (datasetRoleId == null) {
			return null;
		}
		return reconstituteAll(persistableClass, getInternalModel(datasetRoleId));
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
		
		//remove all statements and all index info for the NamedModel
		Model modelToBeDeleted = getIndexableModel(namedModel);
		modelToBeDeleted.removeAll();
		
		//remove the reference to the NamedModel from the metarepository
		log.info("Removing NamedModel: " + namedModel.getUri() + "...");
		Persister.remove(namedModel, getMetarepositoryModel());
		
		if (namedModel instanceof Dataset) {		
			
			//remove the model
			DBConnector connector = new DBConnector(getConnection(((Dataset)namedModel).getConnectionId()));
			boolean successDeleting = connector.deleteSDBStore();
			log.info("Success deleting the SDB store? " + successDeleting);
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
			Model modelToDelete = getModel(namedModel);
			modelToDelete.removeAll();
			
			//delete the file
			String filePath = FileUtils.toFilename(((Datafile)namedModel).getFileUrl());
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
		String targetDatasetRoleId = getDatasetRoleId(objectToDelete);
		if (targetDatasetRoleId==null) {
			log.warn("Unable to delete object " + objectToDelete + ".  Its class has no TargetDataset annotation.");
			return;
		}
		Model model = getInternalModel(targetDatasetRoleId);
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