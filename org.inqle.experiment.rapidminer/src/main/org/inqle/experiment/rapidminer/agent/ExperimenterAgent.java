package org.inqle.experiment.rapidminer.agent;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AAgent;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
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
	
	public ExperimenterAgent createClone() {
		ExperimenterAgent newAgent = new ExperimenterAgent();
		newAgent.clone(this);
		return newAgent;
	}

	public ExperimenterAgent createReplica() {
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
		Persister persister = Persister.getInstance();
		setRunning();
		log.info("Starting to run()");
		LearningCycle learningCycleToRun = getLearningCycle();
		if (learningCycleToRun == null) {
			learningCycleToRun = selectRandomLearningCycle();
		}
		
		log.info("Running this LearningCycle: " + learningCycleToRun);
		cycleCount = 0;
		//run each test
		while (cycleCount != stoppingPoint && getMode() == RUNNING) {
			log.info("############### Running Cycle #" + cycleCount);
			cycleCount++;
			ExperimentResult experimentResult = learningCycleToRun.execute();
			if (experimentResult == null) {
				log.warn("Resulting ExperimentResult is null.  Skip to next cycle.");
				continue;
			}
			log.info("Storing experiment result: " + JenabeanWriter.toString(experimentResult));
			//log.info("Storing experiment result");
			persister.persist(experimentResult, persister.getMetarepositoryModel());
			cycleCount ++;
		}
		log.info("Exiting.  Completed " + cycleCount + " cycles.");
		setStopped();
		
	}

	private LearningCycle selectRandomLearningCycle() {
		List<LearningCycle> allLearningCycles = LearningCycleLister.listAllLearningCycles();
		int randomIndex = RandomListChooser.chooseRandomIndex(allLearningCycles.size());
		return allLearningCycles.get(randomIndex);
	}
}