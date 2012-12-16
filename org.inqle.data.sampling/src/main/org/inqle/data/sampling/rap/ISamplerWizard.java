/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.eclipse.jface.wizard.IWizard;
import org.inqle.data.sampling.ISampler;
import org.inqle.ui.rap.IPart;

/**
 * @author David Donohue
 * Feb 27, 2008
 */
public interface ISamplerWizard extends IWizard {
	public void setPart(IPart part);
	public void setSampler(ISampler sampler);
}
