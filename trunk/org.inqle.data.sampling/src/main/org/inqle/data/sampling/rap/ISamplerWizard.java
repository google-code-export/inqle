/**
 * 
 */
package org.inqle.data.sampling.rap;

import org.eclipse.jface.wizard.IWizard;
import org.inqle.ui.rap.IPart;

/**
 * @author David Donohue
 * Feb 27, 2008
 */
public interface ISamplerWizard extends IWizard {

	public static final String ID = "org.inqle.data.sampling.rap.ISamplerWizard";
	public IPart getPart();
	public void setPart(IPart part);
}
