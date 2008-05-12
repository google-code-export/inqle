package org.inqle.agent;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AgentRegistry {

	private static Logger log = Logger.getLogger(AgentRegistry.class);
	
	private List<String> runningAgents = new ArrayList<String>();
	private List<String> stoppingAgents = new ArrayList<String>();
	
 // Private constructor suppresses generation of a (public) default constructor
	private AgentRegistry() {}
 
 /**
	* AgentRegistryHolder is loaded on the first execution of AgentRegistry.getInstance() 
	* or the first access to AgentRegistryHolder.instance, not before.
	*/
	private static class AgentRegistryHolder { 
		private final static AgentRegistry instance = new AgentRegistry();
	}
	 
	public static AgentRegistry getInstance() {
		return AgentRegistryHolder.instance;
	}
	   
	public void registerRunning(IAgent runningAgent) {
		stoppingAgents.remove(runningAgent.getId());
		runningAgents.add(runningAgent.getId());
	}
	   
	public void registerStopping(IAgent stoppingAgent) {
		runningAgents.remove(stoppingAgent.getId());
		stoppingAgents.add(stoppingAgent.getId());
	}
	   
	public void registerStopped(IAgent stoppedAgent) {
		runningAgents.remove(stoppedAgent.getId());
		stoppingAgents.remove(stoppedAgent.getId());
	}
	
	/**
	 * Retrieve the status of the specified agent.
	 * Returns one of
	 * IAgent.RUNNING
	 * IAgent.STOPPING
	 * IAgent.STOPPED
	 * @param agent
	 * @return
	 */
	public int getMode(IAgent agent) {
		int mode = IAgent.STOPPED;
		if (runningAgents.contains(agent.getId())) {
			mode = IAgent.RUNNING;
		} else if (stoppingAgents.contains(agent.getId())) {
			mode = IAgent.STOPPING;
		}
		log.trace("getMode() for agent " + agent.getId() + "\nrunningAgents=" + runningAgents + "\nstoppingAgents=" + stoppingAgents + "\nMODE=" + mode);
		return mode;
	}

}
