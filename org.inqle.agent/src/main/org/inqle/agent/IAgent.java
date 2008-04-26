package org.inqle.agent;

import org.inqle.data.rdf.jenabean.IBasicJenabean;

/**
 * All agent classes must implement this interface
 * An agent is a Jenabean object (persistable to RDF), 
 * which has behavior which can be customized.  Such customization is stored
 * in 1 or more RDF-persistable fields.
 * @author David Donohue
 * Apr 24, 2008
 */
public interface IAgent extends IBasicJenabean, Runnable {

	
	public static final int STOPPED = 0;
	public static final int STOPPING = 1;
	public static final int RUNNING = 2;
	
	public static final String ID = "org.inqle.agent.IAgent";

	public int getMode();
	public void stop();
	
}
