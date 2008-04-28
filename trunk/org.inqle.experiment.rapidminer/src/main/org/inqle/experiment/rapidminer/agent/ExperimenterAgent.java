package org.inqle.experiment.rapidminer.agent;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AAgent;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.experiment.rapidminer.ExperimentResult;
import org.inqle.experiment.rapidminer.LearningCycle;
import org.inqle.experiment.rapidminer.LearningCycleLister;

public class ExperimenterAgent extends AAgent {

	private LearningCycle learningCycle;
	private static Logger log = Logger.getLogger(ExperimenterAgent.class);
	
	public void clone(ExperimenterAgent objectToClone) {
		setLearningCycle(objectToClone.getLearningCycle());
		super.clone(objectToClone);
	}
	
	@Override
	public IBasicJenabean createClone() {
		ExperimenterAgent newAgent = new ExperimenterAgent();
		newAgent.clone(this);
		return newAgent;
	}

	@Override
	public IBasicJenabean createReplica() {
		ExperimenterAgent newAgent = new ExperimenterAgent();
		newAgent.replicate(this);
		return newAgent;
	}

	public LearningCycle getLearningCycle() {
		return learningCycle;
	}

	public void setLearningCycle(LearningCycle learningCycle) {
		this.learningCycle = learningCycle;
	}

	public void run() {
		mode = RUNNING;
		log.info("Starting to run()");
		LearningCycle learningCycleToRun = getLearningCycle();
		if (learningCycleToRun == null) {
			learningCycleToRun = selectRandomLearningCycle();
		}
		
		log.info("Running this LearningCycle: " + learningCycleToRun);
		cycleCount = 0;
		//run each test
		while (cycleCount != stoppingPoint && mode == RUNNING) {
			ExperimentResult experimentResult = learningCycleToRun.execute();
			persister.persist(experimentResult, persister.getMetarepositoryModel());
			cycleCount ++;
		}
		
	}

	private LearningCycle selectRandomLearningCycle() {
		List<LearningCycle> allLearningCycles = LearningCycleLister.listLearningCycles(persister);
		int randomIndex = RandomListChooser.chooseRandomIndex(allLearningCycles.size());
		return allLearningCycles.get(randomIndex);
	}
}