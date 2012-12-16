/**
 * 
 */
package org.inqle.core.util;

import java.io.File;


/**
 * Contains constants and other core static functions.
 * @author David Donohue
 * Nov 28, 2007
 */
public class InqleInfo {

	//Core services
	//public static final String SERVICE_FACTORY = "org.inqle.core.factory";
	
	//Attributes for services
	public static final String INQLE_HOME = "inqle.home";

	public static final String FILENAME_APPINFO = "AppInfo.ttl";

	public static final String PLUGINS_FOLDER = "plugins";

	public static final String REPOSITORY_MODEL_NAME = "org.inqle.repository.model";

	public static final String APPLICATION_TITLE = "Server Administration";

	public static final String NAME_ATTRIBUTE = "name";
	public static final String CLASS_ATTRIBUTE = "class";
	public static final String FILE_ATTRIBUTE = "file";
	public static final String WEIGHT_ATTRIBUTE = "weight";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String ID_ATTRIBUTE = "id";
	public static final String DESCRIPTION_ATTRIBUTE = "description";
	
	public static final String INQLE_VERSION = "0.3.6";

	public static final String PROJECT_WEBSITE = "http://code.google.com/p/inqle/";

	public static final String TEMP_FOLDER = "assets/temp/";

	public static final String ASSETS_FOLDER = "assets";

//	private static final int CENTRAL_INQLE_SERVER_PORT = 7700;

	//TODO use this:public static final String URL_CENTRAL_REGISTRATION_SERVICE = "http://inqle.org/register";
	public static final String URL_CENTRAL_REGISTRATION_SERVICE = "http://inqle.org:7700/register";
	public static final String URL_CENTRAL_LOOKUP_SERVICE = "http://inqle.org:7700/lookup";
	
//	public static final String URL_CENTRAL_REGISTRATION_SERVICE = "http://localhost:7700/register";
//	public static final String URL_CENTRAL_LOOKUP_SERVICE = "http://localhost:7700/lookup";

	
//	private static final String RDF_SCHEMAS_FOLDER = "assets/rdf/schemas/";
//	private static final String RDF_DATA_FOLDER = "assets/rdf/data/";

	private static final String RDF_SCHEMAS_FOLDER = "schemas";
	private static final String RDF_PREFIXES_FOLDER = "prefixes";
	private static final String RDF_DATA_FOLDER = "data";

	public static final String PARAM_SITE_ID = "site";
	public static final String PARAM_REGISTER_RDF = "rdf";
	public static final String PARAM_ACTION = "action";
	public static final String ACTION_REGISTER_PROPERTIES = "regProp";
	public static final String ACTION_REGISTER_SUBJECTS = "regSubj";
	public static final String ACTION_REGISTER_TABLEMAPPING = "regTMap";
	public static final String PARAM_INQLE_VERSION = "ver";
	public static final String PARAM_RDF_CLASS = "rdfClass";
	public static final String PARAM_DATAMODEL = "datamodel";
	public static final String PARAM_RDF_SUBJECT = "rdfSubj";
	
	public static final String DEFAULT_NAMED_MODEL_NAME = "org.inqle.models.defaultNamedModel";

	public static final String INDEXES_FOLDER = "indexes";

	private static final String RDF_FOLDER = "rdf";
	private static final String DB_FOLDER = "db/tdb";

	public static void createNeededDirectories() {
		File file = new File(getPluginsDirectory());
		file.mkdirs();
		file = new File(getAssetsDirectory());
		file.mkdirs();
		file = new File(getTempDirectory());
		file.mkdirs();
		file = new File(getRdfDirectory());
		file.mkdirs();
		file = new File(getUriPrefixesDirectory());
		file.mkdirs();
		file = new File(getDatabaseDirectory());
		file.mkdirs();
	}
	
	public static String getRootFilePath() {
		return System.getProperty(INQLE_HOME);
	}
	
	public static String getPluginsDirectory() {
		return System.getProperty(INQLE_HOME) + PLUGINS_FOLDER + "/";
	}
	
	public static String getInqleVersion() {
		return INQLE_VERSION;
	}

	public static String getTempDirectory() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getAssetsDirectory() {
		return System.getProperty(INQLE_HOME) + ASSETS_FOLDER + "/";
	}

//	public static int getServerPort() {
//		return SERVER_PORT;
//	}

	public static String getRdfDirectory() {
		return getAssetsDirectory() + RDF_FOLDER + "/";
	}
	
	@Deprecated
	public static String getRdfSchemaFilesDirectory() {
		return getAssetsDirectory() + RDF_FOLDER + "/" + RDF_SCHEMAS_FOLDER + "/";
	}
	
	@Deprecated
	public static String getRdfDataFilesDirectory() {
		return getAssetsDirectory() + RDF_FOLDER + "/" + RDF_DATA_FOLDER + "/";
	}
	
	public static String getUriPrefixesDirectory() {
		return getAssetsDirectory() + RDF_FOLDER + "/" + RDF_PREFIXES_FOLDER + "/";
	}

//	public static final String USER_DATABASE_ID = "org.inqle.core.data.general";
//	public static final String SYSTEM_DATABASE_ID = "org.inqle.core.data.system";

	public static final String ACCESS_ATTRIBUTE = "access";

	public static final String PARAM_MODEL = "model";

	public static final String DEFAULT_LANG = "en";



	public static String getDatabaseDirectory() {
		return getAssetsDirectory() + DB_FOLDER + "/";
	}

	public static final String URI_VARIABLE = "URI";

	public static final String CORE_DATABASE_ID = "_Core";
	
//	public static String getSystemDatabaseRootFilePath() {
//		return getDatabaseRootFilePath() + SYSTEM_DATABASE_ROOT + "/";
//	}
//	
//	public static String getUserDatabaseRootFilePath() {
//		return getDatabaseRootFilePath() + USER_DATABASE_ROOT + "/";
//	}
}
