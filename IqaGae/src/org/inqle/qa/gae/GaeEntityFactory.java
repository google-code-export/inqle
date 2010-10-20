package org.inqle.qa.gae;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.inqle.qa.IQABean;
import org.mortbay.log.Log;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Creates a GAE Entity object, given  java bean which conforms to IQA standard
 * @author David Donohue
 *
 */
public class GaeEntityFactory {

	private static final String[] IGNORABLE_PROPERTIES_ARRAY = {
		"class"
	};
	
	private static final List<String> IGNORABLE_PROPERTIES = Arrays.asList(IGNORABLE_PROPERTIES_ARRAY);
	
	public Entity getEntity(IQABean bean) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (bean == null) return null;
		Key key = null;
		if (bean.getKey()!=null) {
			Log.info("parsing key: " + bean.getKey());
			key = KeyFactory.stringToKey(bean.getKey());
		} else if (bean.getId()!=null) {
			key = KeyFactory.createKey(bean.getClass().getSimpleName(), bean.getId());
		}
		Entity entity = new Entity(key);
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		
		//loop thru all properties of the bean's class.  Skip ignorable properies and those that do not have a setter.  
		//For others, create a property in the new entity
		for (PropertyDescriptor pDescriptor: beanInfo.getPropertyDescriptors()) {
			String propertyName = pDescriptor.getName();
			if (IGNORABLE_PROPERTIES.contains(propertyName)) continue;
			Method getter = pDescriptor.getReadMethod();
			if (getter == null) continue;
			Object value = getter.invoke(bean);
			entity.setProperty(propertyName, value);
		}
		return entity;
	}


}
