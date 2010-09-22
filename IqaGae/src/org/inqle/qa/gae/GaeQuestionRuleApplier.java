package org.inqle.qa.gae;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.inqle.qa.AskableQuestion;
import org.inqle.qa.AskableQuestionFactory;
import org.inqle.qa.QuestionRuleApplier;
import org.inqle.qa.RuleApplier;

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
	private AskableQuestionFactory askableQuestionFactory;
	private RuleApplier ruleApplier;

	@Inject
	public GaeQuestionRuleApplier(DatastoreService datastoreService, AskableQuestionFactory askableQuestionFactory, RuleApplier ruleApplier) {
		this.datastoreService = datastoreService;
		this.askableQuestionFactory = askableQuestionFactory;
		this.ruleApplier = ruleApplier;
	}
	
	@Override
	public List<AskableQuestion> getApplicableQuestions(String userId, String lang) {
		List<AskableQuestion> askableQuestions = new ArrayList<AskableQuestion>();
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allQuestionEntities = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withLimit(500));
		for (Entity questionEntity: allQuestionEntities) {
			if (shouldAskQuestion(userId, questionEntity)) {
				AskableQuestion askableQuestion = askableQuestionFactory.getAskableQuestion(questionEntity, lang);
				askableQuestions.add(askableQuestion);
			}
		}
		return askableQuestions;
	}

	/**
	 * If the question has not been asked too recently and if its rules apply, then return true.  
	 * Otherwise return false
	 * @param userId
	 * @param questionEntity
	 * @return
	 */
	private boolean shouldAskQuestion(String userId, Entity questionEntity) {
		Query answersQuery = new Query("Answer");
		Key userKey = KeyFactory.createKey("Person", userId);
		answersQuery.setAncestor(userKey);
		answersQuery.addFilter("question", FilterOperator.EQUAL, questionEntity.getProperty("id"));
//		answersQuery.addSort("answerDate", SortDirection.DESCENDING);
		Date latestAcceptableDate = new Date();
		Calendar c = Calendar.getInstance();
		Double minInterval = (Double)questionEntity.getProperty("minInterval");
		int minIntervalInt = minInterval.intValue();
		c.add(Calendar.DATE, minIntervalInt * -1);
		latestAcceptableDate = c.getTime();
		answersQuery.addFilter("answerDate", FilterOperator.GREATER_THAN, latestAcceptableDate);
		int countRecentAnswers = datastoreService.prepare(answersQuery).countEntities();
		
		if (countRecentAnswers > 0) {
			//latest response was too recent
			return false;
		}
		
		//TODO test rules!
		
		return true;
	}

}
