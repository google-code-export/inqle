package org.inqle.agent;

import java.util.ArrayList;
import java.util.List;

import org.inqle.agent.rap.IAgentFactory;
import org.inqle.core.extensions.util.ExtensionFactory;

public class AgentLister {

	public static List<IAgent> listAgents() {
		List<IAgent> agents = new ArrayList<IAgent>();
		
		//first add the base plugins
		List<Object> objects =  ExtensionFactory.getExtensions(IAgentFactory.ID);
		for (Object object: objects) {
			if (object == null) continue;
			IAgentFactory agentFactory = (IAgentFactory)object;
			agents.add(agentFactory.newAgent());
		}
		
		//next add customized agents
		//TODO
		
		return agents;
	}
}
