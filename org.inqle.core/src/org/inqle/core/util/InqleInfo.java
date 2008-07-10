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
	
	public static final String INQLE_VERSION = "0.0.9";

	public static final String PROJECT_WEBSITE = "http://code.google.com/p/inqle/";

	public static final String TEMP_FOLDER = "assets/temp/";

	public static final String ASSETS_FOLDER = "assets";

	private static final int SERVER_PORT = 7070;
	
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
}
