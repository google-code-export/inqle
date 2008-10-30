package org.inqle.data.sampling.rap;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.util.ArcLister;
import org.inqle.data.rdf.jena.util.SubjectClassLister;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.SimpleListSelectorPage;
import org.inqle.ui.rap.table.BeanTableSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

public class SimpleSubjectSparqlSamplerWizard extends SamplerWizard implements IListProvider, IList2Provider {

	static Logger log = Logger.getLogger(SimpleSubjectSparqlSamplerWizard.class);
	//private SimpleSubjectSparqlSampler sampler;
	private SimpleListSelectorPage subjectClassSelectorPage;
//	private SamplerBackedSparqlSelectorPage selectPredicatesPage;
	private SimpleListSelectorPage arcSelectorPage;
	private List<Arc> arcsList;
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
		selectedModelsPage.setPropertyNames(Arrays.asList(new String[]{"name", "id", "class"}));
		addPage(selectedModelsPage);
		
		subjectClassSelectorPage = new SimpleListSelectorPage("Select Class of Subject", "Select the class of the subject, with which to build the sample data set.", "Select subject:", SWT.SINGLE);
		addPage(subjectClassSelectorPage);
		
		arcSelectorPage = new SimpleListSelectorPage("Select properties for learning", "Select the properties to use in the data set.", "Select properties:", SWT.MULTI);
		addPage(arcSelectorPage);
		
//		selectPredicatesPage = new SamplerBackedSparqlSelectorPage(sampler, "arcList", Arc.class, "Select properties for learning", null);
//		selectPredicatesPage.setPropertyNames(Arrays.asList(new String[]{"predicate"}));
//		selectPredicatesPage.setKeyPropertyName("predicate");
//		selectPredicatesPage.setColumnWidth(500);
//		addPage(selectPredicatesPage);
		
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
	public boolean canFinish() {
		if (getContainer().getCurrentPage().equals(arcSelectorPage)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean performFinish() {
		//ISampler sampler = (ISampler) bean;
		//sampler.removeInterimData();
		return super.performFinish();
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items to display
	 */
	@SuppressWarnings("unchecked")
	public List getList(IWizardPage page) {
		if (bean == null) return null;
		if (page.equals(subjectClassSelectorPage)) {
			Collection<String> selectedModelsCollection = ((SimpleSubjectSparqlSampler)bean).getSelectedNamedModels();
			return SubjectClassLister.listAllSubjectClasses(selectedModelsCollection);
		}
		
		if (page.equals(arcSelectorPage)) {
			Collection<String> selectedModelsCollection = ((SimpleSubjectSparqlSampler)bean).getSelectedNamedModels();
			URI subjectClass = ((SimpleSubjectSparqlSampler)bean).getSubjectClass();
			if (selectedModelsCollection == null || selectedModelsCollection.size()==0 || subjectClass == null) {
				arcsList = null;
			} else {
				arcsList = ArcLister.listArcs(selectedModelsCollection, subjectClass.toString());
			}
			return arcsList;
		}
		return null;
	}
	
	public void updateValue(IWizardPage page) {
		if (page.equals(subjectClassSelectorPage)) {
			if (subjectClassSelectorPage.getSelectedString()==null) {
				((SimpleSubjectSparqlSampler)bean).setSubjectClass(null);
			} else {
				String selectedSubject = subjectClassSelectorPage.getSelectedString();
				((SimpleSubjectSparqlSampler)bean).setSubjectClass(URI.create(selectedSubject));
				log.info("Updated sampler with subject class: " + selectedSubject);
			}
		}
		
		if (page.equals(arcSelectorPage)) {
			if (subjectClassSelectorPage.getSelectedIndexes()==null || subjectClassSelectorPage.getSelectedIndexes().length==0) {
				((SimpleSubjectSparqlSampler)bean).setSubjectClass(null);
			} else {
				List<Arc> newlySelectedArcs = new ArrayList<Arc>();
				for (int selectedArcIndex: subjectClassSelectorPage.getSelectedIndexes()) {
					newlySelectedArcs.add(arcsList.get(selectedArcIndex));
				}
				((SimpleSubjectSparqlSampler)bean).setArcs(newlySelectedArcs);
				log.info("Updated sampler with subject arcs: " + newlySelectedArcs);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List getList2(IWizardPage page) {
		if (bean == null) return null;
		if (page.equals(subjectClassSelectorPage)) {
			List<String> selectedSubjectClass = new ArrayList<String>();
			URI subjectClassUri = ((SimpleSubjectSparqlSampler)bean).getSubjectClass();
			if (subjectClassUri != null) {
				selectedSubjectClass.add(subjectClassUri.toString());
			}
			return selectedSubjectClass;
		}
		if (page.equals(arcSelectorPage)) {
			Collection<Arc> selectedArcsCollection = ((SimpleSubjectSparqlSampler)bean).getArcs();
			List<Arc> selectedArcsList = new ArrayList<Arc>();
			selectedArcsList.addAll(selectedArcsCollection);
			return selectedArcsList;
		}
		return null;
	}
}
