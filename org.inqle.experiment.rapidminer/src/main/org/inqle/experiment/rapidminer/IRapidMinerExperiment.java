package org.inqle.experiment.rapidminer;

import org.inqle.data.rdf.jenabean.IGlobalJenabean;
import org.inqle.data.sampling.IDataTable;

public interface IRapidMinerExperiment extends IGlobalJenabean {

	public static final String ID = "org.inqle.experiment.rapidminer.IRapidMinerExperiment";
	public static final String RAPID_MINER_EXPERIMENTS_DATAMODEL = "org.inqle.experiment.rapidminer.datamodels.rmExperiments";
	public String getExperimentXml();
	
	public void setExperimentXml(String experimentXml);
	
	public String getExperimentClassPath();
	
	public void setExperimentClassPath(String experimentClassPath);
	
	public String getExperimentType();
	
	public void setExperimentType(String type);
	
	/**
	 * Create the RapidMiner process object, which can be executed to perform the RM experiment.
	 * @return
	 */
	public com.rapidminer.Process createProcess();
	
	/**
	 * If this RM Experiment can handle the provided IDataTable, return true.  Otherwise return false
	 * @param dataTable
	 * @return
	 */
	public boolean handlesDataTable(IDataTable dataTable);
}
