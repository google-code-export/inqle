package org.inqle.qa.server.util;

import org.apache.log4j.Logger;
import org.inqle.qa.server.beans.Answer;
import org.inqle.qa.server.beans.Option;
import org.inqle.qa.server.beans.Question;
import org.inqle.qa.server.beans.Translation;
import org.inqle.rdf.beans.util.BeanTool;

public class QAFactory {

	public static Logger log = Logger.getLogger(QAFactory.class);
	
	public Question newServerQuestion(org.inqle.qa.beans.Question clientQuestion) {
		Question question = BeanTool.replicateToLikeClass(Question.class, clientQuestion);
		return question;
	}
	
	public Answer newServerAnswer(org.inqle.qa.beans.Answer clientAnswer) {
		Answer answer = BeanTool.replicateToLikeClass(Answer.class, clientAnswer);
		return answer;
	}
	
	public Option newServerOption(org.inqle.qa.beans.Option clientOption) {
		Option option = BeanTool.replicateToLikeClass(Option.class, clientOption);
		return option;
	}
	
	public Translation newServerTranslation(org.inqle.qa.beans.Translation clientTranslation) {
		Translation translation = BeanTool.replicateToLikeClass(Translation.class, clientTranslation);
		return translation;
	}
}
