package org.inqle.experiment.rapidminer;

import org.inqle.data.rdf.jenabean.IBasicJenabean;

public interface IRapidMinerExperiment extends IBasicJenabean {

	public static final String ID = "org.inqle.experiment.rapidminer.IRapidMinerExperiment";
	public static final String FILE_ATTRIBUTE = "file";
	public static final String WEIGHT_ATTRIBUTE = "weight";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String REGRESSION_TYPE = "regression";
	public static final String CLASSIFICATION_TYPE = "classification";
	
	public String getExperimentXml();
	
	public void setExperimentXml(String experimentXml);
	
	public String getExperimentClassPath();
	
	public void setExperimentClassPath(String experimentClassPath);
	
	public String getExperimentType();
	
	public void setExperimentType(String type);
	
	public com.rapidminer.Process getProcess();
}
