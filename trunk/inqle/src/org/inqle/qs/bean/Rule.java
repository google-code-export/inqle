package org.inqle.qs.bean;
import java.util.Collection;

import org.openrdf.annotations.Iri;
import org.openrdf.model.URI;

import static org.inqle.qs.Constants.NS_RULE;

@Iri(NS_RULE)
public class Rule {

	private URI deliversQuestion;
	private URI firesOnAnswerToQuestion;
	private URI assertsPredicate;
	private Collection<Condition> conditions;
	private String formula;
	private String sparql;
}
