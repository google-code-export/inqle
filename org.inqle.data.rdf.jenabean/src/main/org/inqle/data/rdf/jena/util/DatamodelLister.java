package org.inqle.data.rdf.jena.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.PurposefulDatamodel;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;

public class DatamodelLister {

	/**
	 * List all datamodels across all databases
	 * @return
	 */
	public static List<PurposefulDatamodel> listAllDatamodels() {
			List<PurposefulDatamodel> datamodels = new ArrayList<PurposefulDatamodel>();
			IDBConnector generalConnector = DBConnectorFactory.getDBConnector();
			List<String> databases = generalConnector.listAllDatabases();
			for (String databaseId: databases) {
				datamodels.addAll(listDatamodels(databaseId));
			}
			return datamodels;
	}

	/**
	 * List all datamodels of the same purpose, across all databases
	 * @param purposeId
	 * @return
	 */
	public static List<String> listAllDatamodelIdsOfPurpose(String purposeId) {
		List<String> datamodels = new ArrayList<String>();
		IDBConnector generalConnector = DBConnectorFactory.getDBConnector();
		List<String> databases = generalConnector.listAllDatabases();
		for (String databaseId: databases) {
			datamodels.addAll(listDatamodelIdsOfPurpose(databaseId, purposeId));
		}
		return datamodels;
	}
	
	/**
	 * List all datamodels of the same purpose, across all databases
	 * @param purposeId
	 * @return
	 */
	public static List<PurposefulDatamodel> listAllDatamodelsOfPurpose(String purposeId) {
		List<PurposefulDatamodel> datamodels = new ArrayList<PurposefulDatamodel>();
		IDBConnector generalConnector = DBConnectorFactory.getDBConnector();
		List<String> databases = generalConnector.listAllDatabases();
		for (String databaseId: databases) {
			datamodels.addAll(listDatamodelsOfPurpose(databaseId, purposeId));
		}
		return datamodels;
	}

	/**
	 * For the specified database, list all datamodels
	 * @param databaseId
	 * @return
	 */
	public static Collection<PurposefulDatamodel> listDatamodels(String databaseId) {
		Persister persister = Persister.getInstance();
		return persister.reconstituteAll(databaseId, PurposefulDatamodel.class);
	}
	
	/**
	 * For the specified database, list all datamodels of the specified purpose
	 * @param databaseId
	 * @param purposeId
	 * @return
	 */
	public static List<String> listDatamodelIdsOfPurpose(String databaseId, String purposeId) {
		List<String> targetDatamodels = new ArrayList<String>();
		Persister persister = Persister.getInstance();
		Collection<PurposefulDatamodel> datamodels = persister.reconstituteAll(databaseId, PurposefulDatamodel.class);
		for (PurposefulDatamodel purposefulDatamodel: datamodels) {
			if (purposefulDatamodel.getDatamodelPurposes().contains(purposeId)) {
				targetDatamodels.add(purposefulDatamodel.getId());
			}
		}
		return targetDatamodels;
	}
	
	/**
	 * For the specified database, list all datamodels of the specified purpose
	 * @param databaseId
	 * @param purposeId
	 * @return
	 */
	public static List<PurposefulDatamodel> listDatamodelsOfPurpose(String databaseId, String purposeId) {
		List<PurposefulDatamodel> targetDatamodels = new ArrayList<PurposefulDatamodel>();
		Persister persister = Persister.getInstance();
		Collection<PurposefulDatamodel> datamodels = persister.reconstituteAll(databaseId, PurposefulDatamodel.class);
		for (PurposefulDatamodel purposefulDatamodel: datamodels) {
			if (purposefulDatamodel.getDatamodelPurposes().contains(purposeId)) {
				targetDatamodels.add(purposefulDatamodel);
			}
		}
		return targetDatamodels;
	}
}
