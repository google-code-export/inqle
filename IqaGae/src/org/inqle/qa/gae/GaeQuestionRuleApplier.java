package org.inqle.qa.gae;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.inqle.qa.Question;
import org.inqle.qa.QuestionBroker;
import org.inqle.qa.QuestionRuleApplier;
import org.inqle.qa.Rule;
import org.inqle.qa.RuleApplier;
import org.inqle.qa.RuleFactory;
import org.mortbay.log.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.inject.Inject;

public class GaeQuestionRuleApplier implements QuestionRuleApplier {

	
	private DatastoreService datastoreService;
	private QuestionBroker questionFactory;
	private RuleApplier ruleApplier;
	private RuleFactory ruleFactory;
	private Logger log;

	@Inject
	public GaeQuestionRuleApplier(Logger log, DatastoreService datastoreService, QuestionBroker questionFactory, RuleApplier ruleApplier, RuleFactory ruleFactory) {
		this.datastoreService = datastoreService;
		this.questionFactory = questionFactory;
		this.ruleApplier = ruleApplier;
		this.ruleFactory = ruleFactory;
		this.log = log;
	}
	
	@Override
	public List<Question> listAllApplicableQuestions(String userId, String lang) {
		List<Question> questions = new ArrayList<Question>();
		List<Entity> allQuestionEntities = listAllQuestionEntities();
		for (Entity questionEntity: allQuestionEntities) {
			if (shouldAskQuestion(userId, questionEntity)) {
				Question question = questionFactory.getQuestion(questionEntity.getKey(), lang);
				questions.add(question);
			}
		}
		return questions;
	}

