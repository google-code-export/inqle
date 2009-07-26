package org.inqle.experiment.rapidminer.agent;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.agent.AAgent;
import org.inqle.agent.IAgent;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.ISampler;
import org.inqle.experiment.rapidminer.ExperimentResult;
import org.inqle.experiment.rapidminer.IRapidMinerExperiment;
import org.inqle.experiment.rapidminer.LearningCycle;

import thewebsemantic.Namespace;

@TargetDatamodel(IAgent.AGENT_DATASET)
@Namespace(RDF.INQLE)
public class ExperimenterAgent extends AAgent {

	public static final int USE_RANDOM_SAMPLER = 0;
	public static final int USE_BASE_SAMPLER = 1;
	public static final int USE_SELECTED_SAMPLER = 2;
	
	private ISampler sampler;
	public ISampler getSampler() {
		return sampler;
	}

	public void setSampler(ISampler sampler) {
		this.sampler = sampler;
	}

	private IRapidMinerExperiment rapidMinerExperiment;
	private static Logger log = Logger.getLogger(ExperimenterAgent.class);
	private int samplerMode = USE_RANDOM_SAMPLER;
	
	public int getSamplerMode() {
		return samplerMode;
	}

	public void setSamplerMode(int samplerMode) {
		this.samplerMode = samplerMode;
	}


	
	public void clone(ExperimenterAgent objectToClone) {
		setSamplerMode(objectToClone.getSamplerMode());
		setSampler(objectToClone.getSampler());
		setRapidMinerExperiment(objectToClone.getRapidMinerExperiment());
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

	public ISampler selectSampler() {
		if (samplerMode == USE_RANDOM_SAMPLER || sampler == null) {
			//log.info("Selecting random learning cycle...");
			return selectRandomSampler();
		}
		return sampler;
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
			LearningCycle learningCycleToRun = new LearningCycle();
			log.info("Running this LearningCycle: " + learningCycleToRun);
			ExperimentResult experimentResult = learningCycleToRun.execute();
			if (experimentResult == null) {
				log.warn("Resulting ExperimentResult is null.  Skip to next cycle.");
				continue;
			}
//			log.trace("Storing experiment result: " + JenabeanWriter.toString(experimentResult));
			log.info("Storing experiment result");
			persister.persist(experimentResult);
			double cycleTime = (System.currentTimeMillis() - cycleStartTime) / 1000;
			log.info("Cycle # " + cycleCount + ": completed in " + cycleTime + " seconds.");
			
			if (learningCycleToRun.isReadyToStopCycling()) break;
		}
		long stoptime = System.currentTimeMillis();
		double runseconds = (stoptime - starttime) / 1000;
		log.info("Exiting.  Completed " + cycleCount + " cycles, in " + runseconds + " seconds." +
				"  Average time per cycle is " + (runseconds / cycleCount) + " seconds.");
		setStopped();
		
	}

	public int getLearningCycleMode() {
		return learningCycleMode;
	}

	public void setLearningCycleMode(int learningCycleMode) {
		this.learningCycleMode = learningCycleMode;
	}

	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment) {
		this.rapidMinerExperiment = rapidMinerExperiment;
	}

	public IRapidMinerExperiment getRapidMinerExperiment() {
		return rapidMinerExperiment;
	}
}