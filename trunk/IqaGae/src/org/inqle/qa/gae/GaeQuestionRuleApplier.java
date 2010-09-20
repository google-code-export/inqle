package org.inqle.qa.gae;

import java.util.List;

import org.inqle.qa.AskableQuestion;
import org.inqle.qa.AskableQuestionFactory;
import org.inqle.qa.RuleApplier;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.inject.Inject;

import test.org.inqle.qa.QuestionRuleApplier;

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
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allQuestionEntities = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withLimit(500));
		for (Entity questionEntity: allQuestionEntities) {
			if (shouldAskQuestion(questionId)) {
				
			}
		}
		
		
		Query rulesQuery = new Query("Mapping");
		rulesQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> optionMappingEntities = datastoreService.prepare(rulesQuery).asList(FetchOptions.Builder.withDefaults());
	}

}
