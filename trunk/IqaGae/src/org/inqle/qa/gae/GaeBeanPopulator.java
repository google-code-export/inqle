package org.inqle.qa.gae;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.inqle.qa.Question;
import org.mortbay.log.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * This class loads a bean with pertinent info, given a corresponding GAE datastore Entity and DatastoreService.
 * @author David Donohue
 * 2010-09-13
 *
 */
public class GaeBeanPopulator {

	/**
	 * To a bean, add all corresponding properties from the entity.
	 * For each child LocalizedString object in the datastore, populate the corresponding string field in the entity using 
	 * the string of proper localization
	 * @param <T>
	 * @param bean
	 * @param entity
	 * @param datastoreService
	 * @param lang
	 * @return a log message
	 * @throws IntrospectionException 
	 */
	public static <T> String populateBean(T bean, Entity entity, DatastoreService datastoreService, String lang) throws IntrospectionException {
		String msg = "";
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		
		//get localized text of requested language
		Query lsQuery = new Query("LocalizedString");
		lsQuery.setAncestor(entity.getKey());
		lsQuery.addFilter("lang", FilterOperator.EQUAL, lang);
		List<Entity> localizedTexts = datastoreService.prepare(lsQuery).asList(FetchOptions.Builder.withDefaults());
		//add the localized text of desired language to a map for later use
		Map<String, String> stringsOfDesiredLocalization = new HashMap<String, String>();
		for (Entity localizedText: localizedTexts) {
			String parentProperty = (String)localizedText.getProperty("parentProperty");
			String text = (String)localizedText.getProperty("text");
			stringsOfDesiredLocalization.put(parentProperty, text);
		}
		
		for (PropertyDescriptor pDescriptor: beanInfo.getPropertyDescriptors()) {
			String propertyName = pDescriptor.getName();
			Object value = entity.getProperty(propertyName);

			if ("id".equals(propertyName)) {
				value = entity.getKey().getName();
			}
			if ("entityKey".equals(propertyName)) {
				value = entity.getKey().toString();
			}
			if (value==null) {
				value = stringsOfDesiredLocalization.get(propertyName);
			}
			if (value==null) {
				msg += "\nNo value found in entity '" + entity.getKey().toString() + "', for bean property: " + propertyName;
				continue;
			}
			String strVal = String.valueOf(value);
			Method setter = pDescriptor.getWriteMethod();
			Class<?> pClass = pDescriptor.getPropertyType();
			if (Integer.class.equals(pClass)) {
				value = Integer.parseInt(strVal);
			} else if (Double.class.equals(pClass)) {
				value = Double.parseDouble(strVal);
				Log.info("PPPPPPPPP Property " + propertyName + " is a Double.  Parsed value=" + value);
			}
			if (setter == null) {
				msg += "\nNo setter for bean property: " + propertyName;
				continue;
			}
			try {
				setter.invoke(bean, value);
			} catch (IllegalArgumentException e) {
				msg += "\nIllegalArgumentException setting property: " + propertyName + " on new Question object.  Skipping this property.";
			} catch (IllegalAccessException e) {
				msg += "\nIllegalAccessException setting property: " + propertyName + " on new Question object.  Skipping this property.";
			} catch (InvocationTargetException e) {
				msg += "\nInvocationTargetException setting property: " + propertyName + " on new Question object.  Skipping this property.";
			}
		}
		if (msg.length() == 0) msg = null;
		return msg;
		
	}
	
	/**
	 * To a bean, add all corresponding properties from the entity.
	 * Do not add localized strings
	 * @param <T>
	 * @param bean
	 * @param entity
	 * @param datastoreService
	 * @param lang
	 * @return a log message
	 * @throws IntrospectionException 
	 */
	public static <T> String populateBean(T bean, Entity entity, DatastoreService datastoreService) throws IntrospectionException {
		String msg = "";
		BeanInfo beanInfo = null;
		beanInfo = Introspector.getBeanInfo(bean.getClass());
		
		for (PropertyDescriptor pDescriptor: beanInfo.getPropertyDescriptors()) {
			String propertyName = pDescriptor.getName();
			Object value = entity.getProperty(propertyName);

			if ("id".equals(propertyName)) {
				value = entity.getKey().getName();
			}
			if ("entityKey".equals(propertyName)) {
				value = entity.getKey().toString();
			}
			if (value==null) {
				msg += "\nNo value found in entity '" + entity.getKey().toString() + "', for bean property: " + propertyName;
				continue;
			}
			String strVal = String.valueOf(value);
			Method setter = pDescriptor.getWriteMethod();
			Class<?> pClass = pDescriptor.getPropertyType();
			if (Integer.class.equals(pClass)) {
				value = Integer.parseInt(strVal);
			} else if (Double.class.equals(pClass)) {
				value = Double.parseDouble(strVal);
			}
			if (setter == null) {
				msg += "\nNo setter for bean property: " + propertyName;
				continue;
			}
			try {
				setter.invoke(bean, value);
			} catch (IllegalArgumentException e) {
				msg += "\nIllegalArgumentException setting property: " + propertyName + " on new Question object.  Skipping this property.";
			} catch (IllegalAccessException e) {
				msg += "\nIllegalAccessException setting property: " + propertyName + " on new Question object.  Skipping this property.";
			} catch (InvocationTargetException e) {
				msg += "\nInvocationTargetException setting property: " + propertyName + " on new Question object.  Skipping this property.";
			}
		}
		if (msg.length() == 0) msg = null;
		return msg;
	}

}
