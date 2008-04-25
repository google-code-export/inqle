package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.List;

import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.data.sampling.rap.ISamplerFactory;
import org.inqle.ui.rap.IPart;

public class SamplerLister {

	public static List<ISampler> listSamplers() {
		List<ISampler> samplers = new ArrayList<ISampler>();
		
		//first add the base plugins
		List<Object> objects =  ExtensionFactory.getExtensions(ISamplerFactory.ID);
		for (Object object: objects) {
			if (object == null) continue;
			ISamplerFactory samplerFactory = (ISamplerFactory)object;
			samplers.add(samplerFactory.newSampler());
		}
		
		//next add customized samplers
		//TODO
		
		return samplers;
	}
}
