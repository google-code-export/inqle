/**
 * 
 */
package org.inqle.agent.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.ui.rap.actions.DynaWizard;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 27, 2008
 */
public interface IAgentFactory extends INamedAndDescribed {

	public static final String ID = "org.inqle.agent.rap.IAgentFactory";
	
	/**
	 * Set the base agent to the provided agent
	 * @param agent
	 */
	public void setBaseAgent(IAgent agent);
	
	/**
	 * Return the base agent.  If none exists, creates a new default agent and returns it
	 * @return
	 */
	public IAgent getBaseAgent();
	
	/**
	 * Create a new agent and return it
	 * @return
	 */
	public IAgent newAgent();
	
	/**
	 * Create a wizard, without a agent model bean yet set.
	 * @param model
	 * @param shell
	 * @return
	 */
	public IAgentWizard createWizard(Model model, Shell shell);

	public boolean hasWizard();

	/**
	 * Create a wizard, using a replica of the base agent as the 
	 * model bean
	 * @param model
	 * @param shell
	 * @return
	 */
	public IAgentWizard createWizardForReplica(Model model, Shell shell);

	public IAgentFactory cloneFactory(IAgent childAgent);

//	public Persister getPersister();
//	
//	public void setPersister(Persister persister);
	
}
