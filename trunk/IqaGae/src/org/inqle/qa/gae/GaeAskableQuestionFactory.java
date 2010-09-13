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
			GaeBeanPopulator.populateBean(askableQuestion, qEntity, datastoreService, lang);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Questioner.class.  Returning null (no Questioner)", e);
			return null;
		}
		
		//TODO add some URI fields
		
		return askableQuestion;
		
//		//get localized text of requested language
//		Query lsQuery = new Query("LocalizedString");
//		lsQuery.setAncestor(questionKey);
//		lsQuery.addFilter("lang", FilterOperator.EQUAL, lang);
//		List<Entity> questionTexts = datastoreService.prepare(lsQuery).asList(FetchOptions.Builder.withDefaults());
//		//add the localized text of desired language to a map for later use
//		Map<String, String> stringsOfDesiredLocalization = new HashMap<String, String>();
//		for (Entity questionText: questionTexts) {
//			String parentProperty = (String)questionText.getProperty("parentProperty");
//			String text = (String)questionText.getProperty("text");
//			stringsOfDesiredLocalization.put(parentProperty, text);
//		}
		
//		BeanInfo questionBeanInfo = null;
//		try {
//			questionBeanInfo = Introspector.getBeanInfo(AskableQuestion.class);
//		} catch (IntrospectionException e) {
//			log.log(Level.SEVERE, "Error introspecting Questioner.class.  Returning null (no Questioner)", e);
//			return null;
//		}
//		
//		for (PropertyDescriptor pDescriptor: questionBeanInfo.getPropertyDescriptors()) {
//			String propertyName = pDescriptor.getName();
//			Object value = qEntity.getProperty(propertyName);
//			if (value==null) {
//				value = stringsOfDesiredLocalization.get(propertyName);
//			}
//			Method setter = pDescriptor.getWriteMethod();
//			if (setter == null) {
//				log.info("No setter for property: " + propertyName);
//				continue;
//			}
//			try {
//				setter.invoke(askableQuestion, value);
//			} catch (IllegalArgumentException e) {
//				log.log(Level.SEVERE, "Error setting property: " + propertyName + " on new Questioner object.  Skipping this property.", e);
//			} catch (IllegalAccessException e) {
//				log.log(Level.SEVERE, "Error setting property: " + propertyName + " on new Questioner object.  Skipping this property.", e);
//			} catch (InvocationTargetException e) {
//				log.log(Level.SEVERE, "Error setting property: " + propertyName + " on new Questioner object.  Skipping this property.", e);
//			}
//		}
	}

}
