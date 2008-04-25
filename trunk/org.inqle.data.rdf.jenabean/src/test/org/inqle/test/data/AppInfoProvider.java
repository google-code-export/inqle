package org.inqle.test.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.RDBModel;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Bean2RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class AppInfoProvider {

	public static final String APP_HOME = "C:/workspace/";
	public static final String FILENAME_APPINFO = "org.inqle.data.rdf.jenabean/src/test/secure/AppInfo.ttl";
	private static final String DB_DRIVER = "org.postgresql.Driver";
	private static final String DB_TYPE = "PostgreSQL";
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/test_inqle_repositories";
	private static final String DB_USER = "inqle";
	private static final String DB_PASSWORD = "~Arden";
	
	public static Logger log = Logger.getLogger(AppInfoProvider.class);
	
	public static String getAppInfoFilePath() {
		return APP_HOME + FILENAME_APPINFO;
	}
	
	public static void createAppInfo() {
		//create the connection object, containing db connection info for the repository namedmodel
		Connection connection = new Connection();
		connection.setDbClass(DB_DRIVER);
		connection.setDbType(DB_TYPE);
		connection.setDbURL(DB_URL);
		connection.setDbUser(DB_USER);
		connection.setDbPassword(DB_PASSWORD);
		
		//Create the repository namedmodel, to contain info about data repositories
		RDBModel repositoryModel = new RDBModel();
		repositoryModel.setModelName(InqleInfo.REPOSITORY_MODEL_NAME);
		repositoryModel.setConnection(connection);
		
		//create the AppInfo object
		AppInfo appInfo = new AppInfo();
		//appInfo.setServerBaseUri("http://inqle.org/ns/TestServerUri");
		appInfo.setServerBaseUrl("http://inqle.org/TestServerUrl");
		appInfo.setRepositoryNamedModel(repositoryModel);
		
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
				AppInfo.APPINFO_INSTANCE,
				getAppInfoModel(), 
				true);
	}
}
