package org.inqle.agent.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.agent.AgentInfo;
import org.inqle.agent.IAgent;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.util.BeanTool;
import org.inqle.ui.rap.IPartType;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DeleteAgentAction extends Action {
	private String menuText;
	private IWorkbenchWindow window;
	//private Persister persister;
	private IAgent agentToDelete = null;
	private CustomizedAgentPart agentPart = null;
	
	private static final Logger log = Logger.getLogger(DeleteAgentAction.class);
	
	//public DeleteAgentAction(String menuText, CustomizedAgentPart agentPart, IAgent agentToDelete, IWorkbenchWindow window, Persister persister) {
		
	public DeleteAgentAction(String menuText, CustomizedAgentPart agentPart, IWorkbenchWindow window) {
		this.window = window;
		this.menuText = menuText;
		this.agentPart = agentPart;
//		this.agentToDelete  = (IAgent)agentPart.getAgentFactory().getBaseAgent().createReplica();
		this.agentToDelete  = BeanTool.replicate(agentPart.getAgentFactory().getBaseAgent());
		//this.persister = persister;
	}
	
	public String getText() {
		return menuText;
	}
	
	@Override
	public void run() {
		boolean confirmDelete = false;
		
		if (this.agentPart instanceof IPartType) {
			IPartType thisPartType = (IPartType)agentPart;
			if (thisPartType.hasChildren()) {
				MessageDialog.openWarning(window.getShell(), "Unable to delete", "Please remove all child objects before deleting this agent.");
				return;
			}
		}
		if (agentToDelete != null) {
			confirmDelete = MessageDialog.openConfirm(window.getShell(), "Delete this Agent", "Are you sure you want to delete agent\n'" + agentToDelete.getName() + "'?\nTHIS CANNOT BE UNDONE!");
		}
		if (confirmDelete) {
			Persister persister = Persister.getInstance();
			persister.remove(agentToDelete, AgentInfo.AGENT_DB);
			IPartType parentPart = agentPart.getParent();
			parentPart.fireUpdate(parentPart);
		}
	}
}
