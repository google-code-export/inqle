/**
 * 
 */
package org.inqle.testing;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Apr 25, 2008
 */
public class JUnitTestRunnerAgentWizard extends AAgentWizard {

	public JUnitTestRunnerAgentWizard(Model saveToModel, Persister persister,
			Shell shell) {
		super(saveToModel, persister, shell);
	}

	/**
	 * @see org.inqle.ui.rap.actions.DynaWizard#addPages()
	 */
	@Override
	public void addPages() {
		// TODO Auto-generated method stub

	}

}
