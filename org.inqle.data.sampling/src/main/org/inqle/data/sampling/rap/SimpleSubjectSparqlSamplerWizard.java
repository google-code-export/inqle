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
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.util.ArcLister;
import org.inqle.data.rdf.jena.util.SubjectClassLister;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.IValueUpdater;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.SimpleListSelectorPage;
import org.inqle.ui.rap.table.BeanTableSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

//import example.Datamodel;

public class SimpleSubjectSparqlSamplerWizard extends SamplerWizard implements IListProvider, IList2Provider, IValueUpdater {

	static Logger log = Logger.getLogger(SimpleSubjectSparqlSamplerWizard.class);
	//private SimpleSubjectSparqlSampler sampler;
	private SimpleListSelectorPage subjectClassSelectorPage;
//	private SamplerBackedSparqlSelectorPage selectPredicatesPage;
	private SimpleListSelectorPage arcSelectorPage;
	private List<Arc> arcsList;
	//protected SimpleSubjectSparqlSampler bean;
	private SimpleListSelectorPage labelSelectorPage;
	private NameDescriptionPage nameDescriptionPage;
	
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
		
		nameDescriptionPage = new NameDescriptionPage(sampler, "Name and Description", null);
		addPage(nameDescriptionPage);
		
		BeanTableSelectorPage selectedModelsPage = new BeanTableSelectorPage(sampler, "selectedDatamodels", String.class, "Select dataset(s) for sampling", null);
		Persister persister = Persister.getInstance();
		selectedModelsPage.setBeans(persister.listDatamodels(InqleInfo.USER_DATABASE_ROOT));
		selectedModelsPage.setTableBeanClass(Datamodel.class);
		selectedModelsPage.setPropertyNames(Arrays.asList(new String[]{"name", "id", "class"}));
		addPage(selectedModelsPage);
		
		subjectClassSelectorPage = new SimpleListSelectorPage("Select Class of Subject", "Select the class of the subject, with which to build the sample data set.", "Select subject:", SWT.SINGLE);
		addPage(subjectClassSelectorPage);
		
		labelSelectorPage = new SimpleListSelectorPage("Select property to be predicted", "Select the property to be used in the data set, as the 'experimental label'.  That is, the value to be predicted.", "Select properties:", SWT.SINGLE);
		addPage(labelSelectorPage);
		
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
		if (! getContainer().getCurrentPage().equals(nameDescriptionPage)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean performFinish() {
		//ISampler sampler = (ISampler) bean;
		//sampler.removeInterimData();
		
		//first work around a jena or Jenabean bug, and store all _1, _2, _3 properties for the sequences.
//		String datasetRoleId = Persister.getDatasetRoleId(getBean());
//		Persister persister = Persister.getInstance();
//		Model model = persister.getInternalModel(datasetRoleId);
//		long statementCount = model.size();
//		model.begin();
//		model.add(RDF.li(1), RDF.type, RDF.Property);
//		model.add(RDF.li(2), RDF.type, RDF.Property);
//		model.add(RDF.li(3), RDF.type, RDF.Property);
//		model.commit();
//		log.info("AAAAAAAAAAAAAAAAAA Added " + (model.size()-statementCount) + " statements before adding sampler.");
		//store the sampler Jenabean
		return super.performFinish();
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items to display
	 */
	@SuppressWarnings("unchecked")
	public List getList(IWizardPage page) {
		if (bean == null) return null;
		if (page.equals(subjectClassSelectorPage)) {
			Collection<String> selectedModelsCollection = ((SimpleSubjectSparqlSampler)bean).getSelectedDatamodels();
			log.info("GGGGGGGGGGGGGGGGGGG Get subjects for datasets: " + selectedModelsCollection );
			return SubjectClassLister.getUncommonSubjectClasses(selectedModelsCollection);
		}
		
		if (page.equals(arcSelectorPage) || page.equals(labelSelectorPage)) {
			Collection<String> selectedModelsCollection = ((SimpleSubjectSparqlSampler)bean).getSelectedDatamodels();
			URI subjectClass = ((SimpleSubjectSparqlSampler)bean).getSubjectClass();
			if (selectedModelsCollection == null || selectedModelsCollection.size()==0 || subjectClass == null) {
				log.info("Returning NULL for list of Arcs");
				arcsList = null;
			} else {
				arcsList = new ArrayList<Arc>(ArcLister.getFilteredValuedArcs(selectedModelsCollection, subjectClass.toString(), SimpleSubjectSparqlSampler.MAX_PROPERTY_ARC_DEPTH));
				log.info("Returning this collection of Arcs: " + arcsList);
			}
			return arcsList;
		}
		return null;
	}
	
	public void updateValue(IWizardPage page) {
		if (page.equals(subjectClassSelectorPage)) {
			if (subjectClassSelectorPage.getSelectedString()==null) {
				((SimpleSubjectSparqlSampler)bean).setSubjectClass(null);
				log.info("Updated sampler with null");
			} else {
				String selectedSubject = subjectClassSelectorPage.getSelectedString();
				((SimpleSubjectSparqlSampler)bean).setSubjectClass(URI.create(selectedSubject));
				log.info("Updated sampler with subject class: " + selectedSubject);
			}
		}
		
		if (page.equals(labelSelectorPage)) {
			if (labelSelectorPage.getSelectedIndexes()==null || labelSelectorPage.getSelectedIndexes().length==0) {
				((SimpleSubjectSparqlSampler)bean).setLabelArc(null);
			} else {
				int selectedArcIndex = labelSelectorPage.getSelectedIndex();
				((SimpleSubjectSparqlSampler)bean).setLabelArc(arcsList.get(selectedArcIndex));
				log.info("Updated sampler with label arc: " + arcsList.get(selectedArcIndex));
			}
		}
		
		if (page.equals(arcSelectorPage)) {
			if (arcSelectorPage.getSelectedIndexes()==null || arcSelectorPage.getSelectedIndexes().length==0) {
				((SimpleSubjectSparqlSampler)bean).setArcs(null);
			} else {
				List<Arc> newlySelectedArcs = new ArrayList<Arc>();
				for (int selectedArcIndex: arcSelectorPage.getSelectedIndexes()) {
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
		if (page.equals(labelSelectorPage)) {
			Arc selectedLabelArc = ((SimpleSubjectSparqlSampler)bean).getLabelArc();
			if (selectedLabelArc==null) return null;
			List<String> selectedLabelArcList = new ArrayList<String>();
			selectedLabelArcList.add(selectedLabelArc.toString());
			return selectedLabelArcList;
		}
		if (page.equals(arcSelectorPage)) {
			Collection<Arc> selectedArcsCollection = ((SimpleSubjectSparqlSampler)bean).getArcs();
			if (selectedArcsCollection==null) return null;
			List<String> selectedArcsList = new ArrayList<String>();
			for (Arc selectedArc: selectedArcsCollection) {
				selectedArcsList.add(selectedArc.toString());
			}
			return selectedArcsList;
		}
		return null;
	}
}
