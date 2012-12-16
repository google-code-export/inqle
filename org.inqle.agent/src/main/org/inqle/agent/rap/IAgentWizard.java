/**
 * 
 */
package org.inqle.agent.rap;

import org.eclipse.jface.wizard.IWizard;
import org.inqle.agent.IAgent;
import org.inqle.ui.rap.IPart;

/**
 * @author David Donohue
 * Feb 27, 2008
 */
public interface IAgentWizard extends IWizard {

	public static final String ID = "org.inqle.agent.IAgentWizard";
	public IPart getPart();
	public void setPart(IPart part);
	public void setAgent(IAgent agent);
	public IAgent getAgent();

	
//	public void setBean(IAgent bean);
//	public IAgent getBean();
}
