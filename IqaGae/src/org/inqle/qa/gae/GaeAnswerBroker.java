package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Answer;
import org.inqle.qa.AnswerBroker;
import org.inqle.qa.Answer;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;

public class GaeAnswerBroker implements AnswerBroker {

	private GaeEntityFactory gaeEntityFactory;
	private Logger log;
	private DatastoreService datastoreService;

	@Inject
	public GaeAnswerBroker(Logger log, DatastoreService datastoreService, GaeEntityFactory gaeEntityFactory) {
		this.gaeEntityFactory = gaeEntityFactory;
		this.log = log;
		this.datastoreService = datastoreService;
	}
	
	@Override
	public void storeAnswer(Answer answer) {
		Entity answerEntity = null;
		try {
			answerEntity = gaeEntityFactory.getEntity(answer);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "Error creating entity from Answer object: " + answer, e);
			return;
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error creating entity from Answer object: " + answer, e);
			return;
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "Error creating entity from Answer object: " + answer, e);
			return;
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "Error creating entity from Answer object: " + answer, e);
			return;
		}
		datastoreService.put(answerEntity);
	}

	@Override
	public Answer getAnswer(Object answerKeyObj) {
		String kind = "Answer";
		Key answerKey = (Key)answerKeyObj;
		Entity answerEntity = null;
		try {
			answerEntity = datastoreService.get(answerKey);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + answerKey, e);
			return null;
		}
		return getAnswer(answerEntity);
	}
	
	public Answer getAnswer(Entity answerEntity) {
		Answer answer = new Answer();
		try {
			String msg = GaeBeanPopulator.populateBean(answer, answerEntity, datastoreService);
			log.fine(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Answer.class.  Returning null (no AnswerBroker)", e);
			return null;
		}
		return answer;
		

	}

}
