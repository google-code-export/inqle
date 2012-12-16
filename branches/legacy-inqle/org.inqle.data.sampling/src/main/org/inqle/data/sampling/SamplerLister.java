package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.rap.ISamplerFactory;
import org.inqle.ui.rap.util.ExtensionSecurityManager;

public class SamplerLister {

	static Logger log = Logger.getLogger(SamplerLister.class);
	
	public static List<ISampler> listCustomSamplers(ISampler baseSampler) {
		Persister persister = Persister.getInstance();
		Class<?> samplerClass = baseSampler.getClass();
		log.trace("listing customized Samplers for base sampler of class=" + samplerClass + "\n" + baseSampler);
		Collection<?> samplerObjCollection = persister.reconstituteAll(samplerClass);
		log.trace("Reconstituted these:" + samplerObjCollection);
		//remove the base (uncustomized) sampler if it is present, and add true custom samplers to the List
		List<ISampler> samplers = new ArrayList<ISampler>();
		for (Object samplerObj: samplerObjCollection) {
			ISampler sampler = (ISampler) samplerObj;
			if(sampler.getId().equals(baseSampler.getId())) {
				continue;
			}
			//if the current sampler has already been added, skip it
			if (samplers != null && samplers.contains(sampler)) {
				log.trace("Sampler already added; skipping it: " + sampler);
				continue;
			}
			log.trace("Adding Sampler of id=" + sampler.getId() + "\n" + sampler);
			samplers.add(sampler);
		}
		//List<?> samplerObjList = new ArrayList<Object>(samplerObjCollection);
		//return (List<ISampler>) samplerObjList;
		return samplers;
	}
	
	/**
	 * List plugin samplers plus their customized children
	 * @return
	 */
	public static List<ISampler> listSamplers(boolean includeBaseSamplers) {
		List<ISampler> samplers = new ArrayList<ISampler>();
		
		//first add the base plugins
		List<Object> objects =  ExtensionSecurityManager.getPermittedExtensions(ISamplerFactory.ID);
		for (Object object: objects) {
			if (object == null) continue;
			ISamplerFactory samplerFactory = (ISamplerFactory)object;
			ISampler baseSampler = samplerFactory.newSampler();
			if (includeBaseSamplers) {
				samplers.add(baseSampler);
			}
			samplers.addAll(listCustomSamplers(baseSampler));
		}
		
		return samplers;
	}
	
//	public static List<ISamplerFactory> listSamplerFactorys(Persister persister) {
//		List<ISamplerFactory> samplerFactories = new ArrayList<ISamplerFactory>();
//		
//		List<IExtensionSpec> extensionSpecs = ExtensionFactory.getExtensionSpecs(ISamplerFactory.ID);
//		
//		for (IExtensionSpec extensionSpec: extensionSpecs) {
//			if (extensionSpec == null) continue;
//			ISamplerFactory experiment = SamplerFactoryFactory.createSamplerFactory(extensionSpec);
//			experiments.add(experiment);
//		}
//		
//		//TODO v0.2: add experiments from RDF
//		
//		return experiments;
//	}
}
