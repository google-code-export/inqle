package org.inqle.data.rdf.jenabean.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IJenabean;

public class BeanTool {

	private static Logger log = Logger.getLogger(BeanTool.class);
	
	/**
	 * This creates an exact replica of the original
	 * @param <T>
	 * @param original
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T replicate(T original) {
		Class<T> theClass = (Class<T>)original.getClass();
		T replica = null;
		try {
			replica = theClass.newInstance();
			BeanUtils.copyProperties(replica, original);
		} catch (Exception e) {
			log.error("Unable to replicate bean: ", e);
		}
		return replica;
	}
	
	/**
	 * This creates a clone of the original, with a distinct random ID.
	 * @param original
	 * @return clone
	 */
	public static <J extends IJenabean> J clone(J original) {
		J clone = replicate(original);
		clone.setRandomId();
		return clone;
	}
	
	public static String getStringRepresentation(Object bean) {
//		return JenabeanWriter.toString(bean);
//		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
		try {
			return BeanUtils.describe(bean).toString();
		} catch (Exception e) {
			log.error("Unable to serialize bean", e);
		}
		return null;
//		return getStringRepresentationExcludingId(bean);
	}

	/**
	 * Serializes a bean, removing a field named "id"
	 * @param bean
	 * @return
	 */
	public static String getStringRepresentationExcludingId(
			Object bean) {
		log.info("gggggggggggggggggggggggggetStringRepresentationExcludingId()...");
		Map description = new HashMap();
		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		PropertyDescriptor[] descriptors =
            propertyUtilsBean.getPropertyDescriptors(bean);
        Class clazz = bean.getClass();
        for (int i = 0; i < descriptors.length; i++) {
            String name = descriptors[i].getName();
            log.info("PPPPPPPPPP property name=" + name);
            if (name.equals("id")) continue;
            if (getReadMethod(clazz, descriptors[i]) != null) {
                try {
					description.put(name, beanUtilsBean.getProperty(bean, name));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return description.toString();
	}
	
    static Method getReadMethod(Class clazz, PropertyDescriptor descriptor) {
        return (MethodUtils.getAccessibleMethod(clazz, descriptor.getReadMethod()));
    }
}
