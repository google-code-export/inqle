package org.inqle.qa.server.services;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.qa.beans.Question;
import org.inqle.qa.services.IQAObjectService;
import org.inqle.rdf.beans.util.BeanTool;

@Deprecated
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
	public org.inqle.qa.beans.Question getQuestion(String questionId) {
		return new RdfObjectService().getObject(Question.class, questionId);
	}
	
	/**
	 * Get a question from the specified datamodel
	 */
	public org.inqle.qa.beans.Question getQuestion(String questionId, String databaseId, String modelName) {
		return new RdfObjectService().getObject(Question.class, databaseId, modelName, questionId);
	}

	/**
	 * convert a client question to a server question, and store it in the default datamodel
	 * @param question
	 * @return
	 */
	public String storeQuestion(Question question) {
		log.info("Storing question: " + question);
//		Question questionToStore = BeanTool.replicateToLikeClass(Question.class, incommingQuestion);
		String msg = new RdfObjectService().storeObject(question);
		log.info("Stored it, msg=" + msg);
		return msg;
	}
	
	/**
	 * convert a client question to a server question, and store it in the specified datamodel
	 */
	public String storeQuestion(String databaseId, String modelName, Question question) {
		log.info("Storing question: " + question);
//		Question questionToStore = BeanTool.replicateToLikeClass(Question.class, question);
		String msg = new RdfObjectService().storeObject(databaseId, modelName, question);
		log.info("Stored it, msg=" + msg);
		return msg;
	}

}
