package org.inqle.experiment.rapidminer.agent;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.experiment.rapidminer.ILearningCycle;
import org.inqle.experiment.rapidminer.LearningCycleLister;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.NumericFieldPage;
import org.inqle.ui.rap.pages.RadioOrListSelectorPage;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentWizard extends AAgentWizard {

	private static final String[] RADIO_OPTIONS = {
		"Use a randomly-selected Learning Cycle",
		"Use the Base Learning Cycle, which itself makes all random selections",
		"Use the below-selected Learning Cycle"
	};
	private static Logger log = Logger.getLogger(ExperimenterAgentWizard.class);
	
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
		
		RadioOrListSelectorPage learningCycleSelectorPage = new RadioOrListSelectorPage(bean, "learningCycle", "Select Learning Cycle to use", null);
		learningCycleSelectorPage.setBeanItemClass(ILearningCycle.class);
		ILearningCycle[] nullLCArray = {};
		learningCycleSelectorPage.setListItems(LearningCycleLister.listCustomizedLearningCycles().toArray(nullLCArray));
		List<String> radioOptionTexts = Arrays.asList(RADIO_OPTIONS);
		//learningCycleSelectorPage.setSelectedOptionIndex();
		learningCycleSelectorPage.setRadioOptionTexts(radioOptionTexts);
		ExperimenterBeanProvider experimenterBeanProvider = new ExperimenterBeanProvider();
		experimenterBeanProvider.setBean(bean);
		learningCycleSelectorPage.setRadioBeanProvider(experimenterBeanProvider);
		addPage(learningCycleSelectorPage);
		
		NumericFieldPage numberOfRunsPage = new NumericFieldPage(bean, "stoppingPoint", "Enter Number of Executions", "Select number of executions to run (-1 to run continuously).", null);
		addPage(numberOfRunsPage);
	}

}
