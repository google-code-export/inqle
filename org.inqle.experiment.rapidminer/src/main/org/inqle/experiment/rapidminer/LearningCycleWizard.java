/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;
import org.inqle.ui.rap.actions.DynaWizard;
import org.inqle.ui.rap.pages.ListSelectorPage;
import org.inqle.ui.rap.pages.NameDescriptionPage;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * @author David Donohue
 * Apr 15, 2008
 */
public class LearningCycleWizard extends DynaWizard {

	//private LearningCycle learningCycle;

	private static Logger log = Logger.getLogger(LearningCycleWizard.class);
	
	public LearningCycleWizard(LearningCycle learningCycle, OntModel learningCycleModel, Persister persister,
			Shell shell) {
		super(learningCycleModel, persister, shell);
//		this.learningCycle = learningCycle;
		this.bean = learningCycle;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.actions.DynaWizard#addPages()
	 */
	@Override
	public void addPages() {
		LearningCycle learningCycle = (LearningCycle)bean;
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(learningCycle, "Name and Description", null);
		addPage(nameDescriptionPage);
		
		ListSelectorPage samplerSelectorPage = new ListSelectorPage(learningCycle, "sampler", "Select sampler to use", null);
		samplerSelectorPage.setBeanItemClass(ISampler.class);
		ISampler[] nullSamplerArray = {};
		samplerSelectorPage.setListItems(SamplerLister.listSamplers(persister).toArray(nullSamplerArray));
		addPage(samplerSelectorPage);
		
		SamplingResultPage samplingResultsPage = new SamplingResultPage(learningCycle, "Result of Sampling");
		samplingResultsPage.setPersister(persister);
		addPage(samplingResultsPage);
		
		LabelSelectorPage labelSelectorPage = new LabelSelectorPage(learningCycle, "labelDataColumn", "Select column to be used as the label", null);
		labelSelectorPage.setDataTableProvider(samplingResultsPage);
		addPage(labelSelectorPage);
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.actions.DynaWizard#getBean()
	 */
//	@Override
//	public Object getBean() {
//		// TODO Auto-generated method stub
//		return this.learningCycle;
//	}

}
