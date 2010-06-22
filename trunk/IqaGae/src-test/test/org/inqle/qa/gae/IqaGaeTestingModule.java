package test.org.inqle.qa.gae;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.inqle.qa.Queryer;
import org.inqle.qa.gae.GaeQueryer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.SessionScoped;


public class IqaGaeTestingModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Queryer.class).to(GaeQueryer.class);
	}
	
	@Provides
//	@SessionScoped
	EntityManagerFactory provideEmf() {
		EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
		return emfInstance;
	}
	
	@Provides
//	@SessionScoped
	PersistenceManagerFactory providePmf() {
		PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		return pmfInstance;
	}
}
