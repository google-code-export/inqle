package org.inqle.agent.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.agent.IAgent;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.ModelPart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class RunAgentAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	private Persister persister;
	private IAgent agentToRun = null;
	private AgentPart agentPart = null;
	
	private static final Logger log = Logger.getLogger(RunAgentAction.class);
	
	//public DeleteAgentAction(String menuText, CustomizedAgentPart agentPart, IAgent agentToRun, IWorkbenchWindow window, Persister persister) {
		
	public RunAgentAction(String menuText, AgentPart agentPart, IWorkbenchWindow window, Persister persister) {
		this.window = window;
		this.menuText = menuText;
		this.agentPart = agentPart;
		this.agentToRun  = agentPart.getAgentFactory().getBaseAgent();
		this.persister = persister;
		assert(agentToRun != null);
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		log.info("Running agent: " + agentToRun);
		if (agentToRun != null) {
			new Thread(agentToRun).start();
			log.info("Started new thread running agent: " + agentToRun);
			agentPart.getParent().fireUpdatePart();
		}
	}
}
