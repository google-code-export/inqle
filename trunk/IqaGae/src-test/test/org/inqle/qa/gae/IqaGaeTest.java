package test.org.inqle.qa.gae;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import org.inqle.qa.Queryer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;
import com.google.inject.Guice;
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

	    @BeforeClass
	    public static void setUp() {	    	
	        helper.setUp();
	        Injector injector = Guice.createInjector(new IqaGaeTestingModule());
			queryer = injector.getInstance(Queryer.class);
	    }

	    @AfterClass
	    public static void tearDown() {
	        helper.tearDown();
	    }
	
	    @Test
		public void doNothing() {
			//do nothing
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
		
		@Test
		public void connectGoogleSpreadsheet() throws MalformedURLException, IOException, ServiceException {
			System.setProperty("http.proxyHost", "webproxy-na.dupont.com");
	    	System.setProperty("http.proxyPort", "80");
	    	System.setProperty("https.proxyHost", "webproxy-na.dupont.com");
	    	System.setProperty("https.proxyPort", "80");
			SpreadsheetService service = new SpreadsheetService("inqle.com-qa-0.1");
			service.setUserCredentials("auser@inqle.com", "pwd_auser");
			URL metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
			SpreadsheetFeed spreadsheetsFeed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = spreadsheetsFeed.getEntries();
			assertNotNull(spreadsheets);
			assert(spreadsheets.size()==1);
			SpreadsheetEntry spreadsheetEntry = null;
			for (int i = 0; i < spreadsheets.size(); i++) {
			  spreadsheetEntry = spreadsheets.get(i);
			  spreadsheetEntry.getSpreadsheetLink();
			  System.out.println("Spreadsheet ID=" + spreadsheetEntry.getId() + "; Title=" + spreadsheetEntry.getTitle().getPlainText() + "; worksheet feed URL=" + spreadsheetEntry.getSpreadsheetLink().getHref());
			}
			
			URL worksheetFeedUrl = spreadsheetEntry.getWorksheetFeedUrl();
			WorksheetFeed worksheetFeed = service.getFeed(worksheetFeedUrl, WorksheetFeed.class);
			WorksheetEntry worksheetEntry = worksheetFeed.getEntries().get(1);
			int i=0;
			for (WorksheetEntry entry : worksheetFeed.getEntries()) {
			  String currTitle = entry.getTitle().getPlainText();
//			  worksheetEntry = entry;
			  System.out.println("Worksheet # " + i + ": Title=" + currTitle);
			  i++;
			}
			
			URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
			CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
			System.out.println("CellFeed: URL=" + cellFeedUrl);
			for (CellEntry cellEntry : cellFeed.getEntries()) {
				  System.out.println(cellEntry.getTitle().getPlainText() + "=" + cellEntry.getPlainTextContent());
				  String shortId = cellEntry.getId().substring(cellEntry.getId().lastIndexOf('/') + 1);
				  System.out.println(" -- Cell(" + shortId + "/" + cellEntry.getTitle().getPlainText()
				      + ") formula(" + cellEntry.getCell().getInputValue() + ") numeric("
				      + cellEntry.getCell().getNumericValue() + ") value("
				      + cellEntry.getCell().getValue() + ")");
				}
			
//			URL iqaObjectsUrl = new URL("http://spreadsheets.google.com/feeds/worksheets/ttsuVNlppfKmjNer07Q0Teg/private/full");
//			WorksheetFeed worksheetsFeed = service.getFeed(iqaObjectsUrl, WorksheetFeed.class);
//			assertNotNull(worksheetsFeed);
//			List<WorksheetEntry> worksheets = worksheetsFeed.getEntries();
//			assertNotNull(worksheets);
//			assert(worksheets.size()>1);
//			for (int i = 0; i < worksheets.size(); i++) {
//			  WorksheetEntry entry = worksheets.get(i);
//			  System.out.println("\tWorksheet ID=" + entry.getId() + "; Title=" + entry.getTitle().getPlainText() + "; toString=" + entry);
//			}
		}
}
