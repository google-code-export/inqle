package org.inqle.qa;


public interface RuleFactory {
	public Rule getRule(Object ruleKey, String lang);
}
