package org.inqle.qa;

import java.util.Properties;


public class AppConfig {
	private Properties properties = new Properties();

	public AppConfig(Properties properties) {
		this.properties  = properties;
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
