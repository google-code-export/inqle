package org.inqle.qa;

import java.util.Properties;

public class AppConfigFromProperties implements AppConfig {

	private Properties properties;

	public AppConfigFromProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
}
