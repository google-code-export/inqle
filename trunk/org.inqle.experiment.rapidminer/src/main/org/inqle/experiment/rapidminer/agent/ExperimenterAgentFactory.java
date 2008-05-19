package org.inqle.experiment.rapidminer.agent;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentFactory;
import org.inqle.agent.rap.IAgentFactory;
import org.inqle.agent.rap.IAgentWizard;
import org.inqle.data.rdf.jenabean.JenabeanWriter;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentFactory extends AAgentFactory {

	private static Logger log = Logger.getLogger(ExperimenterAgentFactory.class);

	public IAgentFactory cloneFactory(IAgent childAgent) {
		ExperimenterAgentFactory newFactory = new ExperimenterAgentFactory();
		newFactory.setBaseAgent(childAgent);
		//newFactory.setPersister(persister);
		newFactory.setName(getName());
		newFactory.setDescription(getDescription());
		return newFactory;
	}

	public IAgentWizard createWizard(Model model, Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(model, shell);
		return newWizard;
	}

	public IAgentWizard createWizardForReplica(Model model,	Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(model, shell);
		ExperimenterAgent replicaAgent = (ExperimenterAgent)getBaseAgent().createReplica();
		log.info("ExperimenterAgentFactory created replicaAgent=\n" + JenabeanWriter.toString(replicaAgent));
		newWizard.setBean(replicaAgent);
		return newWizard;
	}

	public boolean hasWizard() {
		return true;
	}

	public IAgent newAgent() {
		ExperimenterAgent newAgent = new ExperimenterAgent();
		//newAgent.setPersister(persister);
		return newAgent;
	}

}
