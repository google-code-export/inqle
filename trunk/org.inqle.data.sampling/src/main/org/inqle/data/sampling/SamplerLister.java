package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.rap.ISamplerFactory;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;

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
	
	@SuppressWarnings("unchecked")
	public static List<ISampler> listCustomSamplers(ISampler baseSampler) {
		Persister persister = Persister.getInstance();
		Class<?> samplerClass = baseSampler.getClass();
		Collection<?> samplerObjCollection = persister.reconstituteAll(samplerClass);
		List<?> samplerObjList = new ArrayList<Object>(samplerObjCollection);
		return (List<ISampler>) samplerObjList;
	}
	
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
	
	
//	private static String getSparqlToFindChildren(ISampler sampler) {
//		String sparql = "PREFIX rdf: <" + RDF.RDF + ">\n" + 
//			//"PREFIX ja: <" + RDF.JA + ">\n" + 
//			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
//			"SELECT ?id \n" +
//			"{\n" +
//			"GRAPH ?g {\n" +
//			"?samplerUri inqle:id ?id \n" +
//			" . ?samplerUri a ?classUri\n" +
//			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + 
//			sampler.getClass().getName()  +
//			"\" \n" +
//			"\n} }\n";
//		return sparql;
//	}
}
