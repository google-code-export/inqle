package org.inqle.qa.gae;

import java.util.logging.Logger;

import org.inqle.qa.Rule;
import org.inqle.qa.RuleApplier;

import com.google.inject.Inject;

public class GaeRuleApplier implements RuleApplier {

	private Logger log;
	
	@Inject
	public GaeRuleApplier(Logger log) {
		this.log = log;
	}
	@Override
	public boolean applyRule(Rule rule, String userId) {
		// TODO Auto-generated method stub
		return false;
	}
	public Logger getLog() {
		return log;
	}

}
