package org.inqle.test.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.log4j.Logger;
//import org.h2.tools.Server;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.SDBDatabase;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Bean2RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class AppInfoProvider {

	public static final String APP_HOME = "C:/workspace/";
	public static final String FILENAME_APPINFO = "org.inqle.data.rdf.jenabean/src/test/secure/AppInfo.ttl";
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_TYPE = "H2";
	//private static final String DB_URL = "jdbc:postgresql://localhost:5432/test_inqle_repositories";
	private static final String DB_URL = "jdbc:h2:test_inqle_repositories";
	private static final String DB_USER = "test_user";
	private static final String DB_PASSWORD = "test_password";
	
	private static Logger log = Logger.getLogger(AppInfoProvider.class);
//	private static Server server;
	
	public static String getAppInfoFilePath() {
		return APP_HOME + FILENAME_APPINFO;
	}
	
	public static void createAppInfo() {
		//create the connection object, containing db connection info for the repository namedmodel
		SDBDatabase sDBDatabase = new SDBDatabase();
		sDBDatabase.setDbClass(DB_DRIVER);
		sDBDatabase.setDbType(DB_TYPE);
		sDBDatabase.setDbURL(DB_URL);
		sDBDatabase.setDbUser(DB_USER);
		sDBDatabase.setDbPassword(DB_PASSWORD);
		
		//Create the repository namedmodel, to contain info about data repositories
		SystemDatamodel repositoryModel = new SystemDatamodel();
		repositoryModel.setId(InqleInfo.REPOSITORY_MODEL_NAME);
		repositoryModel.setDatabaseId(sDBDatabase.getId());
		
		//create the AppInfo object
		AppInfo appInfo = new AppInfo();
		//appInfo.setServerBaseUri("http://inqle.org/ns/TestServerUri");
		//appInfo.setServerBaseUrl("http://inqle.org/TestServerUrl");
//		appInfo.setMetarepositoryDataset(repositoryModel);
//		appInfo.setInternalConnection(connection);
		OntModel m = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(m);
		writer.save(appInfo);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(getAppInfoFilePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.write(fos, "N3");
		
	}
	
	public static Model getAppInfoModel() {
		log.debug("Getting AppInfo model from " + getAppInfoFilePath());
		return FileManager.get().loadModel( getAppInfoFilePath() );
	}
	
	public static AppInfo getAppInfo() {
		
		return (AppInfo)Persister.reconstitute(
				AppInfo.class, 
				AppInfo.APPINFO_INSTANCE_ID,
				getAppInfoModel(), 
				true);
	}
	
//	public static Server startDatabaseServer() throws SQLException {
//		if (server != null) {
//			return server;
//		}
//		
//		String[] args = { "-trace", "-tcp", "-web", "-pg", "-baseDir", "~" };
//		
//		server = Server.createTcpServer(args).start();
//	
//		log.info("Started H2 Database Server using these args:" + Arrays.asList(args));
//		return server;
//	}
//	
//	public static void stopDatabaseServer() {
//		server.stop();
//	}
}
