/**
 * 
 */
package org.inqle.agent.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.actions.DynaWizard;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Implementation of IAgentWizard interface.  To make
 * a new wizard for your agent, subclass
 * this abstract class and add required methods:
 * addPages()
 * @author David Donohue
 * Feb 28, 2008
 * 
 */
public abstract class AAgentWizard extends DynaWizard implements IAgentWizard {

	private IAgent agent;
	private IPart part;
	
	public IPart getPart() {
		return part;
	}

	public void setPart(IPart part) {
		this.part = part;
	}

	public AAgentWizard(Shell shell) {
		super(shell);
	}

	public void setAgent(IAgent agent) {
		this.agent = agent;
	}

	public IAgent getAgent() {
		return agent;
	}

}
