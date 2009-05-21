package org.inqle.data.sampling.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.data.sampling.SamplerFactoryFactory;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;
import org.inqle.ui.rap.util.ExtensionSecurityManager;

/**
 * @author David Donohue
 * Feb 5, 2008
 */
public class Samplers extends PartType {
	
	private static Logger log = Logger.getLogger(Samplers.class);
	
	private static final String ICON_PATH = "org/inqle/data/sampling/rap/images/samplers.jpeg";
	/* 
	 * 
	 * The children = the list of all ISampler plugins
	 * add to each child a reference to the Persister object,
	 * such that the entire tree may use common database connections
	 * @see org.inqle.ui.rap.IPartType#getChildren()
	 */
	public IPart[] getChildren() {
		//List<Object> objects =  ExtensionFactory.getExtensions(ISamplerFactory.ID);
		List<IExtensionSpec> extensionSpecs = ExtensionSecurityManager.getPermittedExtensionSpecs(ISamplerFactory.ID);
		IPart[] nullIPartArr = new IPart[] {};
		if (extensionSpecs == null) {
			return nullIPartArr;
		}
		List<IPart> parts = new ArrayList<IPart>();
		//for (Object object: objects) {
		for (IExtensionSpec extensionSpec: extensionSpecs) {
			if (extensionSpec == null) {
				continue;
			}
			//ISamplerFactory samplerFactory = (ISamplerFactory)object;
			ISamplerFactory samplerFactory = SamplerFactoryFactory.createSamplerFactory(extensionSpec);
			SamplerPart part = new SamplerPart(samplerFactory);
			//part.setPersister(persister);
			part.addListener(this.listener);
			parts.add(part);
		}
		IPart[] ipartArr = parts.toArray(nullIPartArr);
		
		return ipartArr;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	public String getName() {
		return "Samplers";
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}

}
