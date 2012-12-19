package org.inqle.qs.bean;
import org.openrdf.annotations.Iri;
import org.openrdf.model.URI;

import static org.inqle.qs.Constants.NS_RULE;

@Iri(NS_RULE)
public class Rule {

	private URI deliversQuestion;
	private URI firesOnAnswerToQuestion;
	private URI assertsSubject;
	private URI assertsPredicate;
	private SemanticCondition semanticCondition;
}
