package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Question;
import org.inqle.qa.QuestionBroker;
import org.inqle.qa.GenericLocalizedObjectFactory;
import org.inqle.qa.Option;
import org.inqle.qa.Unit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.inject.Inject;

public class GaeQuestionBroker implements QuestionBroker {

	private DatastoreService datastoreService;
	private Logger log;
	private GenericLocalizedObjectFactory genericLocalizedObjectFactory;
	
	@Inject
	public GaeQuestionBroker(
			Logger log, 
			DatastoreService datastoreService, 
			GenericLocalizedObjectFactory genericLocalizedObjectFactory) {
		this.datastoreService = datastoreService;
		this.log = log;
		this.genericLocalizedObjectFactory = genericLocalizedObjectFactory;
	}
	
	@Override
	public List<Question> listAllQuestions(String lang) {
		List<Question> allAskableQuestions = new ArrayList<Question>();
		Query findQuestionsQuery = new Query("Question");
		findQuestionsQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allQuestionEntities = datastoreService.prepare(findQuestionsQuery).asList(FetchOptions.Builder.withDefaults());
		for (Entity questionEntity: allQuestionEntities) {
			Question question = getQuestion(questionEntity, lang);
			allAskableQuestions.add(question);
		}
		return allAskableQuestions;
	}
	
	@Override
	public Question getQuestion(Object questionKeyObj, String lang) {
		if (questionKeyObj instanceof Entity) {
			return getQuestion((Entity)questionKeyObj, lang);
		}
		String kind = "Question";
		Key questionKey = (Key)questionKeyObj;
		Entity questionEntity = null;
		try {
			questionEntity = datastoreService.get(questionKey);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + questionKey, e);
			return null;
		}
		return getQuestion(questionEntity, lang);
	}
	
	public Question getQuestion(Entity questionEntity, String lang) {
		Question question = new Question();
		try {
			String msg = GaeBeanPopulator.populateBean(question, questionEntity, datastoreService, lang);
			log.fine(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Question.class.  Returning null (no QuestionBroker)", e);
			return null;
		}
		
		//add unit & option fields
		try {
			question.setOptions(getOptions(questionEntity, lang));
		} catch (InstantiationException e) {
			log.log(Level.SEVERE, "InstantiationException creating child object of Question " + question.getId());
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "IllegalAccessException creating child object of Question " + question.getId());
		}
		
		return question;
		

	}

	@Deprecated
	private List<Option> getOptions(Key questionKey, String lang) throws InstantiationException, IllegalAccessException {
		List<Option> answerOptions = new ArrayList<Option>();
		
		//first get the Measure entity
		Query optionsQuery = new Query("Mapping");
		optionsQuery.setAncestor(questionKey);
		optionsQuery.addFilter("parentProperty", FilterOperator.EQUAL, "options");
		optionsQuery.addSort("iqa_orderBy", SortDirection.ASCENDING);
		List<Entity> optionMappingEntities = datastoreService.prepare(optionsQuery).asList(FetchOptions.Builder.withDefaults());
		for (Entity optionMappingEntity: optionMappingEntities) {
//			String optionMappingId = optionMappingEntity.getKey().getName();
			String optionId = (String)optionMappingEntity.getProperty("id");
			String optionKind = (String)optionMappingEntity.getProperty("kind");
			Key optionKey = KeyFactory.createKey(optionKind, optionId);
//			String optionEntityKeyStr = (String)optionMappingEntity.getProperty("entityKey");
//			Key optionKey = KeyFactory.stringToKey(optionEntityKeyStr);
			log.fine("Found option entity: " + optionMappingEntity);
			Option option = genericLocalizedObjectFactory.create(Option.class, optionKey, lang);
			answerOptions.add(option);
		}
		return answerOptions;
	}
	
	@SuppressWarnings("unchecked")
	private List<Option> getOptions(Entity questionEntity, String lang) throws InstantiationException, IllegalAccessException {
		List<Option> answerOptions = new ArrayList<Option>();
		
		if (questionEntity.getProperty("options")==null) return null;
		Object optsObj = questionEntity.getProperty("options");
		List<String> optIds = new ArrayList<String>();
		if (optsObj instanceof String) {
			optIds.add((String)optsObj);
		} else if (optsObj instanceof List<?>) {
			optIds = (List<String>)optsObj;
		} else {
			log.warning("Question property 'options' should have a list of 1 or more strings, but instead has value: " + optsObj);
			return null;
		}
		//first get the Measure entity
		String optionKind = "Option";
		for (String optionId: optIds) {
			
			Key optionKey = KeyFactory.createKey(optionKind, optionId);
			Option option = genericLocalizedObjectFactory.create(Option.class, optionKey, lang);
			answerOptions.add(option);
		}
		return answerOptions;
	}


}
