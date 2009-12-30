package org.inqle.qa.services;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.qa.beans.Question;

import com.hp.hpl.jena.rdf.model.Model;

public class QAObjectService implements IQAObjectService {

	private String serverId;

	private static Logger log = Logger.getLogger(QAObjectService.class);
	
	public <T> T getObject(Class<T> objectClass, String databaseId, String modelName, String objectId) {
		String datamodelId = Persister.getDatamodelId(databaseId, IDBConnector.SUBDATABASE_DATA, modelName);
		Persister persister = Persister.getInstance();
		return persister.reconstitute(objectClass, objectId, datamodelId, true);
	}

	public <T> Collection<T> listObjectsOfClass(Class<T> objectClass, String databaseId, String modelName) {
		String datamodelId = Persister.getDatamodelId(databaseId, IDBConnector.SUBDATABASE_DATA, modelName);
		Persister persister = Persister.getInstance();
		Model model = persister.getModel(datamodelId);
		return persister.reconstituteAll(objectClass, model);
	}

	/**
	 * persist Jenabean object to specified database, using user database
	 * Return error message, or null if no error
	 */
	public <T> String storeObject(String databaseId, String modelName, T objectToStore) {
		String datamodelId = Persister.getDatamodelId(databaseId, IDBConnector.SUBDATABASE_DATA, modelName);
		Persister persister = Persister.getInstance();
		persister.persist(objectToStore, datamodelId);
		return null;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String containerId) {
		this.serverId = containerId;
	}

	public Question getQuestion(String databaseId, String modelName, String objectId) {
		Question question = new Question();
		question.setQuestionType(Question.QUESTION_TYPE_SINGLE_SELECTION);
		return question;
		//		return getObject(Question.class, databaseId, modelName, objectId);
	}

	public String storeQuestion(String databaseId, String modelName, Question question) {
		return storeObject(databaseId, modelName, question);
	}

	public Collection<Question> listAllQuestions(String databaseId, String modelName) {
		return listObjectsOfClass(Question.class, databaseId, modelName);
	}

}
