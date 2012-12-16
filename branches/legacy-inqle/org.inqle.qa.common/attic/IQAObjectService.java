package org.inqle.qa.services;

import java.util.List;

import org.inqle.ecf.common.IInqleEcfService;
import org.inqle.qa.beans.Option;
import org.inqle.qa.beans.Question;

@Deprecated
public interface IQAObjectService extends IInqleEcfService {
	//Methods for questions
	public String storeQuestion(Question questionToStore);
	public String storeQuestion(String databaseId, String modelName, Question questionToStore);
	public Question getQuestion(String questionId);
	public Question getQuestion(String questionId, String databaseId, String modelName);
	public List<Question> searchForQuestions(String queryTerm);
	public List<Question> searchForQuestions(String queryTerm, String databaseId, String modelName);
	
	//Methods for options
	public String storeOption(Option optionToStore);
	public String storeOption(String databaseId, String modelName, Option optionToStore);
	public Option getOption(String optionId);
	public Option getOption(String optionId, String databaseId, String modelName);
	public List<Option> searchForOptions(String queryTerm);
	public List<Option> searchForOptions(String queryTerm, String databaseId, String modelName);
}