	private List<Entity> listAllQuestionEntities() {
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		return datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withLimit(500));
	}

	/**
	 * If the question has not been asked too recently and if its rules apply, then return true.  
	 * Otherwise return false
	 * @param userId
	 * @param questionEntity
	 * @return
	 */
	private boolean shouldAskQuestion(String userId, Entity questionEntity) {
		if (questionEntity==null) return false;
		//test whether the question has been answered too recently
		Query recentAnswersQuery = new Query("Answer");
		Key userKey = KeyFactory.createKey("Person", userId);
		recentAnswersQuery.setAncestor(userKey);
		recentAnswersQuery.addFilter("question", FilterOperator.EQUAL, questionEntity.getKey().getName());
		Date latestAcceptableDate = new Date();
		Calendar c = Calendar.getInstance();
		Double minInterval = (Double)questionEntity.getProperty("minInterval");
		int minIntervalInt = minInterval.intValue();
		c.add(Calendar.DAY_OF_MONTH, minIntervalInt * -1);
		latestAcceptableDate = c.getTime();
		recentAnswersQuery.addFilter("date", FilterOperator.GREATER_THAN_OR_EQUAL, latestAcceptableDate);
		int countRecentAnswers = datastoreService.prepare(recentAnswersQuery).countEntities();
		if (countRecentAnswers > 0) {
			log.info("User: " + userId + " already answered question: " + questionEntity.getKey().getName() + " since " + latestAcceptableDate);
			//latest response was too recent
			return false;
		}
		
		//test whether the user has said not to ask again
		Query moratoriumPreferencesQuery = new Query("Preference");
		moratoriumPreferencesQuery.setAncestor(userKey);
		moratoriumPreferencesQuery.addFilter("question", FilterOperator.EQUAL, questionEntity.getKey().getName());
		moratoriumPreferencesQuery.addFilter("moratoriumUntil", FilterOperator.GREATER_THAN, new Date());
		int countDontAskAnswers = datastoreService.prepare(moratoriumPreferencesQuery).countEntities();
		if (countDontAskAnswers > 0) {
			log.info("User: " + userId + " has said don't ask question: " + questionEntity.getKey().getName());
			//user has said "don't ask this"
			return false;
		}

		//TODO test rules!
		Object rulesObj = questionEntity.getProperty("rules");
		if (rulesObj == null) {
			//no rules, return true
			return true;
		}
		
		List<String> ruleIds = new ArrayList<String>();
		if (rulesObj instanceof String) {
			ruleIds.add((String)rulesObj);
		} else if (rulesObj instanceof List<?>){
			for (Object ruleObj: (List<?>)rulesObj) {
				ruleIds.add(String.valueOf(ruleObj));
			}
		}
		
		//loop thru each rule, return true if any rule is true
		for (String ruleId: ruleIds) {
			Key ruleKey = KeyFactory.createKey("Rule", ruleId);
			Rule rule = ruleFactory.getRule(ruleKey);
			if (ruleApplier.applyRule(rule, userId)) {
				return true;
			}
		}
		
		//rules present but none returns true: return false
		return false;
	}

	/**
	 * If the question has not been asked too recently and if its rules apply, then return true.  
	 * Otherwise return false
	 * @param userId
	 * @param questionEntity
	 * @return
	 */
	private boolean shouldAskQuestion(String userId, Entity questionEntity, Entity questionHistoryEntity) {
		
	}
	
	@Override
	public List<Question> listTopQuestions(String lang, int numberOfQuestions) {
		List<Question> topAskableQuestions = new ArrayList<Question>();
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allQuestionEntities = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withLimit(numberOfQuestions));
		for (Entity questionEntity: allQuestionEntities) {
			Question question = questionFactory.getQuestion(questionEntity, lang);
			topAskableQuestions.add(question);
		}
		return topAskableQuestions;
	}

	@Override
	/*
	 * TODO: ensure we can reach all questions with a single query (max for GAE datastore = 500 per query?)
	 */
	public List<Question> listRandomQuestions(String lang, int numberOfQuestions) {
		List<Question> randomQuestions = new ArrayList<Question>();
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allQuestionEntities = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withDefaults());
		
		List<Integer> randomIndexes = getRandomIndexesList(numberOfQuestions, allQuestionEntities.size());
		for (int randomIndex: randomIndexes) {
			Entity randomQuestionEntity = allQuestionEntities.get(randomIndex);
			Question question = questionFactory.getQuestion(randomQuestionEntity, lang);
			randomQuestions.add(question);
		}
		return randomQuestions;
	}

	@Override
	public List<Question> listTopPlusRandomQuestions(String lang, int numberOfQuestions, int priorityThreshold) {
		List<Question> topPlusRandomQuestions = new ArrayList<Question>();
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allQuestionEntities = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withDefaults());
		
		//first add questions of high enough priority
		List<Entity> nonTopQuestionEntities = new ArrayList<Entity>(allQuestionEntities);
		int numberOfTopQuestions = 0;
		for (Entity questionEntity: allQuestionEntities) {
			Double priorityDouble = (Double) questionEntity.getProperty("priority");
			int priority = priorityDouble.intValue();
			
			if (priority > priorityThreshold) {
				break;
			}
			topPlusRandomQuestions.add(questionFactory.getQuestion(questionEntity, lang));
			nonTopQuestionEntities.remove(questionEntity);
			numberOfTopQuestions++;
		}
		
		List<Integer> randomIndexes = getRandomIndexesList(numberOfQuestions - numberOfTopQuestions, nonTopQuestionEntities.size());
		log.info("randomIndexes=" + randomIndexes);
		for (int randomIndex: randomIndexes) {
			Entity randomQuestionEntity = nonTopQuestionEntities.get(randomIndex);
			Question question = questionFactory.getQuestion(randomQuestionEntity, lang);
			topPlusRandomQuestions.add(question);
		}
		return topPlusRandomQuestions;
	}
	

	/**
	 * return a list containing count nonrepeated integers, from 0 to size
	 * @param count
	 * @param size
	 * @return
	 */
	public List<Integer> getRandomIndexesList(int count, int size) {
		List<Integer> randomIndexes = new ArrayList<Integer>();
		Random randomGenerator = new Random();
		
		//get a list of <numberOfQuestions> indexes
		for (int i=0; i<count; i++) {
			int randomIndex = randomGenerator.nextInt(size);
			int counter = 0;
			while (randomIndexes.contains(randomIndex) && counter < 10000) {
				randomIndex = randomGenerator.nextInt(size);
				counter++;
			}
			randomIndexes.add(randomIndex);
		}
		return randomIndexes;
	}

	@Override
	public List<Question> listApplicableTopPlusRandomQuestions(String lang, String user, int numberOfQuestions, int priorityThreshold) {
		List<Entity> allQuestionEntities = listAllQuestionEntities();
		Key userKey = KeyFactory.createKey("Person", user);
		Query findQuestionHistoriesQuery = new Query("QuestionHistory");
		findQuestionHistoriesQuery.setAncestor(userKey);
		List<Entity> allQuestionHistoryEntities = datastoreService.prepare(findQuestionHistoriesQuery).asList(FetchOptions.Builder.withLimit(numberOfQuestions));
		
		//build qhMap=a map of entities
		Map<String, Entity> qhMap = new HashMap<String, Entity>();
		for (Entity qhEntity: allQuestionHistoryEntities) {
			qhMap.put((String)qhEntity.getProperty("question"), qhEntity);
		}
		
		List<Question> targetQuestions = new ArrayList<Question>();
		
		for (Entity questionEntity: allQuestionEntities) {
			String questionId = questionEntity.getKey().getName();
			if (shouldAskQuestion(user, questionEntity, qhMap.get(questionId)));
		}
	}

}
