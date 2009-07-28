package org.inqle.experiment.rapidminer.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.IAgentWizard;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;
import org.inqle.experiment.rapidminer.IRapidMinerExperiment;
import org.inqle.experiment.rapidminer.Learner;
import org.inqle.experiment.rapidminer.RapidMinerExperimentLister;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IValueUpdater;
import org.inqle.ui.rap.actions.DynaWizard;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.NumericFieldPage;
import org.inqle.ui.rap.pages.RadioOrListSelectorPage;
import org.inqle.ui.rap.pages.SimpleListSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentWizard extends DynaWizard implements IAgentWizard, IListProvider, IList2Provider, IValueUpdater {

	private static final String OPTION_RANDOM_SAMPLER = "Use a randomly-selected Sampler";
	private static final String OPTION_RANDOM_RAPIDMINER_EXPERIMENT = "Use a randomly-selected RapidMiner Experiment";
//	private static final String OPTION_BASE_LC = "Use the Base Learning Cycle, which itself makes all random selections";
	private static Logger log = Logger.getLogger(ExperimenterAgentWizard.class);
	private ExperimenterAgent experimenterAgent;
	private SimpleListSelectorPage samplerSelectorPage;
	private List<Object> samplerOptions;
	private List<Object> experimentOptions;
	List<ISampler> samplers;
	List<IRapidMinerExperiment> rapidMinerExperiments;
	private SimpleListSelectorPage experimentSelectorPage;

	public ExperimenterAgentWizard(ExperimenterAgent experimenterAgent, Shell shell) {
		super(shell);
		this.experimenterAgent = experimenterAgent;
	}

	@Override
	/**
	 * TODO finish wizard
	 */
	public void addPages() {
		//log.info("ExperimenterAgentWizard adding pages...");
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage("Name and Description", null);
		addPage(nameDescriptionPage);
		log.info("Added NameDescriptionPage");
		
		samplerSelectorPage = new SimpleListSelectorPage("Select Sampler to use", "Select whether to use a randomly selected Sampler, or specify the Sampler to be used for this Learning Cycle.", "Select Sampler:", SWT.SINGLE);
		addPage(samplerSelectorPage);
		
		experimentSelectorPage = new SimpleListSelectorPage("Select RapidMiner Experiment to use", "Select whether to use a randomly selected RapidMiner experiment, or specify the experiment to be used repeatedly.", "Select RapidMiner Experiment:", SWT.SINGLE);
		addPage(experimentSelectorPage);
		
		NumericFieldPage numberOfRunsPage = new NumericFieldPage(bean, "stoppingPoint", "Enter Number of Executions", "Select number of executions to run (-1 to run continuously).", null);
		addPage(numberOfRunsPage);
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items to display
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getList(IWizardPage page) {
		if (page.equals(samplerSelectorPage)) {
			samplerOptions = new ArrayList<Object>();
			samplerOptions.add(OPTION_RANDOM_SAMPLER);
			if (samplers==null) {
				samplers = SamplerLister.listSamplers(true);
			}
			for (ISampler sampler: samplers) {
				samplerOptions.add(sampler.getName());
			}
			return samplerOptions;
		}
		
		if (page.equals(experimentSelectorPage)) {
			experimentOptions = new ArrayList<Object>();
			experimentOptions.add(OPTION_RANDOM_RAPIDMINER_EXPERIMENT);
			if (rapidMinerExperiments==null) {
				rapidMinerExperiments = RapidMinerExperimentLister.listRapidMinerExperiments();
			}
			for (IRapidMinerExperiment experiment: rapidMinerExperiments) {
				experimentOptions.add(experiment.getName());
			}
			return experimentOptions;
		}
		
		return null;
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items that are pre-selected
	 */
	public List<Object> getList2(IWizardPage page) {
		if (bean == null) return null;
		if (page.equals(samplerSelectorPage)) {
			if (experimenterAgent.getSampler()==null) return null;
			List<Object> listItems = new ArrayList<Object>();
			listItems.add(experimenterAgent.getSampler().getName());
			return listItems;
		}
		
		if (page.equals(experimentSelectorPage)) {
			if (experimenterAgent.getRapidMinerExperiment()==null) return null;
			List<Object> listItems = new ArrayList<Object>();
			listItems.add(experimenterAgent.getRapidMinerExperiment().getName());
			return listItems;
		}
		
		return null;
	}

	/**
	 * This is called by some wizard pages, to permit update of the bean with appropriate value(s)
	 */
	public void updateValue(IWizardPage page) {
		if (bean == null) return;
		if (page.equals(samplerSelectorPage)) {
			experimenterAgent.setSampler(getSelectedSampler());
		}
		if (page.equals(experimentSelectorPage)) {
			experimenterAgent.setRapidMinerExperiment(getSelectedRapidMinerExperiment());
		}
		
	}
	
	public ISampler getSelectedSampler() {
		int selectedIndex = samplerSelectorPage.getSelectedIndex();
		if (selectedIndex < 1) {
			return null;
		} else {
			return samplers.get(selectedIndex - 1);
		}
	}
	
	private IRapidMinerExperiment getSelectedRapidMinerExperiment() {
		int selectedIndex = experimentSelectorPage.getSelectedIndex();
		if (selectedIndex < 1) {
			return null;
		} else {
			return rapidMinerExperiments.get(selectedIndex - 1);
		}
	}

	@Override
	public boolean performFinish() {
		Persister persister = Persister.getInstance();
		experimenterAgent.setSampler(getSelectedSampler());
		experimenterAgent.setRapidMinerExperiment(getSelectedRapidMinerExperiment());
		persister.persist(experimenterAgent);
		return true;
	}

	@Override
	public IPart getPart() {
		return part;
	}

	@Override
	public void setPart(IPart part) {
		this.part = part;
	}

}
