/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.data.sampling.ISampler;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Abstract implementation of ISamplerFactory.  Methods do nothing
 * and return null
 * 
 * @author David Donohue
 * Apr 7, 2008
 */
public abstract class ASamplerFactory implements ISamplerFactory {

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#cloneSampler()
	 */
	public ISampler cloneSampler() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#createWizard(org.inqle.data.sampling.ISampler, com.hp.hpl.jena.rdf.model.Model, org.inqle.data.rdf.jenabean.Persister, org.eclipse.swt.widgets.Shell)
	 */
	public ISamplerWizard createWizard(Model model, Shell shell) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#getBaseSampler()
	 */
	public ISampler getStartingSampler() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#getName()
	 */
	public String getName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#hasWizard()
	 */
	public boolean hasWizard() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#newSampler()
	 */
	public ISampler newSampler() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#replicateSampler()
	 */
//	public ISampler replicateSampler() {
//		return null;
//	}

	/* (non-Javadoc)
	 * @see org.inqle.data.sampling.rap.ISamplerFactory#setBaseSampler(org.inqle.data.sampling.ISampler)
	 */
	public void setStartingSampler(ISampler sampler) {
	}
	
	/**
	 * Create a clone of this ISamplerFactory
	 * @return
	 */
	public ISamplerFactory cloneFactory() {
		return null;
	}
	
	public ISamplerFactory cloneFactory(ISampler childSampler) {
		return null;
	}

}
