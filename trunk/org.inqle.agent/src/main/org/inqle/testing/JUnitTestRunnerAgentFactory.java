package org.inqle.testing;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentFactory;
import org.inqle.agent.rap.IAgentFactory;
import org.inqle.agent.rap.IAgentWizard;

import com.hp.hpl.jena.rdf.model.Model;

public class JUnitTestRunnerAgentFactory extends AAgentFactory {

	public IAgentFactory cloneFactory(IAgent childAgent) {
		JUnitTestRunnerAgentFactory newFactory = new JUnitTestRunnerAgentFactory();
		newFactory.setBaseAgent(childAgent);
		return newFactory;
	}

	public IAgentWizard createWizard(Model model, Shell shell) {
		JUnitTestRunnerAgentWizard newWizard =  new JUnitTestRunnerAgentWizard(model, shell);
		newWizard.setBean(newAgent());
		return newWizard;
	}

	public IAgentWizard createWizardForReplica(Model model, Shell shell) {
		JUnitTestRunnerAgentWizard newWizard =  new JUnitTestRunnerAgentWizard(model, shell);
		newWizard.setBean((IAgent)getBaseAgent().createReplica());
		return newWizard;
	}

	public boolean hasWizard() {
		return true;
	}

	public JUnitTestRunnerAgent newAgent() {
		JUnitTestRunnerAgent newAgent = new JUnitTestRunnerAgent();
		//newAgent.setPersister(persister);
		return newAgent;
	}

	public IAgentWizard createWizardForClone(Model model, Shell shell) {
		JUnitTestRunnerAgentWizard newWizard =  new JUnitTestRunnerAgentWizard(model, shell);
		newWizard.setBean((IAgent)getBaseAgent().createClone());
		return newWizard;
	}

}
