package org.inqle.experiment.rapidminer;

import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.jenabean.IGlobalJenabean;

public interface IRapidMinerExperiment extends IGlobalJenabean {

	public static final String ID = "org.inqle.experiment.rapidminer.IRapidMinerExperiment";
	public static final String REGRESSION_TYPE = "regression";
	public static final String CLASSIFICATION_TYPE = "classification";
	public String getExperimentXml();
	
	public void setExperimentXml(String experimentXml);
	
	public String getExperimentClassPath();
	
	public void setExperimentClassPath(String experimentClassPath);
	
	public String getExperimentType();
	
	public void setExperimentType(String type);
	
	public com.rapidminer.Process createProcess();
}
