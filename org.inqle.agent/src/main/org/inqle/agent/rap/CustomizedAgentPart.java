/**
 * 
 */
package org.inqle.agent.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.ui.rap.IPart;

/**
 * This class represents a customization
 * @author David Donohue
 * Feb 28, 2008
 */
public class CustomizedAgentPart extends AgentPart {

	public CustomizedAgentPart(IAgentFactory agentFactory) {
		super(agentFactory);
	}
	
	static Logger log = Logger.getLogger(CustomizedAgentPart.class);

	@Override
	public IPart[] getChildren() {
		CustomizedAgentPart[] nullPart = {};
		return nullPart;
	}
	
	@Override
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
		super.addActions(manager, workbenchWindow);
		
		if (!agentFactory.hasWizard()) {
			return;
		}
		
		//"Open this Agent" action.  This wizard works with a replica of the base agent
		//log.info("CustomizedAgentPart.addActions(): persister=" + persister);
		AgentWizardAction editAgentWizardAction = new AgentWizardAction(AgentWizardAction.MODE_OPEN, "Edit this agent...", this, workbenchWindow);
		manager.add(editAgentWizardAction);
		
		//Delete action
		//IAgent replicaOfAgent = agentFactory.replicateAgent();
		log.debug("Created replica of agent:" + JenabeanWriter.toString(agentFactory.getBaseAgent()));
		DeleteAgentAction deleteAgentAction = new DeleteAgentAction("Delete", this, workbenchWindow);
		manager.add(deleteAgentAction);
	}
	
}
