package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.GenericLocalizedObjectFactory;
import org.inqle.qa.Option;
import org.inqle.qa.Rule;
import org.inqle.qa.RuleFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.inject.Inject;

public class GaeRuleFactory implements RuleFactory {

	private DatastoreService datastoreService;
	private Logger log;
	private GenericLocalizedObjectFactory genericLocalizedObjectFactory;
	
	@Inject
	public GaeRuleFactory(
			Logger log, 
			DatastoreService datastoreService, 
			GenericLocalizedObjectFactory genericLocalizedObjectFactory) {
		this.datastoreService = datastoreService;
		this.log = log;
		this.genericLocalizedObjectFactory = genericLocalizedObjectFactory;
	}
	
	@Override
	public List<Rule> listAllRules() {
		List<Rule> allRules = new ArrayList<Rule>();
		Query findRulesQuery = new Query("Rule");
		findRulesQuery.addSort("priority", SortDirection.ASCENDING);
		List<Entity> allRuleEntities = datastoreService.prepare(findRulesQuery).asList(FetchOptions.Builder.withLimit(500));
		for (Entity ruleEntity: allRuleEntities) {
			Rule rule = getRule(ruleEntity);
			allRules.add(rule);
		}
		return allRules;
	}
	
	public Rule getRule(String ruleId) {
		Key ruleKey = KeyFactory.createKey("Rule", ruleId);
		return getRule(ruleKey);
	}
	
	@Override
	public Rule getRule(Object ruleKeyObj) {
		String kind = "Rule";
		Key ruleKey = (Key)ruleKeyObj;
		Entity ruleEntity = null;
		try {
			ruleEntity = datastoreService.get(ruleKey);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + ruleKey, e);
			return null;
		}
		return getRule(ruleEntity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Rule getRule(Entity ruleEntity) {
		Rule rule = new Rule();
		try {
			String msg = GaeBeanPopulator.populateBean(rule, ruleEntity, datastoreService);
			log.info(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Rule.class.  Returning null (no Rule)", e);
			return null;
		}
		
		List<String> subRules = (List<String>)ruleEntity.getProperty("andRules");
		List<Rule> andRules = new ArrayList<Rule>();
		for (String ruleId: subRules) {
			andRules.add(getRule(ruleId));
		}
		rule.setAndRules(andRules);
		
		subRules = (List<String>)ruleEntity.getProperty("orRules");
		List<Rule> orRules = new ArrayList<Rule>();
		for (String ruleId: subRules) {
			orRules.add(getRule(ruleId));
		}
		rule.setOrRules(orRules);
		return rule;
	}

	@Deprecated
	private List<Option> getOptions(Key ruleKey, String lang) throws InstantiationException, IllegalAccessException {
		List<Option> answerOptions = new ArrayList<Option>();
		
		//first get the Measure entity
		Query optionsQuery = new Query("Mapping");
		optionsQuery.setAncestor(ruleKey);
		optionsQuery.addFilter("parentProperty", FilterOperator.EQUAL, "options");
		optionsQuery.addSort("iqa_orderBy", SortDirection.ASCENDING);
		List<Entity> optionMappingEntities = datastoreService.prepare(optionsQuery).asList(FetchOptions.Builder.withDefaults());
		for (Entity optionMappingEntity: optionMappingEntities) {
//			String optionMappingId = optionMappingEntity.getKey().getName();
			String optionId = (String)optionMappingEntity.getProperty("id");
			String optionKind = (String)optionMappingEntity.getProperty("kind");
			Key optionKey = KeyFactory.createKey(optionKind, optionId);
//			String optionEntityKeyStr = (String)optionMappingEntity.getProperty("entityKey");
//			Key optionKey = KeyFactory.stringToKey(optionEntityKeyStr);
			log.info("Found option entity: " + optionMappingEntity);
			Option option = genericLocalizedObjectFactory.create(Option.class, optionKey, lang);
			answerOptions.add(option);
		}
		return answerOptions;
	}
	
	@SuppressWarnings("unchecked")
	private List<Option> getOptions(Entity ruleEntity, String lang) throws InstantiationException, IllegalAccessException {
		List<Option> answerOptions = new ArrayList<Option>();
		
		if (ruleEntity.getProperty("options")==null) return null;
		Object optsObj = ruleEntity.getProperty("options");
		List<String> optIds = new ArrayList<String>();
		if (optsObj instanceof String) {
			optIds.add((String)optsObj);
		} else if (optsObj instanceof List<?>) {
			optIds = (List<String>)optsObj;
			log.warning("Rule property 'options' should have a list of 1 or more strings, but instead has value: " + optsObj);
		} else {
			return null;
		}
		//first get the Measure entity
		String optionKind = "Option";
		for (String optionId: optIds) {
			
			Key optionKey = KeyFactory.createKey(optionKind, optionId);
			Option option = genericLocalizedObjectFactory.create(Option.class, optionKey, lang);
			answerOptions.add(option);
		}
		return answerOptions;
	}

}
