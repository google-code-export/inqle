/**
 * 
 */
package org.inqle.testing;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.agent.rap.AgentWizardAction;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.pages.NameDescriptionPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Apr 25, 2008
 */
@TargetDatamodel(IAgent.AGENT_DATASET)
public class JUnitTestRunnerAgentWizard extends AAgentWizard {
	
	private NameDescriptionPage nameDescriptionPage;

	public JUnitTestRunnerAgentWizard(Shell shell) {
		super(shell);
	}

	/**
	 * @see org.inqle.ui.rap.actions.DynaWizard#addPages()
	 */
	@Override
	public void addPages() {
		nameDescriptionPage = new NameDescriptionPage("Name and Description", null);
		addPage(nameDescriptionPage);
	}

	@Override
	public boolean performFinish() {
		JUnitTestRunnerAgent juAgent = (JUnitTestRunnerAgent)getAgent();
		juAgent.setName(nameDescriptionPage.getName());
		juAgent.setDescription(nameDescriptionPage.getDescription());
		Persister persister = Persister.getInstance();
		persister.persist(juAgent);
		return true;
	}

}
