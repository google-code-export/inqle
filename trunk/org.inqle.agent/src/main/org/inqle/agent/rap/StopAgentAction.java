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
public class StopAgentAction extends Action {
	private String menuText;
	private IAgent agentToStop = null;
	private AgentPart agentPart = null;
	
	private static final Logger log = Logger.getLogger(StopAgentAction.class);
	
	//public DeleteAgentAction(String menuText, CustomizedAgentPart agentPart, IAgent agentToStop, IWorkbenchWindow window, Persister persister) {
		
	public StopAgentAction(String menuText, AgentPart agentPart) {
		this.menuText = menuText;
		this.agentPart = agentPart;
		this.agentToStop  = (IAgent)agentPart.getAgentFactory().getBaseAgent();
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		if (agentToStop != null) {
			agentToStop.stop();
			agentPart.getParent().fireUpdatePart();
		}
	}
}
