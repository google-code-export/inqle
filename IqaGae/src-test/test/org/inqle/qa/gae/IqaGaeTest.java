package test.org.inqle.qa.gae;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.AppConstants;
import org.inqle.qa.Queryer;
import org.inqle.qa.gae.AppConfig;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class IqaGaeTest {

	private static Queryer queryer;
	
//	@Before
//	public void setUp() throws Exception {
//		Injector injector = Guice.createInjector(new IqaGaeTestingModule());
//		queryer = injector.getInstance(Queryer.class);		
//	}
	
	 	private final static LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

		private static SpreadsheetService spreadsheetService;

		private static GdataSpreadsheetImporter gdataSpreadsheetImporter;

		private static Logger log;

		@Inject
		@AppConfig
		private Properties appProps;
		
	    @BeforeClass
	    public static void setUp() {	    	
	        helper.setUp();
	        Injector injector = Guice.createInjector(new IqaGaeTestingModule());
	        log = injector.getInstance(Logger.class);
	        log.setLevel(Level.FINE);
			queryer = injector.getInstance(Queryer.class);
			spreadsheetService = injector.getInstance(SpreadsheetService.class);
			gdataSpreadsheetImporter = injector.getInstance(GdataSpreadsheetImporter.class);
	    }

	    @AfterClass
	    public static void tearDown() {
	        helper.tearDown();
	    }
	
//	    @Test
		public void doNothing() {
			//do nothing
		}
	    
//		@Test
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
		
//		@Test
		public void connectGoogleSpreadsheet() throws MalformedURLException, IOException, ServiceException {
//			SpreadsheetService service = new SpreadsheetService("inqle.com-qa-0.1");
//			service.setUserCredentials(
//					appProps.getProperty(AppConstants.PROP_GOOGLE_SPREADSHEET_ACCOUNT),
//					appProps.getProperty(AppConstants.PROP_GOOGLE_SPREADSHEET_PASSWORD));
			URL metafeedUrl = new URL(AppConstants.GOOGLE_SPREADSHEETS_METAFEED);
			SpreadsheetFeed spreadsheetsFeed = spreadsheetService.getFeed(metafeedUrl, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = spreadsheetsFeed.getEntries();
			assertNotNull(spreadsheets);
			assert(spreadsheets.size()==1);
			SpreadsheetEntry spreadsheetEntry = null;
			for (int i = 0; i < spreadsheets.size(); i++) {
			  spreadsheetEntry = spreadsheets.get(i);
			  spreadsheetEntry.getSpreadsheetLink();
			  log.info("Spreadsheet ID=" + spreadsheetEntry.getId() + "; Title=" + spreadsheetEntry.getTitle().getPlainText() + "; worksheet feed URL=" + spreadsheetEntry.getWorksheetFeedUrl());
			}
			
			URL worksheetFeedUrl = spreadsheetEntry.getWorksheetFeedUrl();
			WorksheetFeed worksheetFeed = spreadsheetService.getFeed(worksheetFeedUrl, WorksheetFeed.class);
			WorksheetEntry worksheetEntry = worksheetFeed.getEntries().get(1);
			int i=0;
			for (WorksheetEntry entry : worksheetFeed.getEntries()) {
			  String currTitle = entry.getTitle().getPlainText();
//			  worksheetEntry = entry;
			  log.info("Worksheet # " + i + ": Title=" + currTitle);
			  i++;
			}
			
			URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
			CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrl, CellFeed.class);
			log.info("CellFeed: URL=" + cellFeedUrl);
			for (CellEntry cellEntry : cellFeed.getEntries()) {
				  log.info(cellEntry.getTitle().getPlainText() + "=" + cellEntry.getPlainTextContent());
				  String shortId = cellEntry.getId().substring(cellEntry.getId().lastIndexOf('/') + 1);
				  log.info(" -- Cell(" + shortId + "/" + cellEntry.getTitle().getPlainText()
				      + ") formula(" + cellEntry.getCell().getInputValue() + ") numeric("
				      + cellEntry.getCell().getNumericValue() + ") value("
				      + cellEntry.getCell().getValue() + ")");
				}
		}
		
		@Test
		public void testGdataSpreadsheetImporter() throws IOException, ServiceException {
			gdataSpreadsheetImporter.importSpreadsheet("https://spreadsheets.google.com/feeds/worksheets/ttsuVNlppfKmjNer07Q0Teg/private/full");
		}
}
