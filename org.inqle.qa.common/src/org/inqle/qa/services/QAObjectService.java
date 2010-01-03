package org.inqle.qa.services;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.StackUtil;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.qa.beans.Question;

import com.hp.hpl.jena.rdf.model.Model;

public class QAObjectService implements IQAObjectService {

	private String serverId;

	private static Logger log = Logger.getLogger(QAObjectService.class);
	
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String containerId) {
		this.serverId = containerId;
	}

	/**
	 * Get a question from the default datamodel
	 * @param questionId
	 * @return
	 */
	public Question getQuestion(String questionId) {
		return new RdfObjectService().getObject(Question.class, questionId);
	}
	
	/**
	 * Get a question from the specified datamodel
	 */
	public Question getQuestion(String databaseId, String modelName, String objectId) {
		return new RdfObjectService().getObject(Question.class, databaseId, modelName, objectId);
	}

	/**
	 * Store a question to the default datamodel
	 * @param question
	 * @return
	 */
	public String storeQuestion(Question question) {
		log.info("Storing question: " + question);
		String msg = new RdfObjectService().storeObject(question);
		log.info("Stored it, msg=" + msg);
		return msg;
	}
	
	/**
	 * Store a question to the specified datamodel
	 */
	public String storeQuestion(String databaseId, String modelName, Question question) {
		return new RdfObjectService().storeObject(databaseId, modelName, question);
	}

	/**
	 * List all questions from the default datamodel
	 */
	public Collection<Question> listAllQuestions() {
		return new RdfObjectService().listObjectsOfClass(Question.class);
	}
	
	/**
	 * List all questions from the specified datamodel
	 */
	public Collection<Question> listAllQuestions(String databaseId, String modelName) {
		return new RdfObjectService().listObjectsOfClass(Question.class, databaseId, modelName);
	}

}
