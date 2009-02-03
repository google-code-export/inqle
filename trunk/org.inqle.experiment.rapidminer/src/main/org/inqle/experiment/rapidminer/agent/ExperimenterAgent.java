package org.inqle.experiment.rapidminer.agent;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AAgent;
import org.inqle.agent.IAgent;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.experiment.rapidminer.ExperimentResult;
import org.inqle.experiment.rapidminer.LearningCycle;
import org.inqle.experiment.rapidminer.LearningCycleLister;
import org.inqle.data.rdf.RDF;

import thewebsemantic.Namespace;

@TargetDatamodel(IAgent.AGENT_DATASET)
@Namespace(RDF.INQLE)
public class ExperimenterAgent extends AAgent {

	public static final int USE_RANDOM_LEARNING_CYCLE = 0;
	public static final int USE_BASE_LEARNING_CYCLE = 1;
	public static final int USE_SELECTED_LEARNING_CYCLE = 2;
	
	private LearningCycle learningCycle;
	private static Logger log = Logger.getLogger(ExperimenterAgent.class);
	private int learningCycleMode = USE_RANDOM_LEARNING_CYCLE;
	
	public void clone(ExperimenterAgent objectToClone) {
		setLearningCycle(objectToClone.getLearningCycle());
		setLearningCycleMode(objectToClone.getLearningCycleMode());
		super.clone(objectToClone);
	}
	
	public void replicate(ExperimenterAgent objectToReplicate) {
		clone(objectToReplicate);
		setId(objectToReplicate.getId());
		super.replicate(objectToReplicate);
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

	public LearningCycle selectLearningCycle() {
		if (learningCycleMode == USE_RANDOM_LEARNING_CYCLE) {
			//log.info("Selecting random learning cycle...");
			return selectRandomLearningCycle();
		}
		if (learningCycleMode == USE_BASE_LEARNING_CYCLE) {
			//log.info("Selecting the base learning cycle...");
			return new LearningCycle();
		}
		if (learningCycle != null) {
			//otherwise, return the learnin//log.info(ield
			//log.info("Selecting pre-selected learning cycle...");
		} else {
			// the learning cycle field is null, so select a random learning cycle
			return selectRandomLearningCycle();
		}
		return learningCycle;
	}
	
	public void run() {
		long starttime = System.currentTimeMillis();
		Persister persister = Persister.getInstance();
		setRunning();
		//log.info("Starting to run()");
//		LearningCycle learningCycleToRun = getLearningCycle();
//		if (learningCycleToRun == null) {
//			learningCycleToRun = selectRandomLearningCycle();
//		}
		
		cycleCount = 0;
		//run each test until interrupted or (cycleCount >= stoppingPoint OR stopping point set to 0 or less [meaning cycle continuously])
		while ((stoppingPoint <= 0 || cycleCount < stoppingPoint) && getMode() == RUNNING) {
			long cycleStartTime = System.currentTimeMillis();
			cycleCount++;
			log.info("############### Running Cycle #" + (cycleCount) + " of " + stoppingPoint);
			
			//select the learning cycle
			LearningCycle learningCycleToRun = selectLearningCycle();
			log.info("Running this LearningCycle: " + learningCycleToRun);
			ExperimentResult experimentResult = learningCycleToRun.execute();
			if (experimentResult == null) {
				log.warn("Resulting ExperimentResult is null.  Skip to next cycle.");
				continue;
			}
//			log.trace("Storing experiment result: " + JenabeanWriter.toString(experimentResult));
			log.info("Storing experiment result");
			persister.persist(experimentResult);
			long cycleTime = (System.currentTimeMillis() - cycleStartTime) / 1000;
			log.info("Cycle # " + cycleCount + ": completed in " + cycleTime + " seconds.");
		}
		long stoptime = System.currentTimeMillis();
		long runseconds = (stoptime - starttime) / 1000;
		log.info("Exiting.  Completed " + cycleCount + " cycles, in " + runseconds + " seconds.");
		setStopped();
		
	}

	private LearningCycle selectRandomLearningCycle() {
		List<LearningCycle> allLearningCycles = LearningCycleLister.listAllLearningCycles();
		int randomIndex = RandomListChooser.chooseRandomIndex(allLearningCycles.size());
		return allLearningCycles.get(randomIndex);
	}

	public int getLearningCycleMode() {
		return learningCycleMode;
	}

	public void setLearningCycleMode(int learningCycleMode) {
		this.learningCycleMode = learningCycleMode;
	}
}