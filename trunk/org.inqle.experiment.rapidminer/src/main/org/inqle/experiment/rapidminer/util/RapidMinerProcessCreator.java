package org.inqle.experiment.rapidminer.util;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.inqle.experiment.rapidminer.IRapidMinerExperiment;
import org.inqle.experiment.rapidminer.LearningCycle;

public class RapidMinerProcessCreator {

	private static Logger log = Logger.getLogger(RapidMinerProcessCreator.class);
	
	/**
	 * Apply the IDataTable of data to the RapidMiner experiment, using the labelDataColumn as label.
	 * @param dataTable
	 * @return
	 */
	public static com.rapidminer.Process createProcess(IRapidMinerExperiment experiment) {
		if (experiment.getExperimentXml() != null) {
			try {
				return new com.rapidminer.Process(experiment.getExperimentXml());
			} catch (Exception e) {
				log.error("Unable to create RapidMiner Process using this XML: " + experiment.getExperimentXml(), e);
				return null;
			}
		}
		
		if (experiment.getExperimentClassPath() != null) {
			InputStream in = experiment.getClass().getResourceAsStream(experiment.getExperimentClassPath());
			try {
				return new com.rapidminer.Process(in);
			} catch (Exception e) {
				log.error("Unable to create RapidMiner Process using this XML stored in this location: " + experiment.getExperimentClassPath(), e);
				return null;
			}
		}
		
		return null;
	}
}
