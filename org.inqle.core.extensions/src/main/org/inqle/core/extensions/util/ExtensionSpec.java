package org.inqle.core.extensions.util;

import org.eclipse.core.runtime.IConfigurationElement;
import org.inqle.core.util.InqleInfo;

/**
 * Any plugins which declare their weight in plugin.xml can be sorted. 
 * Default weight = 0, in cases where no numeric weight is specified
 * @author David Donohue
 * Nov 28, 2007
 */
public class ExtensionSpec implements IExtensionSpec {

	protected IConfigurationElement element;
	private String pluginId;
	
	ExtensionSpec(IConfigurationElement element, String pluginId) {
		this.element = element;
		this.pluginId = pluginId;
	}
	
	public String getAttribute(String attr) {
		return element.getAttribute(attr);
	}
	
	public IConfigurationElement getConfigElement() {
		return this.element;
	}
	
	public int compareTo(Object o) {
		int myWeight = getWeight();
		ExtensionSpec compareConfigInfo = (ExtensionSpec) o;
		IConfigurationElement compareElement = compareConfigInfo.getConfigElement();
		int compareWeight = getWeight(compareElement);
		if (compareWeight > myWeight) return -1;
		if (compareWeight < myWeight) return 1;
		return 0;
	}
	
	private int getWeight(IConfigurationElement element) {
		int defaultWeight = 0;
		int weight = defaultWeight;
		String weightStr = element.getAttribute(InqleInfo.ATTRIBUTE_WEIGHT);
		if (weightStr == null || weightStr.length()==0) {
			//throw new MissingServiceAttributeException("In plugin.xml, the attribute '" + InqleInfo.ATTRIBUTE_WEIGHT + "' must be defined.");
			return defaultWeight;
		}
		try {
			weight = Integer.parseInt(weightStr);
		} catch (Exception e) {
			//throw new MissingServiceAttributeException("In plugin.xml, the attribute '" + InqleInfo.ATTRIBUTE_WEIGHT + "' should contain a numeric value.");
			return defaultWeight;
		}
		return weight;
	}
	
	public int getWeight() {
		return getWeight(this.element);
	}
	
	public String toString() {
		if (element == null) return "null";
		String str = "<" + element.getName() + " ";
		String attrNames[] = element.getAttributeNames();
		for (int i=0; i<attrNames.length; i++) {
			str += attrNames[i] + "='";
			str += element.getAttribute(attrNames[i]) + "' ";
		}
		str += "/>";
		return str;
	}

	public String getPluginId() {
		return pluginId;
	}

}
