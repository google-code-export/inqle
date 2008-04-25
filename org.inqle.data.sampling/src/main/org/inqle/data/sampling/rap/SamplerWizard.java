/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.DynaWizard;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Implementation of ISamplerWizard interface.  To make
 * a new wizard for your sampler, subclass
 * this abstract class and add required methods:
 * addPages()
 * @author David Donohue
 * Feb 28, 2008
 */
public abstract class SamplerWizard extends DynaWizard implements ISamplerWizard {

	public SamplerWizard(Model saveToModel, Persister persister, Shell shell) {
		super(saveToModel, persister, shell);
	}

}
