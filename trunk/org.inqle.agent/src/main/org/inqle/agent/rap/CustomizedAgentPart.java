/**
 * 
 */
package org.inqle.agent.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
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
//public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = super.getActions(workbenchWindow);
		
		if (!agentFactory.hasWizard()) {
			return actions;
		}
		
		//"Open this Agent" action.  This wizard works with a replica of the base agent
		//log.info("CustomizedAgentPart.addActions(): persister=" + persister);
		AgentWizardAction editAgentWizardAction = new AgentWizardAction(AgentWizardAction.MODE_OPEN, "Edit this agent...", this, workbenchWindow);
		actions.add(editAgentWizardAction);
		
		//Delete action
		//IAgent replicaOfAgent = agentFactory.replicateAgent();
		log.debug("Created replica of agent:" + JenabeanWriter.toString(agentFactory.getStartingAgent()));
		DeleteAgentAction deleteAgentAction = new DeleteAgentAction("Delete", this, workbenchWindow);
		actions.add(deleteAgentAction);
		
		return actions;
	}
	
}
