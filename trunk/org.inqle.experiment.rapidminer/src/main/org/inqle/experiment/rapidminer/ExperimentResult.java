/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

import thewebsemantic.Namespace;

import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
@Namespace(RDF.INQLE)
public class ExperimentResult extends BasicJenabean {

	private PerformanceVector performanceVector;
	private LearningCycle learningCycle;
	private Exception learningException;
	
	public PerformanceVector getPerformanceVector() {
		return performanceVector;
	}

	public void setPerformanceVector(PerformanceVector performanceVector) {
		this.performanceVector = performanceVector;
	}
	
	public void clone(ExperimentResult copyFieldsFrom) {
		setLearningCycle(copyFieldsFrom.getLearningCycle());
		setPerformanceVector(copyFieldsFrom.getPerformanceVector());
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



}
