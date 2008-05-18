package org.inqle.agent.rap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AgentLister;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.PartType;

/**
 * @author David Donohue
 * Feb 5, 2008
 */
public class AgentParts extends PartType {
	
	private Logger log = Logger.getLogger(AgentParts.class);
	
	private static final String ICON_PATH = "org/inqle/agent/images/agents.png";
	/* 
	 * 
	 * The children = the list of all IAgent plugins
	 * @see org.inqle.ui.rap.IPartType#getChildren()
	 */
	public IPart[] getChildren() {
		List<Object> objects =  ExtensionFactory.getExtensions(IAgentFactory.ID);
		IPart[] nullIPartArr = new IPart[] {};
		if (objects == null) {
			return nullIPartArr;
		}
		List<IPart> parts = new ArrayList<IPart>();
//		for (Object object: objects) {
//			if (object == null) continue;
//			IAgentFactory agentFactory = (IAgentFactory)object;
//			agentFactory.setPersister(persister);
//			AgentPart part = new AgentPart(agentFactory);
//			part.setParent(this);
//			part.setPersister(persister);
//			part.addListener(listener);
//			parts.add(part);
//		}
		for (IAgentFactory agentFactory: AgentLister.listAgentFactories()) {
			//agentFactory.setPersister(persister);
			AgentPart part = new AgentPart(agentFactory);
			part.setParent(this);
			//part.setPersister(persister);
			part.addListener(listener);
			parts.add(part);
		}
		IPart[] ipartArr = parts.toArray(nullIPartArr);
		
		return ipartArr;
	}

	/* (non-Javadoc)
	 * @see org.inqle.ui.rap.IPart#getName()
	 */
	public String getName() {
		return "Agents";
	}
	
	@Override
	public String getIconPath() {
		return ICON_PATH;
	}

}
