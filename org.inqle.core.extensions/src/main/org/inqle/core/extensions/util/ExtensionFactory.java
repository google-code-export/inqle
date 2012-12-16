package org.inqle.core.extensions.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.inqle.core.util.InqleInfo;

public class ExtensionFactory {

	private static Logger log = Logger.getLogger(ExtensionFactory.class);
	/**
	 * Get a list of all extension points
	 * @return extensionPoints the List of extension point IDs
	 */
	public static List<String> getExtensionPointSpecs() {
		List<String> extensionPoints = new ArrayList<String>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint[] extensions = registry.getExtensionPoints();
		for (int i=0; i<extensions.length; i++) {
			IExtensionPoint ep = extensions[i];
			extensionPoints.add(ep.getUniqueIdentifier());
		}
		return extensionPoints;
	}
	
	/**
	 * Get a list of all extensions specs (each represented by a IExtensionSpec
	 * object) extending the provided extension point
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static List<IExtensionSpec> getExtensionSpecs(String extensionPointId) {
		List<IExtensionSpec> extList = new ArrayList<IExtensionSpec>();
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extension = registry.getExtensionPoint(extensionPointId);
		
		if (extension == null) {
			log.error("Unable to find extension-point " + extensionPointId);
			return extList;
		} else {
			//log.trace("Found extension-point " + extensionPointId + "; Valid?="+extension.isValid());
			IExtension[] extensions =  extension.getExtensions();
			
			for (int i = 0; i < extensions.length; i++) {
				//String extensionId = extensions[i].getNamespaceIdentifier() + "." + extensions[i].getSimpleIdentifier();
				IConfigurationElement [] configElements = extensions[i].getConfigurationElements();
				//log.info("Found extensions w/ configElements of length="+configElements.length);
				
				for (int j = 0; j < configElements.length; j++) {
					IExtensionSpec sInfo = ExtensionSpecFactory.createExtensionSpec(configElements[j], extensions[i].getContributor().getName());
					extList.add(sInfo);
					//log.info("Added configElement " + sInfo.toString());
				}
			}
		}
		//sort according to weight attribute
		Collections.sort(extList);
		return extList;
	}
	
	public static IExtensionSpec getExtensionSpec(String extensionPointId, String extensionId) {
		if (extensionPointId==null || extensionId==null) return null;
		List<IExtensionSpec> theExtensions = getExtensionSpecs(extensionPointId);
		for (IExtensionSpec extensionSpec: theExtensions) {
			String id = extensionSpec.getAttribute(InqleInfo.ID_ATTRIBUTE);
			if (extensionId.equals(id)) {
				return extensionSpec;
			}
		}
		return null;
		
		//this fails when the extension has no ID (which it could:
//		IExtensionRegistry registry = Platform.getExtensionRegistry();
//		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionPointId);
//		if (extensionPoint==null) {
//			log.warn("Unable to retrieve extension point '" + extensionPoint + "'.");
//			return null;
//		}
//		IExtension extension = extensionPoint.getExtension(extensionId);
//		if (extension==null) {
//			log.warn("Unable to retrieve extension '" + extensionId + "'.");
//			return null;
//		}
//		IConfigurationElement [] configElements = extension.getConfigurationElements();
//		//log.info("Found extensions w/ configElements of length="+configElements.length);
//		
//		IExtensionSpec extensionSpec = ExtensionSpecFactory.createExtensionSpec(configElements[0], extension.getContributor().getName());
//		return extensionSpec;
	}
	
	/**
	 * Get a list of all extensions objects extending the provided extension point
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static List<Object> getExtensions(String extensionPointId) {
		List<Object> extList = new ArrayList<Object>();
		
		List<IExtensionSpec> extSpecs = getExtensionSpecs(extensionPointId);
		for (IExtensionSpec spec: extSpecs) {
			Object extObj = createExtensionObject(spec);
			extList.add(extObj);
		}
		return extList;
	}
	
	/**
	 * Get a list of all extensions objects extending the provided extension point
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static <T> List<T> getExtensionObjects(Class<T> objectClass, String extensionPointId) {
		log.info("getExtensionObjects()...");
		List<T> extList = new ArrayList<T>();
		
		List<IExtensionSpec> extSpecs = getExtensionSpecs(extensionPointId);
		log.info("Got " + extSpecs.size() + " extension specs for extension point: " + extensionPointId);
		for (IExtensionSpec spec: extSpecs) {
			T extObj = createExtensionObject(objectClass, spec);
			extList.add(extObj);
		}
		return extList;
	}
	
	/**
	 * Get a list of all extensions objects extending the provided extension point
	 * here the specified attribute has the specified value
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static <T> List<T> getExtensionObjectsWithValue(Class<T> objectClass, String extensionPointId, String attribute, String value) {
		log.info("getExtensionObjectsWithValue()...");
		List<T> extList = new ArrayList<T>();
		
		List<IExtensionSpec> extSpecs = getExtensionsWithValue(extensionPointId, attribute, value);
		log.info("Got " + extSpecs.size() + " extension specs for extension point: " + extensionPointId);
		for (IExtensionSpec spec: extSpecs) {
			T extObj = createExtensionObject(objectClass, spec);
			if (extObj==null) {
				log.warn("Unable to create object of class " + objectClass);
			}
			log.info("Adding extObj=" + extObj);
			extList.add(extObj);
		}
		return extList;
	}
	
	
	/**
	 * Creates an object, given an extension spec
	 * @param spec
	 * @return
	 */
	public static Object createExtensionObject(IExtensionSpec spec) {
		Object instance = null;
		String className = spec.getAttribute(InqleInfo.CLASS_ATTRIBUTE);
		if (className == null) {
			log.error("Unable to instantiate object from spec " + spec + " as it lacks attribute '" + InqleInfo.CLASS_ATTRIBUTE + "'.");
			return null;
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			log.error("Unable to instantiate object: cannot find class '" + className + "'.", e);
			return null;
		}
		try {
			instance = clazz.newInstance();
			if (instance instanceof IJavaExtension) {
				((IJavaExtension)instance).setSpec(spec);
			}
		} catch (Exception e) {
			log.error("Unable to instantiate object of class " + className, e);
			return null;
		}
		return instance;
	}
	
