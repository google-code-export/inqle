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

	public void clone(AAgent objectToClone) {
		super.clone(objectToClone);
	}
}
