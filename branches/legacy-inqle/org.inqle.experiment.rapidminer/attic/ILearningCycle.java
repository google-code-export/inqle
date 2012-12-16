package org.inqle.experiment.rapidminer;

import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.sampling.ISampler;

/**
 * Note that the app requires use of the LearningCycle class directly,
 * so this interface should not be implemented by other classes.
 * 
 * @author David Donohue
 * Apr 18, 2008
 */
public interface ILearningCycle extends IBasicJenabean{

	/**
	 * the ID of the ILearningCycle plugin type
	 */
	String ID = "org.inqle.experiment.rapidminer.ILearningCycle";

	public static final String LEARNING_CYCLES_DATASET = "org.inqle.datamodels.learningCycles";
	public ISampler getSampler();

	public void setSampler(ISampler sampler);
	
//	public IDataColumn getLabelDataColumn();
//	public int getLabelDataColumnIndex();
	
//	public void setLabelDataColumn(IDataColumn labelDataColumn);
//	public void setLabelDataColumnIndex(int labelDataColumnIndex);
	
	public IRapidMinerExperiment getRapidMinerExperiment();
	
	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment);

	public ExperimentResult execute();

	public boolean isReadyToStopCycling();
}
