package org.inqle.testing;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentFactory;
import org.inqle.agent.rap.IAgentFactory;
import org.inqle.agent.rap.IAgentWizard;
import org.inqle.data.rdf.jenabean.util.BeanTool;

public class JUnitTestRunnerAgentFactory extends AAgentFactory {

	public IAgentFactory cloneFactory(IAgent childAgent) {
		JUnitTestRunnerAgentFactory newFactory = new JUnitTestRunnerAgentFactory();
		newFactory.setBaseAgent(childAgent);
		return newFactory;
	}

	public IAgentWizard createWizard(Shell shell) {
		JUnitTestRunnerAgentWizard newWizard =  new JUnitTestRunnerAgentWizard(shell);
		newWizard.setAgent(newAgent());
		return newWizard;
	}

	public IAgentWizard createWizardForReplica(Shell shell) {
		JUnitTestRunnerAgentWizard newWizard =  new JUnitTestRunnerAgentWizard(shell);
//		newWizard.setAgent((IAgent)getBaseAgent().createReplica());
		newWizard.setAgent(BeanTool.replicate(getBaseAgent()));
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

	public IAgentWizard createWizardForClone(Shell shell) {
		JUnitTestRunnerAgentWizard newWizard =  new JUnitTestRunnerAgentWizard(shell);
//		newWizard.setAgent((IAgent)getBaseAgent().createClone());
		newWizard.setAgent(BeanTool.clone(getBaseAgent()));
		return newWizard;
	}

}
