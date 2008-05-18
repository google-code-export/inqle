package org.inqle.agent.rap;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.agent.IAgent;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.DynaWizardDialog;

public class AgentWizardAction extends Action {

	public static final int MODE_RUN = 1;
	public static final int MODE_OPEN = 2;
	public static final int MODE_CLONE = 3;
	private AgentPart agentPart;
	//private Persister persister;
	private IWorkbenchWindow window;
	private String menuText;
	private int mode;
	private IAgentFactory agentFactory;
	private IAgent agent = null;

	private static Logger log = Logger.getLogger(AgentWizardAction.class);
	
	public AgentWizardAction(int mode, String menuText, AgentPart agentPart, IWorkbenchWindow window) {
		// TODO Auto-generated constructor stub
		this.mode = mode;
		this.menuText = menuText;
		this.agentPart = agentPart;
		this.agentFactory = agentPart.getAgentFactory();
		this.window = window;
		//this.persister = persister;
	}
	

	public IAgent getAgent() {
		return agent;
	}

	public void setAgent(IAgent agent) {
		this.agent = agent;
	}
	
	@Override
	public String getText() {
		return menuText;
	}
	
	@Override
	public void runWithEvent(Event event) {
		Persister persister = Persister.getInstance();
		//SimpleSparqlAgent testAgent = new SimpleSparqlAgent();
		//log.info(JenabeanWriter.toString(testAgent));
		//MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Opening new Wizard", event.toString());
		IAgentWizard wizard = null;
		if (mode == MODE_RUN) {
			//wizard = new AgentRunnerWizard(agent, window.getShell());
		} else if (mode == MODE_OPEN) {
			//log.info("Creating wizard...");
			wizard = agentFactory.createWizardForReplica(persister.getMetarepositoryModel(), window.getShell());
			wizard.setPart(agentPart);
			//log.info("Creating dialog...");
			DynaWizardDialog dialog = new DynaWizardDialog(window.getShell(), wizard);
			//log.info("Opening dialog...");
			dialog.open();
		} else if (mode == MODE_CLONE) {
			agent.setName("Clone of " + agent.getName());
			persister.persist(agent, persister.getMetarepositoryModel());
			agentPart.fireUpdatePart();
		}
	}


}
