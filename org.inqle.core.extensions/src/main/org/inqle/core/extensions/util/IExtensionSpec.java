package org.inqle.core.extensions.util;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * This interface contains OSGi configuration, 
 * as a IConfigurationElement.  It adds sortability, by the weight attribute.
 * @author David Donohue
 * Nov 29, 2007
 */
public interface IExtensionSpec extends Comparable<Object> {
	
	public String getAttribute(String attr);
	
	public IConfigurationElement getConfigElement();
	
	public int getWeight();
	
	public String getPluginId();
}
