package org.inqle.core.extensions.util;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Creates instances of IConfigInfo or IExtensionSpec
 * @author David Donohue
 * Nov 29, 2007
 */
public class ExtensionSpecFactory {

	public static IExtensionSpec createExtensionSpec(IConfigurationElement element) {

		//first try to instantiate ExtensionSpec.
		//If unsuccessful, return ConfigInfo instead
		IExtensionSpec sortableCofigInfo = new ExtensionSpec(element);
		return sortableCofigInfo;
	}
}
