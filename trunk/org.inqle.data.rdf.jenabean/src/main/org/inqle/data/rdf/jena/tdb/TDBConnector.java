package org.inqle.data.rdf.jena.tdb;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;

public class TDBConnector implements IDBConnector {

	private String databaseId;

	public TDBConnector(String databaseId) {
		this.databaseId = databaseId;
	}

	public TDBConnector() {
		
	}

	public int createDatabase() {
		
	}

	public boolean deleteDatabase() {
		
	}

	public void formatDatabase() {
		
	}

	public Dataset getDataset() {
		
	}

	public Model getModel(String modelName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public boolean testConnection() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public List<String> listDatabases() {
		List<String> databases = new ArrayList<String>();
		
	}

}
