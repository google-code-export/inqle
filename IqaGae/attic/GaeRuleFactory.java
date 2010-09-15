package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Rule;
import org.inqle.qa.RuleFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;

@Deprecated
public class GaeRuleFactory implements RuleFactory {

	private DatastoreService datastoreService;
	private Logger log;
	
	@Inject
	public GaeRuleFactory(Logger log, DatastoreService datastoreService) {
		this.log = log;
		this.datastoreService = datastoreService;
	}
	
	@Override
	public Rule getRule(Object keyObj, String lang) {
		String kind = "Rule";
		Key key = (Key)keyObj;
		Entity entity = null;
		try {
			entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + key, e);
			return null;
		}
		
		Rule rule = new Rule();
		try {
			String msg = GaeBeanPopulator.populateBean(rule, entity, datastoreService, lang);
			log.info(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Questioner.class.  Returning null (no Questioner)", e);
			return null;
		}
		
		//TODO add some URI fields
		
		return rule;
	}

}
