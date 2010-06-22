package org.inqle.qa.gae;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;

import org.inqle.qa.Queryer;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.servlet.SessionScoped;

public class IqaGaeModule extends AbstractModule implements Module {

	private static final String CONFIGURATION_FOLDER = "WEB-INF/config";
	private static final String PROPERTIES_FILE = "iqa.properties";
	

	@Override
	protected void configure() {
		bind(Queryer.class).to(GaeQueryer.class);
	}
	
	@Provides
	@SessionScoped
	EntityManagerFactory provideEmf() {
		EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
		return emfInstance;
	}
	
	@Provides
	@SessionScoped
	PersistenceManagerFactory providePmf() {
		PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		return pmfInstance;
	}
	

	@Provides
	@SessionScoped
	@AppConfig
	Properties getAppConfig(ServletContext servletContext) {
		Properties properties = new Properties();
		String realPath = servletContext.getRealPath("");
		String fullFileName = realPath + "/" + CONFIGURATION_FOLDER + "/" + PROPERTIES_FILE;
//		System.out.println("Loading application properties.  fullFileName=" + fullFileName);
		try {
			FileInputStream fis = new FileInputStream(fullFileName);
			properties.load(fis); 
		} catch (IOException e) {
			System.out.println("Unable to find configuration file: " + fullFileName);
			return null;
		}
		if (properties==null) {
			System.out.println("Properties is null");
			return null;
		}
		return properties;
	}
}
