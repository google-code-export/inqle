package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.rap.ISamplerFactory;

public class SamplerLister {

//	private Persister persister;

	static Logger log = Logger.getLogger(SamplerLister.class);
	
//	public static List<ISampler> listCustomSamplers(ISampler baseSampler, Persister persister) {
//		List<ISampler> customSamplers = new ArrayList<ISampler>();
//		AppInfo appInfo = persister.getAppInfo();
//		QueryCriteria queryCriteria = new QueryCriteria(persister);
//		queryCriteria.setQuery(getSparqlToFindChildren(baseSampler));
//		queryCriteria.addNamedModel(appInfo.getRepositoryNamedModel());
//		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
//		
//		//for each item in resultTable, add a ModelPart
//		for (QuerySolution row: resultTable.getResultList()) {
//			Literal idLiteral = row.getLiteral("id");
//			log.debug("Reconstituting Sampler of class " + baseSampler.getClass() + ": " + idLiteral.getLexicalForm());
//			ISampler customSampler = (ISampler)Persister.reconstitute(baseSampler.getClass(), idLiteral.getLexicalForm(), persister.getMetarepositoryModel(), true);
//			customSamplers.add(customSampler);
//		}
//		
//		return customSamplers;
//	}
	
	public static List<ISampler> listCustomSamplers(ISampler baseSampler) {
//		log.info("listing customized Samplers for base sampler of id=" + baseSampler.getId() + "\n" + baseSampler);
		Persister persister = Persister.getInstance();
		Class<?> samplerClass = baseSampler.getClass();
		Collection<?> samplerObjCollection = persister.reconstituteAll(samplerClass);
		
		//remove the base (uncustomized) sampler if it is present, and add true custom samplers to the List
		List<ISampler> samplers = new ArrayList<ISampler>();
		for (Object samplerObj: samplerObjCollection) {
			ISampler sampler = (ISampler) samplerObj;
			if(sampler.getId().equals(baseSampler.getId())) {
				continue;
			}
			//if the current sampler has already been added, skip it
			if (samplers != null && samplers.contains(sampler)) {
//				log.info("Sampler already added; skipping it: " + sampler);
				continue;
			}
			//log.info("Adding Sampler of id=" + sampler.getId() + "\n" + sampler);
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
	public static List<ISampler> listSamplers() {
		List<ISampler> samplers = new ArrayList<ISampler>();
		
		//first add the base plugins
		List<Object> objects =  ExtensionFactory.getExtensions(ISamplerFactory.ID);
		for (Object object: objects) {
			if (object == null) continue;
			ISamplerFactory samplerFactory = (ISamplerFactory)object;
			ISampler baseSampler = samplerFactory.newSampler();
			samplers.add(baseSampler);
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
