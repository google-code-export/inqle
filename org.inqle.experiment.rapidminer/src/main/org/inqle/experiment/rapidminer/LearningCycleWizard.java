/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;
import org.inqle.ui.rap.actions.DynaWizard;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.RadioOrListSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Apr 15, 2008
 */
public class LearningCycleWizard extends DynaWizard {

	//private LearningCycle learningCycle;

	private static final String[] RADIO_OPTIONS = {
		"Use a randomly-selected Sampler",
		"Use the below-selected Sampler"
	};
	
	private static Logger log = Logger.getLogger(LearningCycleWizard.class);
	
	public LearningCycleWizard(LearningCycle learningCycle, Model learningCycleModel, Shell shell) {
		super(learningCycleModel, shell);
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
		
//		ListSelectorPage samplerSelectorPage = new ListSelectorPage(learningCycle, "sampler", "Select sampler to use", null);
//		samplerSelectorPage.setBeanItemClass(ISampler.class);
//		ISampler[] nullSamplerArray = {};
//		samplerSelectorPage.setListItems(SamplerLister.listSamplers().toArray(nullSamplerArray));
//		addPage(samplerSelectorPage);
		
		RadioOrListSelectorPage samplerSelectorPage = new RadioOrListSelectorPage(bean, "sampler", "Select Sampler to use", null);
		samplerSelectorPage.setBeanItemClass(ILearningCycle.class);
		ISampler[] nullSamplerArray = {};
		samplerSelectorPage.setListItems(SamplerLister.listSamplers().toArray(nullSamplerArray));
		List<String> radioOptionTexts = Arrays.asList(RADIO_OPTIONS);
		samplerSelectorPage.setRadioOptionTexts(radioOptionTexts);
		SamplerBeanProvider samplerBeanProvider = new SamplerBeanProvider();
		samplerBeanProvider.setBean(bean);
		samplerSelectorPage.setRadioBeanProvider(samplerBeanProvider);
		addPage(samplerSelectorPage);
		
		SamplingResultPage samplingResultsPage = new SamplingResultPage(learningCycle, "Result of Sampling");
		//samplingResultsPage.setPersister(persister);
		addPage(samplingResultsPage);
		
//		LabelSelectorPage labelSelectorPage = new LabelSelectorPage(learningCycle, "labelDataColumn", "Select column to be used as the label", null);
//		labelSelectorPage.setDataTableProvider(samplingResultsPage);
//		addPage(labelSelectorPage);
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
