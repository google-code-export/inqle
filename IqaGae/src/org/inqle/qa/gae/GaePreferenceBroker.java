package org.inqle.qa.gae;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inqle.qa.Preference;
import org.inqle.qa.PreferenceBroker;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.inject.Inject;

public class GaePreferenceBroker implements PreferenceBroker {

	private GaeEntityFactory gaeEntityFactory;
	private Logger log;
	private DatastoreService datastoreService;

	@Inject
	public GaePreferenceBroker(Logger log, DatastoreService datastoreService, GaeEntityFactory gaeEntityFactory) {
		this.gaeEntityFactory = gaeEntityFactory;
		this.log = log;
		this.datastoreService = datastoreService;
	}
	
	@Override
	public void storePreference(Preference preference) {
		Entity preferenceEntity = null;
		if (preference.getKey() == null && preference.getId() != null) {
			Key userKey = KeyFactory.createKey("Person", preference.getUser());
			Key preferenceKey = KeyFactory.createKey(userKey, "Preference", preference.getId());
			preference.setKey(KeyFactory.keyToString(preferenceKey));
		}
		log.info("Storing preference: " + preference);
		
		try {
			preferenceEntity = gaeEntityFactory.getEntity(preference);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "Error creating entity from Preference object: " + preference, e);
			return;
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error creating entity from Preference object: " + preference, e);
			return;
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "Error creating entity from Preference object: " + preference, e);
			return;
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "Error creating entity from Preference object: " + preference, e);
			return;
		}
		
		datastoreService.put(preferenceEntity);
	}

	@Override
	public Preference getPreference(Object preferenceKeyObj) {
		String kind = "Preference";
		Key preferenceKey = (Key)preferenceKeyObj;
		Entity preferenceEntity = null;
		try {
			preferenceEntity = datastoreService.get(preferenceKey);
		} catch (EntityNotFoundException e) {
			log.log(Level.SEVERE, "Error retrieving Entity of kind=" + kind + " and key=" + preferenceKey, e);
			return null;
		}
		return getPreference(preferenceEntity);
	}
	
	public Preference getPreference(Entity preferenceEntity) {
		Preference preference = new Preference();
		try {
			String msg = GaeBeanPopulator.populateBean(preference, preferenceEntity, datastoreService);
			log.fine(msg);
		} catch (IntrospectionException e) {
			log.log(Level.SEVERE, "Error introspecting Preference.class.  Returning null (no PreferenceBroker)", e);
			return null;
		}
		return preference;
		

	}

}
