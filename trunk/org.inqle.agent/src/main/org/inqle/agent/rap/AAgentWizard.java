/**
 * 
 */
package org.inqle.agent.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.DynaWizard;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Implementation of IAgentWizard interface.  To make
 * a new wizard for your agent, subclass
 * this abstract class and add required methods:
 * addPages()
 * @author David Donohue
 * Feb 28, 2008
 */
public abstract class AAgentWizard extends DynaWizard implements IAgentWizard {

	protected IBasicJenabean bean;

	public AAgentWizard(Model saveToModel, Persister persister, Shell shell) {
		super(saveToModel, persister, shell);
	}

}
