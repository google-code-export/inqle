package org.inqle.qa;

import com.google.appengine.api.datastore.Key;

public interface GenericLocalizedObjectFactory {

	/**
	 * Create an object with String fields containing the string of proper language
	 * @param <T>
	 * @param objClass
	 * @param keyObj
	 * @param lang
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public <T> T create(Class<T> objClass, Object keyObj, String lang) throws InstantiationException, IllegalAccessException;

	/**
	 * Create an object without localization
	 * @param <T>
	 * @param objClass
	 * @param keyObj
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public <T> T create(Class<T> objClass, Object keyObj)throws InstantiationException, IllegalAccessException;
}
