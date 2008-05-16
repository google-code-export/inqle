package org.inqle.experiment.rapidminer.agent;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;
import org.inqle.experiment.rapidminer.ILearningCycle;
import org.inqle.experiment.rapidminer.LearningCycleLister;
import org.inqle.ui.rap.pages.ListSelectorPage;
import org.inqle.ui.rap.pages.NameDescriptionPage;
import org.inqle.ui.rap.pages.NumericFieldPage;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentWizard extends AAgentWizard {

	private static Logger log = Logger.getLogger(ExperimenterAgentWizard.class);
	
	public ExperimenterAgentWizard(Model saveToModel, Persister persister,
			Shell shell) {
		super(saveToModel, persister, shell);
	}

	@Override
	/**
	 * TODO finish wizard
	 */
	public void addPages() {
		log.info("ExperimenterAgentWizard adding pages...");
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(bean, "Name and Description", null);
		addPage(nameDescriptionPage);
		log.info("Added NameDescriptionPage");
		
		ListSelectorPage learningCycleSelectorPage = new ListSelectorPage(bean, "learningCycle", "Select Learning Cycle to use", null);
		learningCycleSelectorPage.setBeanItemClass(ILearningCycle.class);
		ILearningCycle[] nullLCArray = {};
		learningCycleSelectorPage.setListItems(LearningCycleLister.listAllLearningCycles(persister).toArray(nullLCArray));
		addPage(learningCycleSelectorPage);
		
		NumericFieldPage numberOfRunsPage = new NumericFieldPage(bean, "stoppingPoint", "Enter Number of Executions", "Select number of executions to run (-1 to run continuously).", null);
		addPage(numberOfRunsPage);
	}

}
