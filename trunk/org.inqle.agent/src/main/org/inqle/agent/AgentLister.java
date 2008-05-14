package org.inqle.agent;

import java.util.ArrayList;
import java.util.List;

import org.inqle.agent.rap.IAgentFactory;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.data.rdf.jenabean.Persister;

public class AgentLister {

	@SuppressWarnings("unchecked")
	public static List<IAgent> listCustomAgents(IAgent baseAgent, Persister persister) {
		return (List<IAgent>) persister.reconstituteAll(baseAgent.getClass());
	}
	
	public static List<IAgent> listAgents(Persister persister) {
		List<IAgent> agents = new ArrayList<IAgent>();
		
		//first add the base plugins
		List<Object> objects =  ExtensionFactory.getExtensions(IAgentFactory.ID);
		for (Object object: objects) {
			if (object == null) continue;
			IAgentFactory agentFactory = (IAgentFactory)object;
			IAgent agent = agentFactory.newAgent();
			agents.add(agent);
			agents.addAll(listCustomAgents(agent, persister));
		}
		
		return agents;
	}
	
	public static List<IAgentFactory> listAgentFactories() {
		List<IAgentFactory> agentFactories = new ArrayList<IAgentFactory>();
		
		List<Object> objects =  ExtensionFactory.getExtensions(IAgentFactory.ID);
		for (Object object: objects) {
			if (object == null) continue;
			IAgentFactory agentFactory = (IAgentFactory)object;
			agentFactories.add(agentFactory);
		}
		
		return agentFactories;
	}
	
}
