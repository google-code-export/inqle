package org.inqle.data.sampling.rap;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.DataTable;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SimpleSparqlSampler;
import org.inqle.ui.rap.actions.NameDescriptionPage;
import org.inqle.ui.rap.actions.SingleTextPage;
import org.inqle.ui.rap.table.BeanTableSelectorPage;
import org.inqle.ui.rap.table.ConverterIdToJenabean;
import org.inqle.ui.rap.table.ConverterJenabeanToId;
import org.inqle.ui.rap.table.RdfTableSelectorPage;
import org.inqle.ui.rap.table.SparqlSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

public class SimpleSparqlSamplerWizard extends SamplerWizard {

	static Logger log = Logger.getLogger(SimpleSparqlSamplerWizard.class);
	//private SimpleSparqlSampler sampler;

	//protected SimpleSparqlSampler bean;
	
	public SimpleSparqlSamplerWizard(Model saveToModel, Persister persister, Shell shell) {
		super(saveToModel, persister, shell);
	}

	@Override
	public void addPages() {
		SimpleSparqlSampler sampler = (SimpleSparqlSampler) bean;
		//log.info("addPages() called; SimpleSparqlSamplerWizard has model bean: " + JenabeanWriter.toString(sampler));
//		SingleTextPage samplerNamePage = new SingleTextPage(sampler, "name", "Sampler name", null);
//		samplerNamePage.setLabelText("Sampler Name");
//		addPage(samplerNamePage);
		
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(sampler, "Name and Description", null);
		addPage(nameDescriptionPage);
		
		BeanTableSelectorPage selectedModelsPage = new BeanTableSelectorPage(sampler, "selectedNamedModels", String.class, "Select dataset(s) for sampling", null);
		selectedModelsPage.setBeans(persister.listNamedModels());
		selectedModelsPage.setTableBeanClass(NamedModel.class);
		selectedModelsPage.setPropertyNames(Arrays.asList(new String[]{"modelName", "id", "class"}));
		addPage(selectedModelsPage);
		
		SamplerBackedSparqlSelectorPage selectPredicatesPage = new SamplerBackedSparqlSelectorPage(sampler, "selectedPredicates", String.class, "Select predicates for learning", null);
		selectPredicatesPage.setPersister(persister);
		selectPredicatesPage.setQuery(SimpleSparqlSampler.SPARQL_GET_DISTINCT_PREDICATES);
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

//	public void setBean(SimpleSparqlSampler sampler) {
//		//assert(samplerObject instanceof SimpleSparqlSampler);
//		//this.sampler = (SimpleSparqlSampler)samplerObject;
//		this.sampler = sampler;
//		//log.info("setBean() called; SimpleSparqlSamplerWizard has model bean: " + JenabeanWriter.toString(sampler));
//	}
	
	@Override
	public boolean performFinish() {
		//ISampler sampler = (ISampler) bean;
		//sampler.removeInterimData();
		return super.performFinish();
	}
}
