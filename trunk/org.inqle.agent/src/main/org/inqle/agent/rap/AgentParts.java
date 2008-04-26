package org.inqle.agent.rap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.PartType;
import org.inqle.core.extensions.util.ExtensionFactory;

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
	 * add to each child a reference to the Persister object,
	 * such that the entire tree may use common database connections
	 * @see org.inqle.ui.rap.IPartType#getChildren()
	 */
	public IPart[] getChildren() {
		List<Object> objects =  ExtensionFactory.getExtensions(IAgentFactory.ID);
		IPart[] nullIPartArr = new IPart[] {};
		if (objects == null) {
			return nullIPartArr;
		}
		List<IPart> parts = new ArrayList<IPart>();
		for (Object object: objects) {
			if (object == null) continue;
			IAgentFactory agentFactory = (IAgentFactory)object;
			AgentPart part = new AgentPart(agentFactory);
			part.setPersister(persister);
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
