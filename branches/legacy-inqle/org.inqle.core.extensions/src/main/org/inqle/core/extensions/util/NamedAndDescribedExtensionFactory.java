package org.inqle.core.extensions.util;

import java.util.ArrayList;
import java.util.List;

import org.inqle.core.domain.INamedAndDescribed;

public class NamedAndDescribedExtensionFactory {

	/**
	 * Get a list of all extensions objects extending the provided extension point
	 * @param extensionPointId the id of the extension point
	 * @return 
	 */
	public static List<INamedAndDescribed> getJenabeanExtensions(String extensionPointId) {
		List<INamedAndDescribed> extList = new ArrayList<INamedAndDescribed>();
		
		List<IExtensionSpec> extSpecs = ExtensionFactory.getExtensionSpecs(extensionPointId);
		for (IExtensionSpec spec: extSpecs) {
			INamedAndDescribed jenabeanObj = createJenabeanObject(spec);
			if (jenabeanObj != null) {
				extList.add(jenabeanObj);
			}
		}
		return extList;
	}
	
	public static INamedAndDescribed createJenabeanObject(IExtensionSpec spec) {
		Object extensionObject = ExtensionFactory.createExtensionObject(spec);
		if (! (extensionObject instanceof INamedAndDescribed)) {
			return null;
		}
		INamedAndDescribed jenabeanObject = (INamedAndDescribed)extensionObject;
		
		String name = spec.getAttribute(INamedAndDescribed.NAME_ATTRIBUTE);
		String description = spec.getAttribute(INamedAndDescribed.DESCRIPTION_ATTRIBUTE);
		jenabeanObject.setName(name);
		jenabeanObject.setDescription(description);
		return jenabeanObject;
	}
}
