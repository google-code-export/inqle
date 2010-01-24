package org.inqle.agent;

import org.inqle.rdf.beans.IUniqueJenabean;

/**
 * All agent classes must implement this interface
 * An agent is a Jenabean object (persistable to RDF), 
 * which has behavior which can be customized.  Such customization is stored
 * in 1 or more RDF-persistable fields.
 * 
 * In your implementation, be sure to call setRunning, setStopping, and setStopped
 * when your agent enters these states.  This will permit the app to
 * know the state of your agent.
 * 
 * @author David Donohue
 * Apr 24, 2008
 */
public interface IAgent extends IUniqueJenabean, Runnable {

	public static final int STOPPED = 0;
	public static final int STOPPING = 1;
	public static final int RUNNING = 2;
	
	public static final String ID = "org.inqle.agent.IAgent";

	public int getMode();
	
	//register that this agent is running
	public void setRunning();
	
	//register that this agent is stopping
	public void setStopping();
	
//register that this agent is stopped
	public void setStopped();
	
}
