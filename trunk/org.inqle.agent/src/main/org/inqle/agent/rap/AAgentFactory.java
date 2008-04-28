/**
 * 
 */
package org.inqle.agent.rap;

import org.inqle.agent.IAgent;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;

/**
 * Abstract implementation of IAgentFactory.  Methods do nothing
 * and return null
 * 
 * @author David Donohue
 * Apr 7, 2008
 */
public abstract class AAgentFactory implements IAgentFactory {

	protected IAgent baseAgent;

	protected Persister persister;

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.IAgentFactory#getBaseAgent()
	 */
	public IAgent getBaseAgent() {
		if (baseAgent == null) {
			baseAgent = newAgent();
		}
		return baseAgent;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.IAgentFactory#setBaseAgent(org.inqle.data.sampling.IAgent)
	 */
	public void setBaseAgent(IAgent baseAgent) {
		this.baseAgent = baseAgent;
	}

	public Persister getPersister() {
		return persister;
	}

	public void setPersister(Persister persister) {
		this.persister = persister;
	}

}
