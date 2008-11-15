/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.IValueUpdater;
import org.inqle.ui.rap.actions.DynaWizard;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.RadioOrListSelectorPage;
import org.inqle.ui.rap.pages.SimpleListSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Apr 15, 2008
 */
public class LearningCycleWizard extends DynaWizard implements IListProvider, IList2Provider, IValueUpdater {

	//private LearningCycle learningCycle;

//	private static final String[] RADIO_OPTIONS = {
//		"Use a randomly-selected Sampler",
//		"Use the below-selected Sampler"
//	};

	private static final String OPTION_USE_RANDOM_SAMPLER = "(Randomly select a sampler each cycle)";
	
	private static Logger log = Logger.getLogger(LearningCycleWizard.class);

	private SimpleListSelectorPage samplerSelectorPage;

	private List<ISampler> allSamplers;
	
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
		
		samplerSelectorPage = new SimpleListSelectorPage("Select sampler to use", "Select whether to use a randomly selected sampler, or specify the sampler to be used for this learning cycle.", "Select sampler:", SWT.SINGLE);
		addPage(samplerSelectorPage);
		
		SamplingResultPage samplingResultsPage = new SamplingResultPage(learningCycle, "Result of Sampling");
		addPage(samplingResultsPage);
		
//		LabelSelectorPage labelSelectorPage = new LabelSelectorPage(learningCycle, "labelDataColumn", "Select column to be used as the label", null);
//		labelSelectorPage.setDataTableProvider(samplingResultsPage);
//		addPage(labelSelectorPage);
	}
	
	/**
	 * This is called by some wizard pages, to retrieve the list of items to display
	 */
	@SuppressWarnings("unchecked")
	public List getList(IWizardPage page) {
		if (bean == null) return null;
		if (page.equals(samplerSelectorPage)) {
			List<String> listItems = new ArrayList<String>();
			listItems.add(OPTION_USE_RANDOM_SAMPLER);
			if (allSamplers==null) {
				allSamplers = SamplerLister.listSamplers();
			}
			for (ISampler sampler: allSamplers) {
				listItems.add(sampler.getName());
			}
			return listItems;
		}
		
		return null;
	}

	public List<Object> getList2(IWizardPage page) {
		if (bean == null) return null;
		LearningCycle learningCycle = (LearningCycle) bean;
		if (page.equals(samplerSelectorPage)) {
			if (learningCycle.getSampler()==null) return null;
			List<Object> listItems = new ArrayList<Object>();
			listItems.add(learningCycle.getSampler().getName());
			return listItems;
		}
		
		return null;
	}

	public void updateValue(IWizardPage page) {
		if (bean == null) return;
		LearningCycle learningCycle = (LearningCycle) bean;
		if (page.equals(samplerSelectorPage)) {
			int selectedSamplerIndex = samplerSelectorPage.getSelectedIndex();
			if (selectedSamplerIndex < 1) {
				learningCycle.setSampler(null);
			} else {
				learningCycle.setSampler(allSamplers.get(selectedSamplerIndex - 1));
			}
		}
	}
}
