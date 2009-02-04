package org.inqle.data.rdf.jena.tdb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.IDBConnector;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
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
	 * Creates a new database (folder) withing the root database folder
	 * Returns IDBConnector.STORE_CREATED if successful, otherwise 
	 * IDBConnector.STORE_NOT_CREATED
	 */
	public int createDatabase() {
		if (databaseId==null || databaseId.length()==0) {
			log.error("Unable to create new database, as databaseId is blank or null");
			return IDBConnector.STORE_NOT_CREATED;
		}
		
		File newDBFolder = new File(getFilePath());
		
		boolean dirMade = newDBFolder.mkdir();
		if (dirMade) {
			return IDBConnector.STORE_CREATED;
		} else {
			return IDBConnector.STORE_NOT_CREATED;
		}
	}

	public boolean deleteDatabase() {
		if (databaseId==null || databaseId.length()==0) {
			log.error("Unable to delete database: null");
			return false;
		}
		File folderToDelete = new File(getFilePath());
		return deleteDirectory(folderToDelete);
	}

	public void formatDatabase() {
		deleteDatabase();
		createDatabase();
	}

	public Dataset getDataset(String modelName) {
		return TDBFactory.createDataset(getFilePath() + "/" + modelName);
	}

	public Model getModel(String modelName) {
//		log.info("Creating/loading model: " + getFilePath() + "/" + modelName);
		return TDBFactory.createModel(getFilePath() + "/" + modelName);
	}

	/**
	 * Cannot close a folder, so nothing to do
	 */
	public void close() {
	}

	public boolean testConnection() {
		return new File(getFilePath()).exists();
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	/**
	 * List all models for this database
	 */
	public List<String> listModels() {
		List<String> modelIds = new ArrayList<String>();
		String dbRootPath = getFilePath();
		File dbRoot = new File(dbRootPath);
		File[] databaseFolders = dbRoot.listFiles();
//		log.info("Listing models for database: " + getFilePath());
		for (File databaseFolder: databaseFolders) {
			if (databaseFolder.isDirectory()) {
				log.info("Found model: " + databaseFolder.getName());
				modelIds.add(databaseFolder.getName());
			}
		}
		return modelIds;
	}
	
	/**
	 * list all databases, from the rootmost DB folder
	 */
	public List<String> listDatabases() {
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
	
	private String getFilePath() {
		return InqleInfo.getDatabaseRootFilePath() + databaseId;
	}

	public boolean deleteDirectory(File dir) {
		File[] children = dir.listFiles();
//		boolean success = true;
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
		String dirName = dir.getAbsolutePath();
		boolean success = dir.delete();
		log.info("deleted folder:" + dirName + "?  " + success);
		return success;
	}

	public boolean deleteModel(String modelName) {
		File dirToDelete = new File(getFilePath() + "/" + modelName);
		return deleteDirectory(dirToDelete);
	}

}
