package org.inqle.qa.gae;

import org.inqle.qa.Answer;
import org.inqle.qa.AnswerBroker;

import com.google.appengine.api.datastore.Entity;
import com.google.inject.Inject;

public class GaeAnswerBroker implements AnswerBroker {

	private GaeEntityFactory gaeEntityFactory;

	@Inject
	public GaeAnswerBroker(GaeEntityFactory gaeEntityFactory) {
		this.gaeEntityFactory = gaeEntityFactory;
	}
	
	@Override
	public void storeAnswer(Answer answer) {
		Entity answerEntity = gaeEntityFactory.getEntity(answer);

	}

}
