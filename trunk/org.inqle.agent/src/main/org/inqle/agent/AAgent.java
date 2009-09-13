/**
 * 
 */
package org.inqle.agent;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

/**
 * Abstract base class for creating agents.  New agent classes
 * must implement run() method.  
 * 
 * In this method, be sure to set mode to RUNNING at the outset.
 * In the course of cycling, increment the counter cycleCount each time, and test for if
 * cycleCount == stoppingPoint.  When it does, set mode to STOPPED and exit.
 * Also in the course of cycling, test to see if mode = STOPPING.  If so, set mode to STOPPED and exit.
 * 
 * The run() method should not modify any field values.  Instead
 * work with local variables.  This way, the agent will be returned to the 
 * exact same state after running as it had before running.  
 * @author David Donohue
 * Apr 24, 2008
 */
public abstract class AAgent extends UniqueJenabean implements IAgent {

	public static final int CYCLE_CONTINUOUSLY = -1;
	//protected int mode = STOPPED;
	protected int cycleCount = 0;
	
	private static Logger log = Logger.getLogger(AAgent.class);
	
	/**
	 * the number of times to cycle.  0 = cycle continuously
	 */
	protected int stoppingPoint = CYCLE_CONTINUOUSLY;
	//protected Persister persister;
	
//	public void clone(AAgent objectToClone) {
//		setStoppingPoint(objectToClone.getStoppingPoint());
//		super.clone(objectToClone);
//	}
//	
//	public void replicate(AAgent objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//		super.replicate(objectToClone);
//	}
	
	public void setRunning() {
		AgentRegistry registry = AgentRegistry.getInstance();
		registry.registerRunning(this);
	}
	
	public void setStopped() {
		AgentRegistry registry = AgentRegistry.getInstance();
		registry.registerStopped(this);
	}
	
	public void setStopping() {
		AgentRegistry registry = AgentRegistry.getInstance();
		registry.registerStopping(this);
	}

	public int getMode() {
		//return mode;
		AgentRegistry registry = AgentRegistry.getInstance();
		return registry.getMode(this);
	}

//	public void setMode(int mode) {
//		this.mode = mode;
//	}

	public int getStoppingPoint() {
		return stoppingPoint;
	}

	public void setStoppingPoint(int numberOfCycles) {
		this.stoppingPoint = numberOfCycles;
	}
	
	@RdfProperty(RDF.NAME_PREDICATE)
	public String getName() {
		if (name == null) {
			int lastDot = this.getClass().getName().lastIndexOf ('.') + 1;
	    if (lastDot > 0) {
	      return this.getClass().getName().substring (lastDot);
	    }
	    return this.getClass().getName();
		}
		return name;
	}
	
//	public Persister getPersister() {
//		return persister;
//	}
//
//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}
}
