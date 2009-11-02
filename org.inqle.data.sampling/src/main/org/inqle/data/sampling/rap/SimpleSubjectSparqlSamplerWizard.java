package org.inqle.data.sampling.rap;

import java.net.URI;
import java.util.ArrayList;
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
import org.inqle.data.sampling.SamplingInfo;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.IValueUpdater;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.SimpleListSelectorPage;

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
	private MinMaxPage minMaxPage;
//	private SimpleSubjectSparqlSampler sampler;
//	
//	public SimpleSubjectSparqlSampler getSampler() {
//		return sampler;
//	}
//
//	public void setSampler(SimpleSubjectSparqlSampler sampler) {
//		this.sampler = sampler;
//	}
	private SimpleListSelectorPage selectedModelsPage;
	private List<String> userDatamodelStrings;
//	private List<String> userDatamodels;
	private List<String> userDatamodelIds;

	public SimpleSubjectSparqlSamplerWizard(Shell shell) {
		super(shell);
	}

	@Override
	public void addPages() {
		SimpleSubjectSparqlSampler sssSampler = (SimpleSubjectSparqlSampler)getSampler();
		//log.info("addPages() called; SimpleSubjectSparqlSamplerWizard has model bean: " + JenabeanWriter.toString(sampler));
//		SingleTextPage samplerNamePage = new SingleTextPage(sampler, "name", "Sampler name", null);
//		samplerNamePage.setLabelText("Sampler Name");
//		addPage(samplerNamePage);
		
		nameDescriptionPage = new NameDescriptionPage("Name and Description", null);
		nameDescriptionPage.setTheName(sssSampler.getName());
		nameDescriptionPage.setTheDescription(sssSampler.getDescription());
		addPage(nameDescriptionPage);
		
		selectedModelsPage = new SimpleListSelectorPage("Select datamodel(s) for sampling", 
				"Optionally select which datamodel(s) from which to extract data.", 
				"Select datamodels:", 
				SWT.MULTI);
		addPage(selectedModelsPage);
		
		subjectClassSelectorPage = new SimpleListSelectorPage("Select Class of Subject", 
				"Optionally select the class of the subject, with which to build the sample data set.  If no subject class is selected, then each run, it will be randomly selected.", 
				"Select subject:", 
				SWT.SINGLE);
		addPage(subjectClassSelectorPage);
		
		labelSelectorPage = new SimpleListSelectorPage(
				"Select property to be predicted", 
				"Optionally select the property to be used in the data set, as the 'Experimental Label'.  That is, the value to be predicted.  If no selection is made, then each run, the Experimental Label will be randomly selected.", 
				"Select properties:", SWT.SINGLE);
		addPage(labelSelectorPage);
		
		minMaxPage = new MinMaxPage(
				"Number of Attributes", 
				"Enter the number of attributes to test each run.  Specify the minimum and maximum number of attributes to select.  Each run, a random number will be selected in this range.",
				sssSampler.getMinLearnablePredicates(),
				sssSampler.getMaxLearnablePredicates()
		);
		addPage(minMaxPage);
		
		arcSelectorPage = new SimpleListSelectorPage(
				"Select properties for learning", 
				"Optionally, select the properties to use in the data set.  If no properties are selected, then each run, properties will be randomly selected from the entire set.", 
				"Select properties:", 
				SWT.MULTI);
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
		SimpleSubjectSparqlSampler sssSampler = (SimpleSubjectSparqlSampler)getSampler();
		sssSampler.setName(nameDescriptionPage.getName());
		sssSampler.setDescription(nameDescriptionPage.getDescription());
		Integer minAttributes = minMaxPage.getMinVal();
		if (minAttributes != null) {
			sssSampler.setMinLearnablePredicates(minAttributes);
		}
		Integer maxAttributes = minMaxPage.getMaxVal();
		if (maxAttributes != null) {
			sssSampler.setMaxLearnablePredicates(maxAttributes);
		}
		Persister persister = Persister.getInstance();
		persister.persist(sssSampler);
		
		return true;
	}

//	private List<String> getUserDatamodelStrings() {
//		if (userDatamodelStrings==null) {
//			userDatamodelStrings = new ArrayList<String>();
//			for (Datamodel datamodel: getUserDatamodels()) {
//				userDatamodelStrings.add(datamodel.getName());
//			}
//		}
//		return userDatamodelStrings;
//	}
	
