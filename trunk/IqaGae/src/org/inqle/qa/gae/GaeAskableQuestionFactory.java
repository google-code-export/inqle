package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.AskableQuestion;
import org.inqle.qa.AskableQuestionFactory;
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

public class GaeAskableQuestionFactory implements AskableQuestionFactory {

	private DatastoreService datastoreService;
	private Logger log;
	private GenericLocalizedObjectFactory genericLocalizedObjectFactory;
	
	@Inject
	public GaeAskableQuestionFactory(
			Logger log, 
			DatastoreService datastoreService, 
			GenericLocalizedObjectFactory genericLocalizedObjectFactory) {
		this.datastoreService = datastoreService;
		this.log = log;
		this.genericLocalizedObjectFactory = genericLocalizedObjectFactory;
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
		try {
			askableQuestion.setOptions(getOptions(questionKey, lang));
		} catch (InstantiationException e) {
			log.log(Level.SEVERE, "InstantiationException creating child object of AskableQuestion " + askableQuestion.getId());
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "IllegalAccessException creating child object of AskableQuestion " + askableQuestion.getId());
		}
		
		return askableQuestion;
		

	}

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
			log.info("Found option entity: " + optionMappingEntity);
			Option option = genericLocalizedObjectFactory.create(Option.class, optionKey, lang);
			answerOptions.add(option);
		}
		return answerOptions;
	}

}
