package test.org.inqle.qa.gae;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Answer;
import org.inqle.qa.AnswerBroker;
import org.inqle.qa.AppConstants;
import org.inqle.qa.Preference;
import org.inqle.qa.PreferenceBroker;
import org.inqle.qa.QuestionBroker;
import org.inqle.qa.Queryer;
import org.inqle.qa.Question;
import org.inqle.qa.QuestionRuleApplier;
import org.inqle.qa.gae.AppConfig;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;
import org.inqle.qa.gdata.gae.GaeGdataSpreadsheetImporter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
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

public class TestIqaGaeBasic {

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

		private static DatastoreService datastoreService;

		private static QuestionBroker questionFactory;
		
		private static QuestionRuleApplier questionRuleApplier;

		private static AnswerBroker answerBroker;

		private static PreferenceBroker preferenceBroker;
		
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
			datastoreService = injector.getInstance(DatastoreService.class); 
			questionFactory = injector.getInstance(QuestionBroker.class);
			questionRuleApplier = injector.getInstance(QuestionRuleApplier.class);
			answerBroker = injector.getInstance(AnswerBroker.class);
			preferenceBroker = injector.getInstance(PreferenceBroker.class);
	    }

	    @AfterClass
	    public static void tearDown() {
	        helper.tearDown();
	    }
	
	    @Test
	    public void testIsShortUri() {
	    	assertTrue(GaeGdataSpreadsheetImporter.isShortUri("stooge1:whateVer"));
	    	assertTrue(GaeGdataSpreadsheetImporter.isShortUri("stooge_1:whate-Ver"));
	    	assertTrue(GaeGdataSpreadsheetImporter.isShortUri("inqleqa:UOM/id/dl"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("inqle/qa:UOM/id/dl"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("stooge_1::whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("stooge_1:whate-Ver "));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("stooge_1:whate:Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri(" stooge1:whateVer"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("stooge_1 whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("stooge 1:whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("stooge_1:whate Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri("whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri(""));
	    	assertFalse(GaeGdataSpreadsheetImporter.isShortUri(null));
	    }
	    
	    @Test
	    public void testLocalizedString() {
	    	assertTrue(GaeGdataSpreadsheetImporter.isLocalizedString("sto=whateVer"));
	    	assertTrue(GaeGdataSpreadsheetImporter.isLocalizedString("st-oo=sto=whateVer"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("st-oosto=whateVer"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("stooge_1::whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("stooge_1 whate-Ver "));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("stooge_1=whate:Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString(" st=whatever"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("sg =whatever"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("stooge 1:whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("stooge_1:whate Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString("whate-Ver"));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString(""));
	    	assertFalse(GaeGdataSpreadsheetImporter.isLocalizedString(null));
	    }
	    
//		@Test
		public void queryerStoreAndLoadObjects() {
			Logger log = Logger.getLogger(TestIqaGaeBasic.class.getName());
			
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
//		public void connectGoogleSpreadsheet() throws MalformedURLException, IOException, ServiceException {
//			URL metafeedUrl = new URL(AppConstants.GOOGLE_SPREADSHEETS_METAFEED);
//			SpreadsheetFeed spreadsheetsFeed = spreadsheetService.getFeed(metafeedUrl, SpreadsheetFeed.class);
//			List<SpreadsheetEntry> spreadsheets = spreadsheetsFeed.getEntries();
//			assertNotNull(spreadsheets);
//			assert(spreadsheets.size()==1);
//			SpreadsheetEntry spreadsheetEntry = null;
//			for (int i = 0; i < spreadsheets.size(); i++) {
//			  spreadsheetEntry = spreadsheets.get(i);
//			  spreadsheetEntry.getSpreadsheetLink();
//			  log.info("Spreadsheet ID=" + spreadsheetEntry.getId() + "; Title=" + spreadsheetEntry.getTitle().getPlainText() + "; worksheet feed URL=" + spreadsheetEntry.getWorksheetFeedUrl());
//			}
//			
//			URL worksheetFeedUrl = spreadsheetEntry.getWorksheetFeedUrl();
//			WorksheetFeed worksheetFeed = spreadsheetService.getFeed(worksheetFeedUrl, WorksheetFeed.class);
//			WorksheetEntry worksheetEntry = worksheetFeed.getEntries().get(1);
//			int i=0;
//			for (WorksheetEntry entry : worksheetFeed.getEntries()) {
//			  String currTitle = entry.getTitle().getPlainText();
//			  log.info("Worksheet # " + i + ": Title=" + currTitle);
//			  i++;
//			}
//			
//			URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
//			CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrl, CellFeed.class);
//			log.info("CellFeed: URL=" + cellFeedUrl);
//			for (CellEntry cellEntry : cellFeed.getEntries()) {
//				  log.info(cellEntry.getTitle().getPlainText() + "=" + cellEntry.getPlainTextContent());
//				  String shortId = cellEntry.getId().substring(cellEntry.getId().lastIndexOf('/') + 1);
//				  log.info(" -- Cell(" + shortId + "/" + cellEntry.getTitle().getPlainText()
//				      + ") formula(" + cellEntry.getCell().getInputValue() + ") numeric("
//				      + cellEntry.getCell().getNumericValue() + ") value("
//				      + cellEntry.getCell().getValue() + ")");
//				}
//		}
		
		@Test
		public void testGdataSpreadsheetImporter() throws IOException, ServiceException, EntityNotFoundException {
			gdataSpreadsheetImporter.importSpreadsheet("https://spreadsheets.google.com/feeds/worksheets/ttsuVNlppfKmjNer07Q0Teg/private/full");
			Entity q1 = datastoreService.get(KeyFactory.createKey("Question", "Weight"));
			assertNotNull(q1);
			log.info("q1=" + q1);
			Query findQuestionsQuery = new Query("Question");
//			findQuestionsQuery.addFilter("gender", FilterOperator.EQUAL, "female");
			findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
			List<Entity> r1 = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withLimit(500));
			assertNotNull(r1);
			log.info("First 500 questions=" + r1);
			assert(r1.size() == 20);
			Entity e1_1 = r1.get(0);
			log.info("Queried questions.  Top priority question = " + e1_1);
			assert(e1_1.getKey().toString().equals("weight"));
		
			//load localized strings
			Query lsQuery = new Query("LocalizedString");
			lsQuery.setAncestor(q1.getKey());
			List<Entity> rls = datastoreService.prepare(lsQuery).asList(FetchOptions.Builder.withLimit(500));
			assertNotNull(rls);
			log.info("Localized strings for question 1=" + rls);
			assertEquals(5, rls.size());
			
			//load Mappings
			Query mappingQuery = new Query("Mapping");
			mappingQuery.setAncestor(q1.getKey());
			List<Entity> rmapping = datastoreService.prepare(mappingQuery).asList(FetchOptions.Builder.withLimit(500));
			assertNotNull(rmapping);
			log.info("Mappings for question 1=" + rmapping);
			assertEquals(0, rmapping.size());
		}
		
		@Test
		public void testBuildingAskableQuestions() {
			Question askableQuestion1 = questionFactory.getQuestion(KeyFactory.createKey("Question", "Weight"), "en");
			log.info("askableQuestion1=" + askableQuestion1);
			assert(askableQuestion1.getQuestionText() != null);
			
			Question askableQuestion2 = questionFactory.getQuestion(KeyFactory.createKey("Question", "Height"), "en");
			log.info("askableQuestion2=" + askableQuestion2);
			assert(askableQuestion2.getQuestionText() != null);
			
			Question askableQuestion3 = questionFactory.getQuestion(KeyFactory.createKey("Question", "Gender"), "en");
			log.info("askableQuestion3=" + askableQuestion3);
			assert(askableQuestion3.getQuestionText().equals("What is your sex?"));
		}
		
//		@Test
		public void testApplyQuestionRulesAmidstAnswerEntities() {
			String userId = "dummy";
			List<Question> applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			log.info("Found these askable questions: " + applicableAskableQuestions);
			assertEquals(20, applicableAskableQuestions.size());
			
			Key userKey = KeyFactory.createKey("Person", userId);
			
			Entity answer = new Entity("Answer", "1001", userKey);
			answer.setProperty("question", "Height");
			answer.setProperty("user", userId);
			answer.setProperty("date", new Date());
			datastoreService.put(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			answer = new Entity("Answer", "1002", userKey);
			answer.setProperty("question", "Weight");
			answer.setProperty("user", userId);
			answer.setProperty("date", new Date());
			datastoreService.put(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(18, applicableAskableQuestions.size());
			
			answer = new Entity("Answer", "1003", userKey);
			answer.setProperty("question", "WaistCircumference");
			answer.setProperty("user", userId);
			answer.setProperty("date", new Date());
			datastoreService.put(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(17, applicableAskableQuestions.size());
			
			answer = new Entity("Answer", "1004", userKey);
			answer.setProperty("question", "Gender");
			answer.setProperty("user", userId);
			answer.setProperty("wrong_date_field", new Date());
			datastoreService.put(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(17, applicableAskableQuestions.size());
			
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			Entity preference = new Entity("Preference", "moratoriumUntil/Question/HoursOfExercise", userKey);
			preference.setProperty("question", "HoursOfExercise");
			preference.setProperty("user", userId);
			preference.setProperty("moratoriumUntil", c.getTime());
			datastoreService.put(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(17, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			preference = new Entity("Preference", "moratoriumUntil/Question/HoursOfExercise", userKey);
			preference.setProperty("question", "HoursOfExercise");
			preference.setProperty("user", userId);
			preference.setProperty("moratoriumUntil", c.getTime());
			datastoreService.put(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -100);
			preference = new Entity("Preference", "moratoriumUntil/Question/HoursOfExercise", userKey);
			preference.setProperty("question", "HoursOfExercise");
			preference.setProperty("user", userId);
			preference.setProperty("moratoriumUntil", c.getTime());
			datastoreService.put(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(17, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 100000000);
			preference = new Entity("Preference", "moratoriumUntil/Question/AvgDailyFruitVeg", userKey);
			preference.setProperty("question", "AvgDailyFruitVeg");
			preference.setProperty("user", userId);
			preference.setProperty("moratoriumUntil", c.getTime());
			datastoreService.put(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
		}
		
		@Test
		public void testApplyQuestionRulesAmidstAnswers() {
			String userId = "dummy";
			List<Question> applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			log.info("Found these askable questions: " + applicableAskableQuestions);
			assertEquals(20, applicableAskableQuestions.size());
			
			Key userKey = KeyFactory.createKey("Person", userId);
			
			Answer answer = new Answer();
			answer.setId("1001");
			answer.setQuestion("Height");
			answer.setUser(userId);
			answer.setDate(new Date());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			answer = new Answer();
			answer.setId("1002");
			answer.setQuestion("Weight");
			answer.setUser(userId);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -5);
			answer.setDate(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			answer = new Answer();
			answer.setId("1003");
			answer.setQuestion("Weight");
			answer.setUser(userId);
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			answer.setDate(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			answer = new Answer();
			answer.setId("1004");
			answer.setQuestion("Weight");
			answer.setUser(userId);
			c = Calendar.getInstance();
			c.add(Calendar.HOUR, -25);
			answer.setDate(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
//			
			answer = new Answer();
			answer.setId("1005");
			answer.setQuestion("Weight");
			answer.setUser(userId);
			c = Calendar.getInstance();
			c.add(Calendar.HOUR, -23);
			answer.setDate(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(18, applicableAskableQuestions.size());
			
			answer = new Answer();
			answer.setId("1006");
			answer.setQuestion("WaistCircumference");
			answer.setUser(userId);
			answer.setDate(new Date());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(17, applicableAskableQuestions.size());

			answer = new Answer();
			answer.setId("1007");
			answer.setQuestion("Gender");
			answer.setUser(userId);
			answer.setDate(new Date());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			Preference preference = new Preference();
			preference.setId("1001");
			preference.setQuestion("HoursOfExercise");
			preference.setUser(userId);
			preference.setMoratoriumUntil(new Date());
			preferenceBroker.storePreference(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			preference = new Preference();
			preference.setId("1001");
			preference.setQuestion("HoursOfExercise");
			preference.setUser(userId);
			preference.setMoratoriumUntil(c.getTime());
			preferenceBroker.storePreference(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(15, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -100);
			preference = new Preference();
			preference.setId("1001");
			preference.setQuestion("HoursOfExercise");
			preference.setUser(userId);
			preference.setMoratoriumUntil(c.getTime());
			preferenceBroker.storePreference(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -100);
			preference = new Preference();
			preference.setId("1002");
			preference.setQuestion("AvgDailyFruitVeg");
			preference.setUser(userId);
			preference.setMoratoriumUntil(c.getTime());
			preferenceBroker.storePreference(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 100);
			preference = new Preference();
			preference.setId("1002");
			preference.setQuestion("AvgDailyFruitVeg");
			preference.setUser(userId);
			preference.setMoratoriumUntil(c.getTime());
			preferenceBroker.storePreference(preference);
			applicableAskableQuestions = questionRuleApplier.getApplicableQuestions(userId, "en");
			assertEquals(15, applicableAskableQuestions.size());
		}
}
