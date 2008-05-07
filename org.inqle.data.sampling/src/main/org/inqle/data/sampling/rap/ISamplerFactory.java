/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.actions.DynaWizard;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 27, 2008
 */
public interface ISamplerFactory extends INamedAndDescribed {

	public static final String ID = "org.inqle.data.sampling.rap.ISamplerFactory";
	
	/**
	 * Set the base sampler to the provided sampler
	 * @param sampler
	 */
	public void setBaseSampler(ISampler sampler);
	
	/**
	 * Return the base sampler.  If none exists, creates a new default sampler and returns it
	 * @return
	 */
	public ISampler getBaseSampler();
	
	/**
	 * Create a new sampler and return it
	 * @return
	 */
	public ISampler newSampler();
	
	/**
	 * Make an clone of the base Sampler, which is identical to the 
	 * base sampler except has unique ID.
	 * @return the new sampler
	 */
	public ISampler cloneSampler();
	
	/**
	 * Make an exact replica of the provided Sampler
	 * @return the new sampler
	 */
	//public ISampler replicateSampler();
	
	/**
	 * Create a wizard, without a sampler model bean yet set.
	 * @param model
	 * @param persister
	 * @param shell
	 * @return
	 */
	public ISamplerWizard createWizard(Model model, Persister persister, Shell shell);

	public String getName();

	public boolean hasWizard();

	/**
	 * Create a wizard, using a replica of the base sampler as the 
	 * model bean
	 * @param metarepositoryModel
	 * @param persister
	 * @param shell
	 * @return
	 */
	public ISamplerWizard createWizardForReplica(Model model,
			Persister persister, Shell shell);

	/**
	 * Creates a clone of the ISamplerFactory, using the current base sampler
	 * @return
	 */
	public ISamplerFactory cloneFactory();

	/**
	 * Creates a clone of the ISamplerFactory, using the provided ISampler
	 * @param childSampler
	 * @return
	 */
	public ISamplerFactory cloneFactory(ISampler childSampler);
	
}
