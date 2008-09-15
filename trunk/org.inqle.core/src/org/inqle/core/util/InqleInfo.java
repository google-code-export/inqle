/**
 * 
 */
package org.inqle.core.util;

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
	
	public static final String INQLE_VERSION = "0.1.3";

	public static final String PROJECT_WEBSITE = "http://code.google.com/p/inqle/";

	public static final String TEMP_FOLDER = "assets/temp/";

	public static final String ASSETS_FOLDER = "assets";

	private static final int SERVER_PORT = 7070;

	//TODO use this:public static final String URL_CENTRAL_REGISTRATION_SERVICE = "http://inqle.org/register";
	public static final String URL_CENTRAL_REGISTRATION_SERVICE = "http://localhost/register";
	public static final String URL_CENTRAL_LOOKUP_SERVICE = "http://localhost/lookup";
	
	public static final String PARAM_SITE_ID = "site";

	public static final String PARAM_REGISTER_RDF = "rdf";

	public static final String PARAM_INQLE_VERSION = "ver";

	public static final String PARAM_SEARCH_DATA_SUBJECT = "searchDataSubject";

	public static final String PARAM_SEARCH_RDF_CLASS = "searchRdfClass";
	
//	public static final String PARAM_SEARCH_ALL_DATA_CLASSES = "searchAllDataClasses";
	
	public static final String PARAM_SEARCH_START_INDEX = "start";

	public static final String PARAM_SEARCH_COUNT_RESULTS = "count";

	public static final String PARAM_DATA_AND_SUBJECT_PROPERTIES_OF_SUBJECT = "dsPropertiesOfSubject";
	
	public static final String PARAM_PROPERTIES_OF_SUBJECT_FROM_SCHEMA_FILES = "propertiesOfSubjectFromSchema";

	private static final String RDF_SCHEMAS_FOLDER = "assets/rdf/schemas/";

	private static final String RDF_DATA_FOLDER = "assets/rdf/data/";

	public static final String DEFAULT_NAMED_MODEL_NAME = "org.inqle.models.defaultNamedModel";
	
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

	public static int getServerPort() {
		return SERVER_PORT;
	}

	public static String getRdfSchemaFilesDirectory() {
		return System.getProperty(INQLE_HOME) + RDF_SCHEMAS_FOLDER + "/";
	}
	
	public static String getRdfDataFilesDirectory() {
		return System.getProperty(INQLE_HOME) + RDF_DATA_FOLDER + "/";
	}
}