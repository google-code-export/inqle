package org.inqle.data.rdf.jenabean.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
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



	/**
	 * This creates an exact replica of the original
	 * @param <T>
	 * @param original
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T replicateToLikeClass(Class<T> newClass, Object original) {
		T replica = null;
		try {
			replica = newClass.newInstance();
			BeanUtils.copyProperties(replica, original);
		} catch (Exception e) {
			log.error("Unable to replicate bean: ", e);
		}
		return replica;
	}
	
//	public static String getStringRepresentation(Object bean) {
////		return JenabeanWriter.toString(bean);
////		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
//		try {
//			return BeanUtils.describe(bean).toString();
//		} catch (Exception e) {
//			log.error("Unable to serialize bean", e);
//		}
//		return null;
////		return getStringRepresentationExcludingId(bean);
//	}

//	/**
//	 * Serializes a bean, removing a field named "id"
//	 * @param bean
//	 * @return
//	 */
//	public static String getStringRepresentationExcludingId(
//			Object bean) {
//		log.info("gggggggggggggggggggggggggetStringRepresentationExcludingId(a " + bean.getClass().getName() + ")...");
//		Map description = new HashMap();
//		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
//		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
//		PropertyDescriptor[] descriptors =
//            propertyUtilsBean.getPropertyDescriptors(bean);
//        Class clazz = bean.getClass();
//        for (int i = 0; i < descriptors.length; i++) {
//            String name = descriptors[i].getName();
//            
//            if (name.equals("id") || name.equals("uri") || name.equals("stringRepresentation") || name.equals("qnameRepresentation")) continue;
//            log.info("CCCCCCCCCCCC Copy property name=" + name);
//            if (getReadMethod(clazz, descriptors[i]) != null) {
//                try {
//					description.put(name, beanUtilsBean.getProperty(bean, name));
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//        }
//        String descrStr = description.toString();
//        log.info("Returning '" + descrStr + "'");
//        return descrStr;
//	}
	
//    static Method getReadMethod(Class clazz, PropertyDescriptor descriptor) {
//        return (MethodUtils.getAccessibleMethod(clazz, descriptor.getReadMethod()));
//    }
}
