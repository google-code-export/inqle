package org.inqle.data.sampling.rap;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.table.BeanTableSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

public class SimpleSubjectSparqlSamplerWizard extends SamplerWizard {

	static Logger log = Logger.getLogger(SimpleSubjectSparqlSamplerWizard.class);
	//private SimpleSubjectSparqlSampler sampler;

	//protected SimpleSubjectSparqlSampler bean;
	
	public SimpleSubjectSparqlSamplerWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
	}

	@Override
	public void addPages() {
		SimpleSubjectSparqlSampler sampler = (SimpleSubjectSparqlSampler) bean;
		//log.info("addPages() called; SimpleSubjectSparqlSamplerWizard has model bean: " + JenabeanWriter.toString(sampler));
//		SingleTextPage samplerNamePage = new SingleTextPage(sampler, "name", "Sampler name", null);
//		samplerNamePage.setLabelText("Sampler Name");
//		addPage(samplerNamePage);
		
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(sampler, "Name and Description", null);
		addPage(nameDescriptionPage);
		
		BeanTableSelectorPage selectedModelsPage = new BeanTableSelectorPage(sampler, "selectedNamedModels", String.class, "Select dataset(s) for sampling", null);
		Persister persister = Persister.getInstance();
		selectedModelsPage.setBeans(persister.listNamedModels());
		selectedModelsPage.setTableBeanClass(NamedModel.class);
		selectedModelsPage.setPropertyNames(Arrays.asList(new String[]{"modelName", "id", "class"}));
		addPage(selectedModelsPage);
		
		SamplerBackedSparqlSelectorPage selectPredicatesPage = new SamplerBackedSparqlSelectorPage(sampler, "selectedPredicates", String.class, "Select predicates for learning", null);
		//selectPredicatesPage.setPersister(persister);
//		selectPredicatesPage.setQuery(SimpleSubjectSparqlSampler.SPARQL_GET_DISTINCT_PREDICATES);
		selectPredicatesPage.setPropertyNames(Arrays.asList(new String[]{"predicate"}));
		selectPredicatesPage.setKeyPropertyName("predicate");
		selectPredicatesPage.setColumnWidth(500);
		addPage(selectPredicatesPage);
		
//		ColumnAssignerPage columnAssignerPage = new ColumnAssignerPage(sampler, "Identify roles in learning");
//		SamplingResultPage samplingResultsPage = new SamplingResultPage(sampler, "Result of Sampling");
//		samplingResultsPage.setPersister(persister);
//		addPage(samplingResultsPage);
	}

//	@Deprecated
//	private String getTestSparql() {
//		String sparql = 
//			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
//			"PREFIX ja: <" + RDF.JA + ">\n" + 
//			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
//			"SELECT ?uri ?class \n" +
//			"{\n" +
//			"GRAPH ?g {\n";
//			//"?uri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?id \n";
//		sparql += " ?uri a ?classUri\n" +
//			" . ?classUri <" + RDF.JAVA_CLASS + "> ?class \n";
//		sparql += "\n} }\n";
//		return sparql;
//	}

//	@Override
//	public Object getBean() {
//		return this.sampler;
//	}

//	public void setBean(SimpleSubjectSparqlSampler sampler) {
//		//assert(samplerObject instanceof SimpleSubjectSparqlSampler);
//		//this.sampler = (SimpleSubjectSparqlSampler)samplerObject;
//		this.sampler = sampler;
//		//log.info("setBean() called; SimpleSubjectSparqlSamplerWizard has model bean: " + JenabeanWriter.toString(sampler));
//	}
	
	@Override
	public boolean performFinish() {
		//ISampler sampler = (ISampler) bean;
		//sampler.removeInterimData();
		return super.performFinish();
	}
}
