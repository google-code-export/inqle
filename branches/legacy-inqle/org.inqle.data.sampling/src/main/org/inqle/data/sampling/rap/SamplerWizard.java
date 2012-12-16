/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.IPart;
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

	public static final String ID = "org.inqle.data.sampling.rap.ISamplerWizard";
	private ISampler sampler;
	
	private IPart part;
	
	public SamplerWizard(Shell shell) {
		super(shell);
	}
	
	public ISampler getSampler() {
		return sampler;
	}

	public void setSampler(ISampler sampler) {
		this.sampler = sampler;
	}
	
	public IPart getPart() {
		return part;
	}
	
	public void setPart(IPart part) {
		this.part = part;
	}

}
