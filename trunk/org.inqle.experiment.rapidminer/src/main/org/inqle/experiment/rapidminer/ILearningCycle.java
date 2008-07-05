package org.inqle.experiment.rapidminer;

import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.ISampler;

/**
 * Note that the app requires use of the LearningCycle class directly,
 * so this interface should not be implemented by other classes.
 * 
 * @author David Donohue
 * Apr 18, 2008
 */
public interface ILearningCycle {

	/**
	 * the ID of the ILearningCycle plugin type
	 */
	String ID = "org.inqle.experiment.rapidminer.ILearningCycle";

	public static final String LEARNING_CYCLES_DATASET = "org.inqle.datasets.learningCycles";
	public ISampler getSampler();

	public void setSampler(ISampler sampler);
	
	public DataColumn getLabelDataColumn();
	
	public void setLabelDataColumn(DataColumn labelDataColumn);
	
	public IRapidMinerExperiment getRapidMinerExperiment();
	
	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment);
}
