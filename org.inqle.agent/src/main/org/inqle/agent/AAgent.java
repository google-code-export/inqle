/**
 * 
 */
package org.inqle.agent;

import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

/**
 * @author David Donohue
 * Apr 24, 2008
 */
public abstract class AAgent extends BasicJenabean implements IAgent {

	protected int mode = STOPPED;
	
	public void clone(AAgent objectToClone) {
		super.clone(objectToClone);
	}
	
	public void run() {
		mode = RUNNING;
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
}
