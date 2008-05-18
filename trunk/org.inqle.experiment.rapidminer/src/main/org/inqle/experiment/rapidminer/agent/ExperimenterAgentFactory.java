package org.inqle.experiment.rapidminer.agent;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentFactory;
import org.inqle.agent.rap.IAgentFactory;
import org.inqle.agent.rap.IAgentWizard;
import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentFactory extends AAgentFactory {

	public IAgentFactory cloneFactory(IAgent childAgent) {
		ExperimenterAgentFactory newFactory = new ExperimenterAgentFactory();
		newFactory.setBaseAgent(childAgent);
		//newFactory.setPersister(persister);
		newFactory.setName(getName());
		newFactory.setDescription(getDescription());
		return newFactory;
	}

	public IAgentWizard createWizard(Model model, Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(model, shell);
		return newWizard;
	}

	public IAgentWizard createWizardForReplica(Model model,	Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(model, shell);
		newWizard.setBean(getBaseAgent().createReplica());
		return newWizard;
	}

	public boolean hasWizard() {
		return true;
	}

	public IAgent newAgent() {
		ExperimenterAgent newAgent = new ExperimenterAgent();
		//newAgent.setPersister(persister);
		return newAgent;
	}

}
