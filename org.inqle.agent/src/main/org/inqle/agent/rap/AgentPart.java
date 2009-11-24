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
import org.inqle.agent.AgentLister;
import org.inqle.agent.IAgent;
import org.inqle.data.rdf.jenabean.util.BeanTool;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;

/**
 * @author David Donohue
 * Feb 26, 2008
 */
public class AgentPart extends PartType {
	
	//static int agentPartCount = 0;
	//int initCount = 0;

	protected IAgentFactory agentFactory;

	private static final String ICON_PATH_AGENT = "org/inqle/agent/images/agent.png";
	private static final String ICON_PATH_RUNNING = "org/inqle/agent/images/running.gif";
	private static final String ICON_PATH_STOPPED = "org/inqle/agent/images/stopped.png";
	private static final String ICON_PATH_STOPPING = "org/inqle/agent/images/stopping.gif";

	private List<CustomizedAgentPart> childParts = new ArrayList<CustomizedAgentPart>();
	
	static Logger log = Logger.getLogger(AgentPart.class);

	private boolean childrenIntialized = false;

	//private IAgent agent;
	
	public AgentPart(IAgentFactory agentFactory) {
		//agentPartCount++;
		this.agentFactory = agentFactory;
		//this.agent = agentFactory.getBaseAgent();
	}
	
	/**
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	@Override
	public String getName() {
		IAgent agent = agentFactory.getStartingAgent();
		if (agent == null || agent.getName() == null) {
			return agent.getClass().getName();
		}
		return agent.getName();
	}
	
	@Override
	public String getIconPath() {
		IAgent agent = agentFactory.getStartingAgent();
		if (agent.getMode() == IAgent.RUNNING) {
			return ICON_PATH_RUNNING;
		}
		if (agent.getMode() == IAgent.STOPPED) {
			return ICON_PATH_STOPPED;
		}
		if (agent.getMode() == IAgent.STOPPING) {
			return ICON_PATH_STOPPING;
		}
		return ICON_PATH_AGENT;
	}
	
	public IAgentFactory getAgentFactory() {
		return agentFactory;
	}
	
//	public static final String SPARQL_BEGIN = 
//		"PREFIX rdf: <" + RDF.RDF + ">\n" + 
//		"PREFIX ja: <" + RDF.JA + ">\n" + 
//		"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
//		"SELECT ?id \n" +
//		"{\n" +
//		"GRAPH ?g {\n" +
//		"?agentUri inqle:" + RDF.JENABEAN_ID_ATTRIBUTE + " ?id \n";
//	
//	public static final String SPARQL_END =
//		"\n} }\n";
		
//	private String getSparqlToFindChildren() {
//		IAgent agent = agentFactory.getBaseAgent();
//		String sparql = SPARQL_BEGIN +
//			" . ?agentUri a ?classUri\n" +
//			" . ?classUri <" + RDF.JAVA_CLASS + "> \"" + agent.getClass().getName() + "\" \n" +
//			SPARQL_END;
//		return sparql;
//	}
	
	@Override
	public IPart[] getChildren() {
		if (! this.childrenIntialized) {
			initChildren();
		}
		CustomizedAgentPart[] nullPart = {};
		if (childParts.size() == 0) {
			log.debug("No customizations found.");
			return nullPart;
		}
		return childParts.toArray(nullPart);
	}
	
	public void initChildren() {
		IAgent agent = agentFactory.getStartingAgent();
		
		//for each item in resultTable, add a ModelPart
		childParts = new ArrayList<CustomizedAgentPart>();

		for (IAgent childAgent: AgentLister.listCustomAgents(agent)) {
			IAgentFactory childAgentFactory = agentFactory.cloneFactory(childAgent);
			CustomizedAgentPart part = new CustomizedAgentPart(childAgentFactory);
			part.setParent(this);
			//part.setPersister(persister);
			part.addListener(listener);
			childParts.add(part);
		}
		this.childrenIntialized = true;
	}
	
	@Override
//public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		List<IAction> actions = new ArrayList<IAction>();
		IAgent agent = agentFactory.getStartingAgent();
		int mode = agent.getMode();
		if (mode == IAgent.STOPPED) {
			//"Run this agent" action
			log.trace("Adding 'Run this agent' for agentFactory=" + this.getAgentFactory());
			RunAgentAction runAgentAction = new RunAgentAction("Run this agent", this, workbenchWindow);
			///runAgentWizardAction.setAgent(replicaOfAgent);
			actions.add(runAgentAction);
		}
		
		if (mode == IAgent.RUNNING) {
			StopAgentAction stopAgentAction = new StopAgentAction("Stop this agent", this);
			///runAgentWizardAction.setAgent(replicaOfAgent);
			actions.add(stopAgentAction);
		}
		
		if (!agentFactory.hasWizard()) {
			return actions;
		}
		
		//Delete action
//		DeleteAgentAction deleteAgentAction = new DeleteAgentAction("Delete", this, workbenchWindow, this.persister);
//		actions.add(deleteAgentAction);
		
		//"Clone this Agent" action.  This wizard works with a clone of the base agent
//		IAgent cloneOfAgent = (IAgent)agentFactory.getBaseAgent().createClone();
		IAgent cloneOfAgent = BeanTool.clone(agentFactory.getStartingAgent());
		AgentWizardAction cloneAgentWizardAction = new AgentWizardAction(AgentWizardAction.MODE_CLONE, "Create customization of this agent", this, workbenchWindow);
		cloneAgentWizardAction.setAgent(cloneOfAgent); 
		actions.add(cloneAgentWizardAction);
		
		return actions;
	}

	@Override
	public Object getObject() {
		return agentFactory.getStartingAgent();
	}
}
