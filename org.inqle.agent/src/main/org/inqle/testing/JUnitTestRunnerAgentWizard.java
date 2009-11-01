/**
 * 
 */
package org.inqle.testing;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.AgentInfo;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.agent.rap.AgentWizardAction;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;
import org.inqle.ui.rap.pages.NameDescriptionPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Apr 25, 2008
 */
@TargetDatamodelName(AgentInfo.AGENT_DATASET)
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
		String datamodelId = Persister.getTargetDatamodelId(JUnitTestRunnerAgent.class, AgentInfo.AGENT_DB);
		persister.persist(juAgent, datamodelId);
		return true;
	}

}
