package org.inqle.qa;

import java.util.List;

import com.google.appengine.api.datastore.Entity;


public interface RuleFactory {

	public Rule getRule(Object ruleKey);

	public Rule getRule(Entity ruleEntity);
	
	public List<Rule> listAllRules();

	
}
