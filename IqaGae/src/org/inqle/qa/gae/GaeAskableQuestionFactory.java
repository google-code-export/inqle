package org.inqle.qa.gae;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.AskableQuestion;
import org.inqle.qa.AskableQuestionFactory;
import org.inqle.qa.RuleFactory;
import org.inqle.qa.Unit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.inject.Inject;

public class GaeAskableQuestionFactory implements AskableQuestionFactory {

	private DatastoreService datastoreService;
	private Logger log;
	private RuleFactory ruleFactory;
	
	@Inject
	public GaeAskableQuestionFactory(
			Logger log, 
			DatastoreService datastoreService, 
			RuleFactory ruleFactory) {
		this.datastoreService = datastoreService;
		this.log = log;
		this.ruleFactory = ruleFactory;
	}
	
	@Override
	public AskableQuestion getAskableQuestion(Object questionKeyObj, String lang) {
		String kind = "Question";
		Key questionKey = (Key)questionKeyObj;
		Entity qEntity = null;
		try {
			qEntity = datastoreService.get(questionKey);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + questionKey, e);
			return null;
		}
		
		AskableQuestion askableQuestion = new AskableQuestion();
		try {
			String msg = GaeBeanPopulator.populateBean(askableQuestion, qEntity, datastoreService, lang);
			log.info(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting AskableQuestion.class.  Returning null (no Questioner)", e);
			return null;
		}
		
		//TODO add unit & option fields
		askableQuestion.setAnswerUnits(getAnswerUnits(questionKey, lang));
		askableQuestion.setAnswerOptions(getAnswerOptions(questionKey, lang));
		
		return askableQuestion;
		

	}

	private Map<String, Unit> getAnswerUnits(Key questionKey, String lang) {
		Map<String, Unit> answerUnits = new HashMap<String, Unit>();
		
		//first get the Measure entity
		Query measuresQuery = new Query("Mapping");
		measuresQuery.setAncestor(questionKey);
		measuresQuery.addFilter("parentProperty", FilterOperator.EQUAL, "measure");
		List<Entity> measures = datastoreService.prepare(measuresQuery).asList(FetchOptions.Builder.withDefaults());
		Entity measure = measures.get(1);
		
		//next get the Unit objects
		Query unitQuery = new Query("Mapping");
		unitQuery.setAncestor(measure.getKey());
		unitQuery.addFilter("parentProperty", FilterOperator.EQUAL, "unit");
		List<Entity> units = datastoreService.prepare(measuresQuery).asList(FetchOptions.Builder.withDefaults());
		Entity measure = measures.get(1);
		
		//add the localized text of desired language to a map for later use
		Map<String, String> stringsOfDesiredLocalization = new HashMap<String, String>();
		for (Entity localizedText: localizedTexts) {
			String parentProperty = (String)localizedText.getProperty("parentProperty");
			String text = (String)localizedText.getProperty("text");
			stringsOfDesiredLocalization.put(parentProperty, text);
		}
	}

}
