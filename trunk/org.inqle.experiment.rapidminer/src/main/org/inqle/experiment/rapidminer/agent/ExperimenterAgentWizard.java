package org.inqle.experiment.rapidminer.agent;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentWizard extends AAgentWizard {

	public ExperimenterAgentWizard(Model saveToModel, Persister persister,
			Shell shell) {
		super(saveToModel, persister, shell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub

	}

}
