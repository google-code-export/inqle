/**
 * 
 */
package org.inqle.agent.rap;

import org.inqle.agent.IAgent;

/**
 * Abstract implementation of IAgentFactory.  Methods do nothing
 * and return null
 * 
 * @author David Donohue
 * Apr 7, 2008
 */
public abstract class AAgentFactory implements IAgentFactory {

	private String name;
	private String description;
	
	protected IAgent startingAgent;

	//protected Persister persister;

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.IAgentFactory#getBaseAgent()
	 */
	public IAgent getStartingAgent() {
		if (startingAgent == null) {
			startingAgent = newAgent();
			startingAgent.setBaseId();
		}
		return startingAgent;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.IAgentFactory#setBaseAgent(org.inqle.data.sampling.IAgent)
	 */
	public void setStartingAgent(IAgent baseAgent) {
		this.startingAgent = baseAgent;
	}

//	public Persister getPersister() {
//		return persister;
//	}
//
//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
