package org.inqle.experiment.rapidminer.agent;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.inqle.agent.IAgent;
import org.inqle.agent.rap.AAgentFactory;
import org.inqle.agent.rap.IAgentFactory;
import org.inqle.agent.rap.IAgentWizard;
import org.inqle.data.rdf.jenabean.util.JenabeanWriter;
import org.inqle.rdf.beans.util.BeanTool;

import com.hp.hpl.jena.rdf.model.Model;

public class ExperimenterAgentFactory extends AAgentFactory {

	private static Logger log = Logger.getLogger(ExperimenterAgentFactory.class);

	public IAgentFactory cloneFactory(IAgent childAgent) {
		ExperimenterAgentFactory newFactory = new ExperimenterAgentFactory();
		newFactory.setStartingAgent(childAgent);
		//newFactory.setPersister(persister);
		newFactory.setName(getName());
		newFactory.setDescription(getDescription());
		return newFactory;
	}

	public IAgentWizard createWizard(Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(shell);
		return newWizard;
	}

	public IAgentWizard createWizardForReplica(Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(shell);
//		ExperimenterAgent replicaAgent = (ExperimenterAgent)getBaseAgent().createReplica();
		ExperimenterAgent replicaAgent = BeanTool.replicate((ExperimenterAgent)getStartingAgent());
//		log.info("ExperimenterAgentFactory created replicaAgent=\n" + JenabeanWriter.toString(replicaAgent));
		newWizard.setAgent(replicaAgent);
		return newWizard;
	}

	public IAgentWizard createWizardForClone(Shell shell) {
		ExperimenterAgentWizard newWizard = new ExperimenterAgentWizard(shell);
//		ExperimenterAgent cloneAgent = (ExperimenterAgent)getBaseAgent().createClone();
		ExperimenterAgent cloneAgent = BeanTool.clone((ExperimenterAgent)getStartingAgent());
		log.info("ExperimenterAgentFactory created replicaAgent=\n" + JenabeanWriter.toString(cloneAgent));
		newWizard.setAgent(cloneAgent);
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