//	private List<Datamodel> getUserDatamodels() {
//		if (userDatamodels==null) {
//			Persister persister = Persister.getInstance();
//			userDatamodels = persister.listDatamodels(InqleInfo.USER_DATABASE_ID);
//		}
//		return userDatamodels;
//	}
	
	private List<String> getUserDatamodelIds() {
		if (userDatamodelIds==null) {
			Persister persister = Persister.getInstance();
			userDatamodelIds = persister.listDatamodelIds(InqleInfo.USER_DATABASE_ID);
		}
		return userDatamodelIds;
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items to display
	 */
	@SuppressWarnings("unchecked")
	public List getList(IWizardPage page) {
		SimpleSubjectSparqlSampler sssSampler = (SimpleSubjectSparqlSampler)getSampler();
		if (sssSampler == null) return null;
		
		if (page.equals(selectedModelsPage)) {
			return getUserDatamodelIds();
		}
		if (page.equals(subjectClassSelectorPage)) {
			Collection<String> selectedModelsCollection = sssSampler.getSelectedDatamodels();
			log.info("GGGGGGGGGGGGGGGGGGG Get subjects for datamodels: " + selectedModelsCollection );
			return SubjectClassLister.getUncommonSubjectClasses(SamplingInfo.SAMPLER_DB, selectedModelsCollection);
		}
		
		if (page.equals(arcSelectorPage) || page.equals(labelSelectorPage)) {
			Collection<String> selectedModelsCollection = sssSampler.getSelectedDatamodels();
			URI subjectClass = sssSampler.getSubjectClass();
			if (selectedModelsCollection == null || selectedModelsCollection.size()==0 || subjectClass == null) {
				log.info("Returning NULL for list of Arcs");
				arcsList = null;
			} else {
				arcsList = new ArrayList<Arc>(ArcLister.getFilteredValuedArcs(SamplingInfo.SAMPLER_DB, selectedModelsCollection, subjectClass.toString(), SimpleSubjectSparqlSampler.MAX_PROPERTY_ARC_DEPTH));
				log.info("Returning this collection of Arcs: " + arcsList);
			}
			return arcsList;
		}
		return null;
	}

	public void updateValue(IWizardPage page) {
		SimpleSubjectSparqlSampler sssSampler = (SimpleSubjectSparqlSampler)getSampler();
		if (sssSampler==null) return;
		
		if (page.equals(selectedModelsPage)) {
			if (selectedModelsPage.getSelectedStrings()==null || selectedModelsPage.getSelectedStrings().length==0) {
				sssSampler.setSelectedDatamodels(null);
			} else {
				List<String> selectedDatamodelIds = new ArrayList<String>();
				//add each selected ID to the sssSampler
				for (int selectedIndex: selectedModelsPage.getSelectedIndexes()) {
					String selectedDM = getUserDatamodelIds().get(selectedIndex);
					selectedDatamodelIds.add(selectedDM);
				}
				sssSampler.setSelectedDatamodels(selectedDatamodelIds);
			}
		}
		
		if (page.equals(subjectClassSelectorPage)) {
			if (subjectClassSelectorPage.getSelectedString()==null) {
				sssSampler.setSubjectClass(null);
				log.info("Updated sampler with null");
			} else {
				String selectedSubject = subjectClassSelectorPage.getSelectedString();
				sssSampler.setSubjectClass(URI.create(selectedSubject));
				log.info("Updated sampler with subject class: " + selectedSubject);
			}
		}
		
		if (page.equals(labelSelectorPage)) {
			if (labelSelectorPage.getSelectedIndexes()==null || labelSelectorPage.getSelectedIndexes().length==0) {
				sssSampler.setLabelArc(null);
			} else {
				int selectedArcIndex = labelSelectorPage.getSelectedIndex();
				sssSampler.setLabelArc(arcsList.get(selectedArcIndex));
				log.info("Updated sampler with label arc: " + arcsList.get(selectedArcIndex));
			}
		}
		
		if (page.equals(arcSelectorPage)) {
			if (arcSelectorPage.getSelectedIndexes()==null || arcSelectorPage.getSelectedIndexes().length==0) {
				sssSampler.setArcs(null);
			} else {
				List<Arc> newlySelectedArcs = new ArrayList<Arc>();
				for (int selectedArcIndex: arcSelectorPage.getSelectedIndexes()) {
					newlySelectedArcs.add(arcsList.get(selectedArcIndex));
				}
				sssSampler.setArcs(newlySelectedArcs);
				log.info("Updated sampler with subject arcs: " + newlySelectedArcs);
			}
		}
	}

	/**
	 * Return the list of things which are already selected in the SSSSampler
	 */
	@SuppressWarnings("unchecked")
	public List getList2(IWizardPage page) {
		SimpleSubjectSparqlSampler sssSampler = (SimpleSubjectSparqlSampler)getSampler();
		if (sssSampler==null) return null;
		
		if (page.equals(selectedModelsPage)) {
//			List<String> selectedDatamodelNames = new ArrayList<String>();
//			Collection<String> selectedDatamodelIds = sssSampler.getSelectedDatamodels();
//			if (selectedDatamodelIds==null || selectedDatamodelIds.size()==0) return null;
//			
//			for (String datamodelId: getUserDatamodelIds()) {
//				if (selectedDatamodelIds.contains(datamodelId)) {
//					selectedDatamodelNames.add(datamodel.getName());
//				}
//			}
//			return selectedDatamodelNames;
			return new ArrayList<String>(sssSampler.getSelectedDatamodels());
		}
		
		if (sssSampler == null) return null;
		if (page.equals(subjectClassSelectorPage)) {
			List<String> selectedSubjectClass = new ArrayList<String>();
			URI subjectClassUri = sssSampler.getSubjectClass();
			if (subjectClassUri != null) {
				selectedSubjectClass.add(subjectClassUri.toString());
			}
			return selectedSubjectClass;
		}
		if (page.equals(labelSelectorPage)) {
			Arc selectedLabelArc = sssSampler.getLabelArc();
			if (selectedLabelArc==null) return null;
			List<String> selectedLabelArcList = new ArrayList<String>();
			selectedLabelArcList.add(selectedLabelArc.toString());
			return selectedLabelArcList;
		}
		if (page.equals(arcSelectorPage)) {
			Collection<Arc> selectedArcsCollection = sssSampler.getArcs();
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
