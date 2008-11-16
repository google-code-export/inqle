package org.inqle.experiment.rapidminer.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.experiment.rapidminer.ILearningCycle;
import org.inqle.experiment.rapidminer.LearningCycle;
import org.inqle.experiment.rapidminer.LearningCycleLister;
import org.inqle.ui.rap.IList2Provider;
import org.inqle.ui.rap.IListProvider;
import org.inqle.ui.rap.IValueUpdater;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.NumericFieldPage;
import org.inqle.ui.rap.pages.RadioOrListSelectorPage;
import org.inqle.ui.rap.pages.SimpleListSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentWizard extends AAgentWizard  implements IListProvider, IList2Provider, IValueUpdater {

	private static final String OPTION_RANDOM_LC = "Use a randomly-selected Learning Cycle";
//	private static final String OPTION_BASE_LC = "Use the Base Learning Cycle, which itself makes all random selections";
	private static Logger log = Logger.getLogger(ExperimenterAgentWizard.class);
	private SimpleListSelectorPage learningCycleSelectorPage;
	private ArrayList learningCycleOptions;
	private List<LearningCycle> learningCycles;
	
	public ExperimenterAgentWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
	}

	@Override
	/**
	 * TODO finish wizard
	 */
	public void addPages() {
		//log.info("ExperimenterAgentWizard adding pages...");
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(bean, "Name and Description", null);
		addPage(nameDescriptionPage);
		log.info("Added NameDescriptionPage");
		
//		ListSelectorPage learningCycleSelectorPage = new ListSelectorPage(bean, "learningCycle", "Select Learning Cycle to use", null);
//		learningCycleSelectorPage.setBeanItemClass(ILearningCycle.class);
//		ILearningCycle[] nullLCArray = {};
//		learningCycleSelectorPage.setListItems(LearningCycleLister.listAllLearningCycles().toArray(nullLCArray));
//		addPage(learningCycleSelectorPage);
		
//		RadioOrListSelectorPage learningCycleSelectorPage = new RadioOrListSelectorPage(bean, "learningCycle", "Select Learning Cycle to use", null);
//		learningCycleSelectorPage.setBeanItemClass(ILearningCycle.class);
//		ILearningCycle[] nullLCArray = {};
//		learningCycleSelectorPage.setListItems(LearningCycleLister.listCustomizedLearningCycles().toArray(nullLCArray));
//		List<String> radioOptionTexts = Arrays.asList(RADIO_OPTIONS);
//		//learningCycleSelectorPage.setSelectedOptionIndex();
//		learningCycleSelectorPage.setRadioOptionTexts(radioOptionTexts);
//		ExperimenterBeanProvider experimenterBeanProvider = new ExperimenterBeanProvider();
//		experimenterBeanProvider.setBean(bean);
//		learningCycleSelectorPage.setRadioBeanProvider(experimenterBeanProvider);
//		addPage(learningCycleSelectorPage);
		learningCycleSelectorPage = new SimpleListSelectorPage("Select Learning Cycle to use", "Select whether to use a randomly selected Learning Cycle, the base Learning Cycle, or a customized Learning Cycle.", "Select Learning Cycle:", SWT.SINGLE);
		addPage(learningCycleSelectorPage);
		
		NumericFieldPage numberOfRunsPage = new NumericFieldPage(bean, "stoppingPoint", "Enter Number of Executions", "Select number of executions to run (-1 to run continuously).", null);
		addPage(numberOfRunsPage);
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items to display
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getList(IWizardPage page) {
		if (page.equals(learningCycleSelectorPage)) {
			learningCycleOptions = new ArrayList();
			learningCycleOptions.add(OPTION_RANDOM_LC);
//			learningCycleOptions.add(OPTION_BASE_LC);
			if (learningCycles==null) {
				learningCycles = LearningCycleLister.listCustomizedLearningCycles();
			}
			for (ILearningCycle learningCycle: learningCycles) {
				learningCycleOptions.add(learningCycle.getName());
			}
			return learningCycleOptions;
		}
		return null;
	}

	/**
	 * This is called by some wizard pages, to retrieve the list of items that are pre-selected
	 */
	public List<Object> getList2(IWizardPage page) {
		if (bean == null) return null;
		ExperimenterAgent experimenterAgent = (ExperimenterAgent) bean;
		if (page.equals(learningCycleSelectorPage)) {
			if (experimenterAgent.getLearningCycle()==null) return null;
			List<Object> listItems = new ArrayList<Object>();
			listItems.add(experimenterAgent.getLearningCycle().getName());
			return listItems;
		}
		return null;
	}

	/**
	 * This is called by some wizard pages, to permit update of the bean with appropriate value(s)
	 */
	public void updateValue(IWizardPage page) {
		if (bean == null) return;
		ExperimenterAgent experimenterAgent = (ExperimenterAgent) bean;
		if (page.equals(learningCycleSelectorPage)) {
			int selectedLCIndex = learningCycleSelectorPage.getSelectedIndex();
			if (selectedLCIndex < 1) {
				experimenterAgent.setLearningCycle(null);
			} else {
				experimenterAgent.setLearningCycle(learningCycles.get(selectedLCIndex - 1));
			}
		}
		
	}

}
