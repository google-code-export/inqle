package test.org.inqle.qa.gae;


import java.util.List;
import java.util.logging.Logger;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import static org.junit.Assert.*;

import org.inqle.qa.Queryer;
import org.inqle.qa.gae.IqaGaeModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class IqaGaeTest {

	private Queryer queryer;
	
//	@Before
//	public void setUp() throws Exception {
//		Injector injector = Guice.createInjector(new IqaGaeTestingModule());
//		queryer = injector.getInstance(Queryer.class);		
//	}
	
	 private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	    @Before
	    public void setUp() {
	        helper.setUp();
	        Injector injector = Guice.createInjector(new IqaGaeTestingModule());
			queryer = injector.getInstance(Queryer.class);
	    }

	    @After
	    public void tearDown() {
	        helper.tearDown();
	    }
	
	@Test
	public void queryerStoreAndLoadObjects() {
		Logger log = Logger.getLogger(IqaGaeTest.class.getName());
		
		List<Persistme> persistmesBeforeStoring = queryer.getList(Persistme.class, null);
//		assertNull(persistmesBeforeStoring);
		assert(persistmesBeforeStoring.size()==0);
		
		String[] vals = {"first", "second", "third", "fourth", "fifth"};
		for (String val: vals) {
			Persistme persistme = new Persistme(val);
			queryer.store(persistme);
		}
		
		List<Persistme> persistmes = queryer.getList(Persistme.class, null);
		log.info("Retrieved persistmes: " + persistmes);
		
		assertNotNull(persistmes);
		assert(persistmes.size()==5);
	}
}
