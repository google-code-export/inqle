package org.inqle.qa.gae;

import org.inqle.qa.Answer;

import com.google.appengine.api.datastore.Entity;

/**
 * Creates a GAE Entity object, given  java bean which conforms to IQA standard
 * @author gd9345
 *
 */
public class GaeEntityFactory {

	public Entity getEntity(Object bean) {
		String shortClassName = bean.getClass().getSimpleName();
		String id = bean.get
	}


}
