package org.inqle.data.rdf.jena.tdb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.IDBConnector;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TDBConnector implements IDBConnector {

	private String databaseId;

	private static Logger log = Logger.getLogger(TDBConnector.class);
	
	public TDBConnector(String databaseId) {
		this.databaseId = databaseId;
	}

	public TDBConnector() {
		
	}

	/**
	 * Creates a new database (folder)
	 * Returns IDBConnector.STORE_CREATED if successful, otherwise 
	 * IDBConnector.STORE_NOT_CREATED
	 */
	public boolean createDatabase() {
		if (databaseId==null || databaseId.length()==0) {
			log.error("Unable to create new database, as databaseId is blank or null");
			return false;
		}
		
		File newDBFolder = new File(getBaseFilePath());
		
		return newDBFolder.mkdir();
	}

	/**
	 * Creates a new subdatabase (folder) within the root database folder
	 * Returns true if successful
	 */
	public boolean createSubdatabase(String subdatabase) {
		boolean success = false;
		if (!testConnection()) createDatabase();
		File dbFolder = new File(getFilePath(subdatabase));
		return dbFolder.mkdir();
	}
	
	public boolean deleteDatabase() {
		if (databaseId==null || databaseId.length()==0) {
			log.error("Unable to delete database: null");
			return false;
		}
		File folderToDelete = new File(getBaseFilePath());
		return deleteDirectory(folderToDelete);
	}

	public void formatDatabase() {
		deleteDatabase();
		createDatabase();
	}

	public Dataset getDataset(String modelType, String modelName) {
		return TDBFactory.createDataset(getFilePath(modelType) + "/" + modelName);
	}

	public Model getModel(String modelType, String modelName) {
		//if the database does not yet exist, create it
		if (!testSubdatabaseConnection(modelType)) {
			createSubdatabase(modelType);
		}
//		log.info("Creating/loading model: " + getFilePath() + "/" + modelName);
		return TDBFactory.createModel(getFilePath(modelType) + "/" + modelName);
	}

	/**
	 * Cannot close a folder, so nothing to do
	 */
	public void close() {
	}

	public boolean testConnection() {
		if (databaseId == null || databaseId.length()==0) return false;
		return new File(getBaseFilePath()).exists();
	}
	
	public boolean testSubdatabaseConnection(String subdatabase) {
		if (!testConnection()) return false;
		return new File(getFilePath(subdatabase)).exists();
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	/**
	 * List all models for this database, of the given type
	 */
	public List<String> listModelNames(String modelType) {
		String dbPath = getFilePath(modelType);
		List<String> modelNames = new ArrayList<String>();
		File dbRoot = new File(dbPath);
		File[] databaseFolders = dbRoot.listFiles();
//		log.info("Listing models for database: " + getFilePath());
		for (File databaseFolder: databaseFolders) {
			if (databaseFolder.isDirectory()) {
				log.info("Found model: " + databaseFolder.getName());
				modelNames.add(databaseFolder.getName());
			}
		}
		return modelNames;
	}
	
	public List<String> listAllDatabases() {
		List<String> databases = new ArrayList<String>();
		String dbRootPath = InqleInfo.getDatabaseRootFilePath();
		File dbRoot = new File(dbRootPath);
		File[] databaseFolders = dbRoot.listFiles();
		for (File databaseFolder: databaseFolders) {
			if (databaseFolder.isDirectory()) {
				databases.add(databaseFolder.getName());
			}
		}
		return databases;
	}
	
	private String getBaseFilePath() {
		return InqleInfo.getDatabaseRootFilePath() + databaseId;
	}
	
	private String getFilePath(String subdatabase) {
		return getBaseFilePath() + "/" + subdatabase;
	}
	
//	public static boolean databaseExists(String dbId) {
//		if (dbId == null || dbId.length()==0) return false;
//		return new File(InqleInfo.getDatabaseRootFilePath() + dbId).exists();
//	}

	public boolean deleteDirectory(File dir) {
		File[] children = dir.listFiles();
		for (File child: children) {
			if (child.isDirectory()) {
				deleteDirectory(child);
			}	else {
				String fileName = child.getAbsolutePath();
				boolean success = child.delete();
				boolean writable = child.canWrite();
				log.info("deleted file:" + fileName + "?  " + success + "; writable=" + writable);
			}
		}
//		FileOps.clearDirectory(dir.getAbsolutePath());
		boolean success = dir.delete();
		log.info("deleted folder:" + dir + "?  " + success);
		return success;
	}

	public boolean deleteModel(String type, String modelName) {
		Dataset datasetToDelete = getDataset(type, modelName);
		datasetToDelete.getDefaultModel().removeAll();
		TDB.sync(datasetToDelete) ;
		datasetToDelete.close();
		File dirToDelete = new File(getFilePath(type) + "/" + modelName);
		return deleteDirectory(dirToDelete);
	}
	
	public boolean modelExists(String modelType, String modelName) {
		File putativeModelFolder = new File(getFilePath(modelType) + "/" + modelName);
		return putativeModelFolder.exists();
	}

}
