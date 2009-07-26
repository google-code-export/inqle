/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Collection;

import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.IUniqueJenabean;

/**
 * This interface specifies the results/output of any RapidMiner experiment.
 * @author David Donohue
 * Apr 22, 2008
 */
public interface IExperimentResult extends IUniqueJenabean {

	public static final String EXPERIMENT_RESULTS_DATAMODEL = "org.inqle.rapidminer.experiment.datamodels.rmExperimentResults";
	
	public void setExperimentLabelArc(Arc experimentLabelArc);

	public void setExperimentAttributeArcs(Collection<Arc> experimentAttributeArcs);

	public void setExperimentSubjectArc(Arc experimentSubjectArc);

	public void setRapidMinerExperimentId(String rapidMinerExperimentId);

	public String getRapidMinerExperimentId();

	public Arc getExperimentSubjectArc();

	public Collection<Arc> getExperimentAttributeArcs();
	
	public String getExperimentAttributeQnameRepresentation();

	/**
	 * Implementations should do nothing.  QName representation should be computed from the experimentAttributeArcs
	 * Having this method ensures that the field will be stored 
	 * @param dummy
	 */
	public void setExperimentAttributeQnameRepresentation(String dummy);
	
	public String getSamplerClassName();

	public void setSamplerClassName(String samplerClassName);
	
	public Arc getExperimentLabelArc();
	
}
