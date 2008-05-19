package org.inqle.data.sampling;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.sampling.rap.ISamplerFactory;

public class SamplerFactoryFactory {

	private static Logger log = Logger.getLogger(SamplerFactoryFactory.class);
	
	public static ISamplerFactory createSamplerFactory(IExtensionSpec extensionSpec) {
		ISamplerFactory samplerFactory = (ISamplerFactory)ExtensionFactory.createExtensionObject(extensionSpec);
		samplerFactory.setName(extensionSpec.getAttribute(InqleInfo.NAME_ATTRIBUTE));
		samplerFactory.setDescription(extensionSpec.getAttribute(InqleInfo.DESCRIPTION_ATTRIBUTE));
		return samplerFactory;
	}
	
}