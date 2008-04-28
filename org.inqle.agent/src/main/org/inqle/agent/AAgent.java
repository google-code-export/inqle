/**
 * 
 */
package org.inqle.agent;

import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;

/**
 * Abstract base class for creating agents.  Must implement run() method.
 * In this method, be sure to set mode to RUNNING at the outset.
 * In the course of cycling, increment the counter cycleCount each time, and test for if
 * cycleCount == stoppingPoint.  When it does, set mode to STOPPED and exit.
 * Also in the course of cycling, test to see if mode = STOPPING.  If so, set mode to STOPPED and exit.
 * 
 * @author David Donohue
 * Apr 24, 2008
 */
public abstract class AAgent extends BasicJenabean implements IAgent {

	public static final int CYCLE_CONTINUOUSLY = 0;
	protected int mode = STOPPED;
	protected int cycleCount = 0;
	/**
	 * the number of times to cycle.  0 = cycle continuously
	 */
	protected int stoppingPoint = CYCLE_CONTINUOUSLY;
	protected Persister persister;
	
	public void clone(AAgent objectToClone) {
		setNumberOfCycles(objectToClone.getNumberOfCycles());
		super.clone(objectToClone);
	}
	
	public void stop() {
		mode = STOPPING;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getNumberOfCycles() {
		return stoppingPoint;
	}

	public void setNumberOfCycles(int numberOfCycles) {
		this.stoppingPoint = numberOfCycles;
	}
	
	public Persister getPersister() {
		return persister;
	}

	public void setPersister(Persister persister) {
		this.persister = persister;
	}
}
