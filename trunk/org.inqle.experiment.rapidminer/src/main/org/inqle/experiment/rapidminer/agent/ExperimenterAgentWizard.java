package org.inqle.experiment.rapidminer.agent;

import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.rap.AAgentWizard;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.NameDescriptionPage;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentWizard extends AAgentWizard {

	public ExperimenterAgentWizard(Model saveToModel, Persister persister,
			Shell shell) {
		super(saveToModel, persister, shell);
	}

	@Override
	/**
	 * TODO finish wizard
	 */
	public void addPages() {
		NameDescriptionPage nameDescriptionPage = new NameDescriptionPage(bean, "Name and Description", null);
		addPage(nameDescriptionPage);
	}

}
