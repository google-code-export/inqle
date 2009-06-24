package org.inqle.agent.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.agent.IAgent;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class RunAgentAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	private IAgent agentToRun = null;
	private AgentPart agentPart = null;
	
	private static final Logger log = Logger.getLogger(RunAgentAction.class);
	
	public RunAgentAction(String menuText, AgentPart agentPart, IWorkbenchWindow window) {
//		this.window = window;
		this.menuText = menuText;
		this.agentPart = agentPart;
		this.agentToRun  = agentPart.getAgentFactory().getBaseAgent();
		//this.persister = persister;
		assert(agentToRun != null);
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		log.info("Running agent: " + agentToRun);
		if (agentToRun != null) {
			new Thread(agentToRun).start();
			log.info("Started new thread running agent: " + agentToRun);
			agentPart.getParent().fireUpdatePart();
		}
	}
}
