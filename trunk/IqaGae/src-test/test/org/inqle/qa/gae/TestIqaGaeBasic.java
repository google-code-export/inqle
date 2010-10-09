package test.org.inqle.qa.gae;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Answer;
import org.inqle.qa.AnswerBroker;
import org.inqle.qa.AppConstants;
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
//		public void testApplyQuestionRulesAmidstAnswerEntities() {
//			String userId = "dummy";
//			List<Question> applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			log.info("Found these askable questions: " + applicableAskableQuestions);
//			assertEquals(20, applicableAskableQuestions.size());
//			
//			Key userKey = KeyFactory.createKey("Person", userId);
//			
//			Entity answer = new Entity("Answer", "1001", userKey);
//			answer.setProperty("question", "Height");
//			answer.setProperty("user", userId);
//			answer.setProperty("date", new Date());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(19, applicableAskableQuestions.size());
//			
//			answer = new Entity("Answer", "1002", userKey);
//			answer.setProperty("question", "Weight");
//			answer.setProperty("user", userId);
//			answer.setProperty("date", new Date());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(18, applicableAskableQuestions.size());
//			
//			answer = new Entity("Answer", "1003", userKey);
//			answer.setProperty("question", "WaistCircumference");
//			answer.setProperty("user", userId);
//			answer.setProperty("date", new Date());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(17, applicableAskableQuestions.size());
//			
//			answer = new Entity("Answer", "1004", userKey);
//			answer.setProperty("question", "Gender");
//			answer.setProperty("user", userId);
//			answer.setProperty("wrong_date_field", new Date());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(17, applicableAskableQuestions.size());
//			
//			Calendar c = Calendar.getInstance();
//			c.add(Calendar.DATE, -1);
//			answer = new Entity("Answer", "moratoriumUntil/Question/HoursOfExercise", userKey);
//			answer.setProperty("question", "HoursOfExercise");
//			answer.setProperty("user", userId);
//			answer.setProperty("moratoriumUntil", c.getTime());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(17, applicableAskableQuestions.size());
//			
//			c = Calendar.getInstance();
//			c.add(Calendar.DATE, 1);
//			answer = new Entity("Answer", "moratoriumUntil/Question/HoursOfExercise", userKey);
//			answer.setProperty("question", "HoursOfExercise");
//			answer.setProperty("user", userId);
//			answer.setProperty("moratoriumUntil", c.getTime());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(16, applicableAskableQuestions.size());
//			
//			c = Calendar.getInstance();
//			c.add(Calendar.DATE, -100);
//			answer = new Entity("Answer", "moratoriumUntil/Question/HoursOfExercise", userKey);
//			answer.setProperty("question", "HoursOfExercise");
//			answer.setProperty("user", userId);
//			answer.setProperty("moratoriumUntil", c.getTime());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(17, applicableAskableQuestions.size());
//			
//			c = Calendar.getInstance();
//			c.add(Calendar.DATE, 100000000);
//			answer = new Entity("Answer", "moratoriumUntil/Question/AvgDailyFruitVeg", userKey);
//			answer.setProperty("question", "AvgDailyFruitVeg");
//			answer.setProperty("user", userId);
//			answer.setProperty("moratoriumUntil", c.getTime());
//			datastoreService.put(answer);
//			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
//			assertEquals(16, applicableAskableQuestions.size());
//		}
		
		@Test
		public void testApplyQuestionRulesAmidstAnswers() {
			String userId = "dummy";
			List<Question> applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			log.info("Found these askable questions: " + applicableAskableQuestions);
			assertEquals(20, applicableAskableQuestions.size());
			
			Key userKey = KeyFactory.createKey("Person", userId);
			
			Answer answer = new Answer(userId, "Height");
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -5);
			answer = new Answer(userId, "Weight", c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -2);
			answer = new Answer(userId, "Weight", c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.HOUR, -25);
			answer = new Answer(userId, "Weight", c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(19, applicableAskableQuestions.size());
		
			c = Calendar.getInstance();
			c.add(Calendar.HOUR, -23);
			answer = new Answer(userId, "Weight", c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(18, applicableAskableQuestions.size());
			
			answer = new Answer(userId, "WaistCircumference");
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(17, applicableAskableQuestions.size());

			answer = new Answer(userId, "Gender");
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			answer = new Answer(userId, "HoursOfExercise");
			answer.setMoratoriumUntil(new Date());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			answer = new Answer(userId, "HoursOfExercise");
			answer.setMoratoriumUntil(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(15, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -100);
			answer = new Answer(userId, "HoursOfExercise");
			answer.setMoratoriumUntil(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, -100);
			answer = new Answer(userId, "AvgDailyFruitVeg");
			answer.setMoratoriumUntil(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(16, applicableAskableQuestions.size());
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 100);
			answer = new Answer(userId, "AvgDailyFruitVeg");
			answer.setMoratoriumUntil(c.getTime());
			answerBroker.storeAnswer(answer);
			applicableAskableQuestions = questionRuleApplier.listAllApplicableQuestions(userId, "en");
			assertEquals(15, applicableAskableQuestions.size());
		}
		
		@Test
		public void testGet5Questions() {
			List<Question> allQuestions = questionFactory.listAllQuestions("en");
			List<Question> questionsToAsk = questionRuleApplier.listTopPlusRandomQuestions("en", 5, 1);
			log.info("questionsToAsk=" + questionsToAsk);
			assertEquals(5, questionsToAsk.size());
			Question q1 = questionsToAsk.get(0);
			assertEquals("Weight", q1.getId());
			
			//generate 1000 different lists of 5 questions
			Set<String> allQuestionsEverAsked = new HashSet<String>();
			for (int j=0; j<1000; j++) {
				questionsToAsk = questionRuleApplier.listTopPlusRandomQuestions("en", 5, 1);
				
				int i=0;
				List<String> qids = new ArrayList<String>();
				for (Question q: questionsToAsk) {
					allQuestionsEverAsked.add(q.getId());
					assertFalse(qids.contains(q.getId()));
					qids.add(q.getId());
					i++;
				}
			}
			log.info("allQuestionsEverAsked=" + allQuestionsEverAsked);
			assertEquals(allQuestions.size(), allQuestionsEverAsked.size());
		}
}
