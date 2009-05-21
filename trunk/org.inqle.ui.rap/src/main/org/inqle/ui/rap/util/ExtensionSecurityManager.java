package org.inqle.ui.rap.util;

import java.util.ArrayList;
import java.util.List;

import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;

public class ExtensionSecurityManager {

	public static boolean userHasAccess(IExtensionSpec spec) {
		String roles = spec.getAttribute(InqleInfo.ACCESS_ATTRIBUTE);
		if (roles==null || roles.length()==0) return true;
		String[] rolesArr = roles.split(",");
		List<String> userRoles = InqleUiInfo.getUserRoles();
		for (String requiredRole: rolesArr) {
			if (userRoles.contains(requiredRole)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get a list of all extensions objects extending the provided extension point
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static List<Object> getPermittedExtensions(String extensionPointId) {
		List<Object> extList = new ArrayList<Object>();
		
		List<IExtensionSpec> extSpecs = getPermittedExtensionSpecs(extensionPointId);
		for (IExtensionSpec spec: extSpecs) {
			Object extObj = ExtensionFactory.createExtensionObject(spec);
			extList.add(extObj);
		}
		return extList;
	}

	/**
	 * Get a list of all extensions specs (each represented by a IExtensionSpec
	 * object) extending the provided extension point
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static List<IExtensionSpec> getPermittedExtensionSpecs(String extensionPointId) {
		List<IExtensionSpec> extensionSpecs = ExtensionFactory.getExtensionSpecs(extensionPointId);
		List<IExtensionSpec> exList = new ArrayList<IExtensionSpec>();
		for (IExtensionSpec spec: extensionSpecs) {
			if (userHasAccess(spec)) {
				exList.add(spec);
			}
		}
		return exList;
	}
}
