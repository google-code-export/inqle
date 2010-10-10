package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Answer;
import org.inqle.qa.AnswerBroker;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
		
		
		//if moratorium answer was given, do not store the answer
		if (answer.getMoratoriumUntil() == null) {
			Entity answerEntity = null;
			if (answer.getKey() == null && answer.getId() != null) {
				Key userKey = KeyFactory.createKey("Person", answer.getUser());
				Key answerKey = KeyFactory.createKey(userKey, "Answer", answer.getId());
				answer.setKey(KeyFactory.keyToString(answerKey));
			}
			log.info("Storing answer: " + answer);
			
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
			log.info("SASASASA Storing Answer Entity" + answerEntity);
			datastoreService.put(answerEntity);
		} else {
			log.info("NSANSANSA NOT Storing Answer as it is a moratorium answer" + answer);
		}
		
		//load the QuestionHistory
		Key qhKey = KeyFactory.createKey("QuestionHistory", answer.getUser() + "/" + answer.getQuestion());
		Entity qhEntity = null;
		try {
			qhEntity = datastoreService.get(qhKey);
		} catch (EntityNotFoundException e) {
			qhEntity = createQuestionHistory(answer);
		}
		updateWithLatestAnswer(qhEntity, answer);
		log.info("SSSSSSS Storing Question History Entity=" + qhEntity);
		datastoreService.put(qhEntity);
	}

	private static void updateWithLatestAnswer(Entity qhEntity, Answer answer) {
		qhEntity.setProperty("question", answer.getQuestion());
		qhEntity.setProperty("user", answer.getUser());
		qhEntity.setProperty("lastAnswer", answer.getId());
		qhEntity.setProperty("lastAnswerDate", answer.getDate());
		qhEntity.setProperty("lastAnswerVal", answer.getText());
		qhEntity.setProperty("moratoriumUntil", answer.getMoratoriumUntil());
	}

	private static Entity createQuestionHistory(Answer answer) {
		Key userKey = KeyFactory.createKey("Person", answer.getUser());
		String qhId = answer.getUser() + "/" + answer.getQuestion();
		Entity qhEntity = new Entity("QuestionHistory", qhId, userKey);
		qhEntity.setProperty("firstAnswer", answer.getId());
		qhEntity.setProperty("firstAnswerDate", answer.getDate());
		qhEntity.setProperty("firstAnswerVal", answer.getText());
		return qhEntity;
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