	/**
	 * Creates an object of the specified class, given an extension spec.
	 * If the class implements IJavaExtension, then the values of this extension will be added to the new object
	 * @param spec
	 * @return
	 */
	public static <T> T createExtensionObject(Class<T> objectClass, IExtensionSpec spec) {
		T instance = null;
//		String className = spec.getAttribute(InqleInfo.CLASS_ATTRIBUTE);
//		if (className == null) {
//			log.error("Unable to instantiate object from spec " + spec + " as it lacks attribute '" + InqleInfo.CLASS_ATTRIBUTE + "'.");
//			return null;
//		}
		try {
			instance = objectClass.newInstance();
			if (instance instanceof IJavaExtension) {
				((IJavaExtension)instance).setSpec(spec);
			}
		} catch (Exception e) {
			log.error("Unable to instantiate object of class " + objectClass.getName(), e);
			return null;
		}
		return instance;
	}

	/**
	 * 
	 * @param extensionPointId the id of the extension point
	 * @param attribute the attribute name
	 * @param value the value this attribute should have, in order for this extension to be returned
	 * @return hashtable of extensions which match the criteria
	 */
	public static List<IExtensionSpec> getExtensionsWithValue(String extensionPointId, String attribute, String value) {
		List<IExtensionSpec> extList = new ArrayList<IExtensionSpec>();
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extension = registry.getExtensionPoint(extensionPointId);
		
		if (extension == null) {
			log.error("Unable to find extension-point " + extensionPointId);
		} else {
			//log.trace("Found extension-point " + extensionPointId + "; Valid?="+extension.isValid());
			IExtension[] extensions =  extension.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
//				String extensionId = extensions[i].getNamespaceIdentifier() + "." + extensions[i].getSimpleIdentifier();
				IConfigurationElement [] configElements = extensions[i].getConfigurationElements();
				//log.trace("Found extensions w/ configElements of length="+configElements.length);
				for (int j = 0; j < configElements.length; j++) {
					String foundVal = configElements[j].getAttribute(attribute);
					log.info("Testing for attribute: " + attribute + ".  Found value: " + foundVal);
					if (foundVal != null && foundVal.equals(value)) {
						IExtensionSpec sElement = ExtensionSpecFactory.createExtensionSpec(configElements[j], extensions[i].getContributor().getName());
						extList.add(sElement);
							

					}
				}
			}
		}
		//sort according to weight attribute
		Collections.sort(extList);
		return extList;
	}

	/**
	 * Creates an instance of any IExtensionSpec object.  Assumes it contains the attribute class.
	 * @param directive the IDirective, which contains info on how to instantiate the class
	 * @param firstFactorySpec
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 *
	public static Object getInstance(IExtensionSpec configInfo, IDirective directive) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		Object newObject = getInstance(configInfo);
		if (newObject instanceof IDirectable) {
			((IDirectable)newObject).setDirective(directive);
		}
		return newObject;
	}
	
	public static Object getInstance(IExtensionSpec configInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String className = configInfo.getAttribute("class");
		if (className==null || className.length()==0) {
			throw new MissingServiceAttributeException("In plugin.xml, plugin is missing the 'class' attribute.");
		}
		Class<?> classToCreate = Class.forName(className);
		Object newObject = classToCreate.newInstance();
		if (newObject instanceof IConfigurableViaDS) {
			((IConfigurableViaDS)newObject).configureViaDS(configInfo);
		}
		return newObject;
	}
	*/
	
	
	/*
	public static BundleContext getBundleContext(HttpServletRequest _req) {
		return (BundleContext)_req.getAttribute(InqleInfo.PARAM_BUNDLE_CONTEXT);
	}
	
	public static List<Bundle> listAgents(HttpServletRequest _req) {
		ArrayList<Bundle> agents = new ArrayList<Bundle>();
		List agentids = listAgentIds(_req);
		
		Bundle[] bundles = getBundleContext(_req).getBundles();
		for (int i=0; i<bundles.length; i++) {
			String bundleName = bundles[i].getSymbolicName();
			if (agentids.contains(bundleName)) {
				agents.add(bundles[i]);
			}
		}
    return agents;
	}
	
	public static Bundle getAgent(String agentName, HttpServletRequest _req) {
		List agents = listAgents(_req);
		Iterator agentI = agents.iterator();
		while (agentI.hasNext()) {
			Bundle agent = (Bundle)agentI.next();
			if (agent.getSymbolicName().equals(agentName)) return agent;
		}
		return null;
	}
	
	public static List<String> listAgentIds(HttpServletRequest _req) {
		ArrayList<String> agentids = new ArrayList<String>();
		List agentExtensions = getExtensions(InqleInfo.EXTENSION_POINT_AGENT);
		//log.info("Found " + agentExtensions.size() + " extensions for point " + InqleInfo.EXTENSION_POINT_AGENT);
		//convert this list of bundle config info into list of IDs
		Iterator aeI = agentExtensions.iterator();
		while (aeI.hasNext()) {
			IExtensionSpec ci = (IExtensionSpec)aeI.next();
			String agentid = ci.getAttribute("id");
			//log.info("Looping on bundle " + agentid);
			agentids.add(agentid);
		}
		//log.info("Found agent IDs:" + agentids);
    return agentids;
	}
	*/
}
