package org.inqle.qa.services;

import java.util.Collection;

import org.inqle.ecf.common.IInqleEcfService;
import org.inqle.qa.beans.Question;

public interface IQAObjectService extends IInqleEcfService {

	public String storeQuestion(String databaseId, String modelName, Question questionToStore);
	public Question getQuestion(String databaseId, String modelName, String objectId);
	public Collection<Question> listAllQuestions(String databaseId, String modelName);
	public String storeQuestion(Question questionToStore);
	public Question getQuestion(String questionId);
	public Collection<Question> listAllQuestions();
}
