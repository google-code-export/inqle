/**
 * 
 */
package org.inqle.agent.rap;

import org.eclipse.swt.widgets.Shell;
import org.inqle.ui.rap.actions.DynaWizard;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
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

	public AAgentWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
	}

}
