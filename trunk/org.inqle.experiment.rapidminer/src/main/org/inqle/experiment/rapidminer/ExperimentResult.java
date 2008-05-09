/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Collection;
import java.util.List;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.ISampler;

import thewebsemantic.Namespace;

import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
@Namespace(RDF.INQLE)
public class ExperimentResult extends BasicJenabean {

	//private PerformanceVector performanceVector;
	private LearningCycle learningCycle;
	private Exception learningException;
	private ISampler sampler;
	private DataColumn experimentLabel;
	private Collection<DataColumn> experimentAttributes;
	private DataColumn experimentSubject;
	private IRapidMinerExperiment rapidMinerExperiment;
	private double correlation;
	private double root_mean_squared_error;
	
//	public PerformanceVector getPerformanceVector() {
//		return performanceVector;
//	}

	public void setPerformanceVector(PerformanceVector performanceVector) {
		//this.performanceVector = performanceVector;
		//set key fields
		this.correlation = performanceVector.getCriterion("correlation").getAverage();
		this.root_mean_squared_error = performanceVector.getCriterion("correlation").getAverage();
	}
	
	public void clone(ExperimentResult copyFieldsFrom) {
		setLearningCycle(copyFieldsFrom.getLearningCycle());
		//setPerformanceVector(copyFieldsFrom.getPerformanceVector());
		super.clone(copyFieldsFrom);
	}
	
	public void replicate(ExperimentResult objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
	}
	
	/* (non-Javadoc)
	 * @see org.inqle.data.rdf.jenabean.BasicJenabean#createClone()
	 */
	@Override
	public IBasicJenabean createClone() {
		ExperimentResult newExperimentResult = new ExperimentResult();
		newExperimentResult.clone(this);
		return newExperimentResult;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.rdf.jenabean.BasicJenabean#createReplica()
	 */
	@Override
	public IBasicJenabean createReplica() {
		ExperimentResult newExperimentResult = new ExperimentResult();
		newExperimentResult.replicate(this);
		return newExperimentResult;
	}

	public LearningCycle getLearningCycle() {
		return learningCycle;
	}

	public void setLearningCycle(LearningCycle learningCycle) {
		this.learningCycle = learningCycle;
	}

	public Exception getException() {
		return learningException;
	}

	public void setException(Exception learningException) {
		this.learningException = learningException;
	}

	public void setSampler(ISampler sampler) {
		this.sampler = sampler;
	}

	public void setExperimentLabel(DataColumn experimentLabel) {
		this.experimentLabel = experimentLabel;
	}

	public void setExperimentAttributes(Collection<DataColumn> experimentAttributes) {
		this.experimentAttributes = experimentAttributes;
	}

	public void setExperimentSubject(DataColumn experimentSubject) {
		this.experimentSubject = experimentSubject;
	}

	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment) {
		this.rapidMinerExperiment = rapidMinerExperiment;
	}

	public IRapidMinerExperiment getRapidMinerExperiment() {
		return rapidMinerExperiment;
	}

	public DataColumn getExperimentSubject() {
		return experimentSubject;
	}

	public Collection<DataColumn> getExperimentAttributes() {
		return experimentAttributes;
	}

	public DataColumn getExperimentLabel() {
		return experimentLabel;
	}

	public ISampler getSampler() {
		return sampler;
	}

	public double getCorrelation() {
		return correlation;
	}

	public void setCorrelation(double correlation) {
		this.correlation = correlation;
	}

	public double getRoot_mean_squared_error() {
		return root_mean_squared_error;
	}

	public void setRoot_mean_squared_error(double root_mean_squared_error) {
		this.root_mean_squared_error = root_mean_squared_error;
	}



}
