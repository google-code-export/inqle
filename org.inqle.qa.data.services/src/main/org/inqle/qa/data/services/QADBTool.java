package org.inqle.qa.data.services;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.DBConnectorFactory;
import org.inqle.data.rdf.jena.DatabaseBackedJenamodel;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.LocalFolderDatabase;
import org.inqle.data.rdf.jena.PurposefulDatamodel;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;

public class QADBTool {

	private static final String USERS_DB_FOLDER = "org.inqle.qa.data.users";

	private static final String DATAMODEL_ID_ANSWERS = "org.inqle.qa.db.id.answers.1";
	private static final String DATAMODEL_PURPOSE_ANSWERS = "org.inqle.qa.db.function.answers";
	private static final String DATAMODEL_ID_FACTS = "org.inqle.qa.db.id.facts.1";
	private static final String DATAMODEL_PURPOSE_FACTS = "org.inqle.qa.db.function.facts";
	private static final String DATAMODEL_ID_SEEN = "org.inqle.qa.db.id.seen.1";
	private static final String DATAMODEL_PURPOSE_SEEN = "org.inqle.qa.db.function.seen";

	private static Logger log = Logger.getLogger(QADBTool.class);
	
	/**
	 * Create a user's database and associated datamodels
	 * TODO add ImportedData type, to support users uploading spreadsheets
	 * @param userId
	 * @return
	 */
	public static boolean createUserDatabase(String userId) {
		//create the user database and first user datamodel
		IDatabase databaseForUser = getDatabaseForUser(userId);
		Persister persister = Persister.getInstance();
		try {
			boolean success = persister.createNewDatabase(databaseForUser);
			log.info("CREATED user database, with success?" + success);
		} catch (Exception e) {
			log.error("Error creating/storing user database", e);
			return false;
		}
		return true;
	}

	public static LocalFolderDatabase getDatabaseForUser(String userId) {
		LocalFolderDatabase userDatabase = new LocalFolderDatabase();
		userDatabase.setId(getDatabaseIdForUser(userId));
		return userDatabase;
	}
	
	public static String getDatabaseIdForUser(String userId) {
		return USERS_DB_FOLDER + "/" + IDBConnector.SUBDATABASE_DATA + "/" + userId;
	}
	
	public static boolean userDataBaseExists(String userId) {
		IDBConnector dbConnector = DBConnectorFactory.getDBConnector(getDatabaseIdForUser(userId));
		return dbConnector.testConnection();
	}
	
//	public boolean ensureUserDatamodelExists(PurposefulDatamodel datamodel) {
//		IDBConnector dbConnector = DBConnectorFactory.getDBConnector(datamodel.getDatabaseId());
//		log.info("Creating Model of id '" + datamodel.getId() + "'...");
//		Model model = dbConnector.getModel(datamodel.getId());
//	}
}

/*
//create all datamodels

//ANSWERS
UserDatamodel aDatamodel = new UserDatamodel();
aDatamodel.setId(DATAMODEL_ID_ANSWERS);
aDatamodel.setDatabaseId(databaseForUser.getId());
aDatamodel.addDatamodelPurpose(DATAMODEL_PURPOSE_ANSWERS);
persister.createDatabaseBackedModel(aDatamodel);

//DERIVED DATA
aDatamodel = new UserDatamodel();
aDatamodel.setId(DATAMODEL_ID_FACTS);
aDatamodel.setDatabaseId(databaseForUser.getId());
aDatamodel.addDatamodelPurpose(DATAMODEL_PURPOSE_FACTS);
persister.createDatabaseBackedModel(aDatamodel);

//ADVISORIES SEEN
aDatamodel = new UserDatamodel();
aDatamodel.setId(DATAMODEL_ID_SEEN);
aDatamodel.setDatabaseId(databaseForUser.getId());
aDatamodel.addDatamodelPurpose(DATAMODEL_PURPOSE_SEEN);
persister.createDatabaseBackedModel(aDatamodel);

return true;
*/