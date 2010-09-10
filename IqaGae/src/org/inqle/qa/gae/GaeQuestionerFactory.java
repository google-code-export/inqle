package org.inqle.qa.gae;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Questioner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.inject.Inject;

public class GaeQuestionerFactory implements QuestionerFactory {

	private DatastoreService datastoreService;
	private Logger log;
	
	@Inject
	public GaeQuestionerFactory(Logger log, DatastoreService datastoreService) {
		this.datastoreService = datastoreService;
		this.log = log;
	}
	
	@Override
	public Questioner getQuestioner(String questionId, String lang) {
		String kind = "Question";
		Key questionKey = KeyFactory.createKey(kind, questionId);
		Entity qEntity = null;
		try {
			qEntity = datastoreService.get(questionKey);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + questionKey, e);
			return null;
		}
		
		//get localized text of requested language
		Query lsQuery = new Query("LocalizedString");
		lsQuery.setAncestor(questionKey);
		lsQuery.addFilter("lang", FilterOperator.EQUAL, lang);
		List<Entity> questionTexts = datastoreService.prepare(lsQuery).asList(FetchOptions.Builder.withDefaults());

		Questioner questioner = new Questioner();
		BeanInfo questionBeanInfo = null;
		try {
			questionBeanInfo = Introspector.getBeanInfo(Questioner.class);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Questioner.class", e);
			return null;
		}
		for (PropertyDescriptor pDescriptor: questionBeanInfo.getPropertyDescriptors()) {
			String propertyName = pDescriptor.getName();
			qEntity.getProperty(propertyName);
		}
		questioner.setQuestionText((String)qEntity.getProperty("questionText"));
		
		return questioner;
	}

}
