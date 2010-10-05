package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.GenericLocalizedObjectFactory;
import org.inqle.qa.Unit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;

/**
 * Creates simple objects of desired localization from an Entity
 * @author David Donohue
 *
 */
public class GaeGenericLocalizedObjectFactory implements GenericLocalizedObjectFactory {

	private DatastoreService datastoreService;
	private Logger log;
	
	@Inject
	public GaeGenericLocalizedObjectFactory(Logger log, DatastoreService datastoreService) {
		this.log = log;
		this.datastoreService = datastoreService;
	}
	
	@Override
	public <T> T create(Class<T> objClass, Object keyObj, String lang) throws InstantiationException, IllegalAccessException {
		Key key = (Key)keyObj;
		String kind = key.getKind();
		Entity entity = null;
		try {
			entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + key, e);
			return null;
		}
		
		T theObj = objClass.newInstance();
		try {
			String msg = GaeBeanPopulator.populateBean(theObj, entity, datastoreService, lang);
			log.fine(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting class " + objClass + ".  Returning null (no Questioner)", e);
			return null;
		}
		
		return theObj;
	}

	@Override
	public <T> T create(Class<T> objClass, Object keyObj) throws InstantiationException, IllegalAccessException {
		Key key = (Key)keyObj;
		String kind = key.getKind();
		Entity entity = null;
		try {
			entity = datastoreService.get(key);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + key, e);
			return null;
		}
		
		T theObj = objClass.newInstance();
		try {
			String msg = GaeBeanPopulator.populateBean(theObj, entity, datastoreService);
			log.info(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting class " + objClass + ".  Returning null (no Questioner)", e);
			return null;
		}
		
		return theObj;
	}
	
}
