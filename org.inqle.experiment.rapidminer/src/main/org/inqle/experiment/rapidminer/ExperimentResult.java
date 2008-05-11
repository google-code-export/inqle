/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Collection;

import org.inqle.core.util.JavaHasher;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.UniqueJenabean;
import org.inqle.data.sampling.ISampler;

import thewebsemantic.Namespace;

import com.rapidminer.operator.performance.PerformanceVector;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
@Namespace(RDF.INQLE)
public class ExperimentResult extends GlobalJenabean {

	//private PerformanceVector performanceVector;
	//private LearningCycle learningCycle;
	//private Exception learningException;
	//private ISampler sampler;
	private Arc experimentLabelArc;
	private Collection<Arc> experimentAttributeArcs;
	private String experimentSubject;
	private IRapidMinerExperiment rapidMinerExperiment;
	private double correlation;
	private double root_mean_squared_error;
	
	public String toString() {
		String s = getClass().toString() + " {";
		s += super.toString();
		//s += "[learningCycle=" + learningCycle + "]\n";
		//s += "[sampler=" + sampler + "]\n";
		s += "[experimentSubject=" + experimentSubject + "]\n";
		s += "[experimentAttributeArcs=" + experimentAttributeArcs + "]\n";
		s += "[experimentLabelArc=" + experimentLabelArc + "]\n";
		s += "[rapidMinerExperiment=" + rapidMinerExperiment + "]\n";
		//s += "[learningException=" + learningException + "]\n";
		s += "[correlation=" + correlation + "]\n";
		s += "[root_mean_squared_error=" + root_mean_squared_error + "]\n";
		s += "}";
		return s;
	}
	
//	public PerformanceVector getPerformanceVector() {
//		return performanceVector;
//	}

	@Override
	public String getId() {
		String hash = JavaHasher.hashSha256(toString());
		return hash;
	}
	
	public void setPerformanceVector(PerformanceVector performanceVector) {
		//this.performanceVector = performanceVector;
		//set key fields
		this.correlation = performanceVector.getCriterion("correlation").getAverage();
		this.root_mean_squared_error = performanceVector.getCriterion("root_mean_squared_error").getAverage();
	}
	
	public void clone(ExperimentResult copyFieldsFrom) {
		setExperimentSubject(copyFieldsFrom.getExperimentSubject());
		setExperimentAttributeArcs(copyFieldsFrom.getExperimentAttributeArcs());
		setExperimentLabelArc(copyFieldsFrom.getExperimentLabelArc());
		setRapidMinerExperiment(copyFieldsFrom.getRapidMinerExperiment());
		setCorrelation(copyFieldsFrom.getCorrelation());
		setRoot_mean_squared_error(copyFieldsFrom.getRoot_mean_squared_error());
		super.clone(copyFieldsFrom);
	}
	
//	public void replicate(ExperimentResult objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//	}
	
	/* (non-Javadoc)
	 * @see org.inqle.data.rdf.jenabean.BasicJenabean#createClone()
	 */
	public ExperimentResult createClone() {
		ExperimentResult newExperimentResult = new ExperimentResult();
		newExperimentResult.clone(this);
		return newExperimentResult;
	}

	/* (non-Javadoc)
	 * @see org.inqle.data.rdf.jenabean.BasicJenabean#createReplica()
	 */
//	public ExperimentResult createReplica() {
//		ExperimentResult newExperimentResult = new ExperimentResult();
//		newExperimentResult.replicate(this);
//		return newExperimentResult;
//	}

//	public LearningCycle getLearningCycle() {
//		return learningCycle;
//	}
//
//	public void setLearningCycle(LearningCycle learningCycle) {
//		this.learningCycle = learningCycle;
//	}

//	public Exception getLearningException() {
//		return learningException;
//	}
//
//	public void setLearningException(Exception learningException) {
//		this.learningException = learningException;
//	}

//	public void setSampler(ISampler sampler) {
//		this.sampler = sampler;
//	}

	public void setExperimentLabelArc(Arc experimentLabelArc) {
		this.experimentLabelArc = experimentLabelArc;
	}

	public void setExperimentAttributeArcs(Collection<Arc> experimentAttributeArcs) {
		this.experimentAttributeArcs = experimentAttributeArcs;
	}

	public void setExperimentSubject(String experimentSubject) {
		this.experimentSubject = experimentSubject;
	}

	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment) {
		this.rapidMinerExperiment = rapidMinerExperiment;
	}

	public IRapidMinerExperiment getRapidMinerExperiment() {
		return rapidMinerExperiment;
	}

	public String getExperimentSubject() {
		return experimentSubject;
	}

	public Collection<Arc> getExperimentAttributeArcs() {
		return experimentAttributeArcs;
	}

	public Arc getExperimentLabelArc() {
		return experimentLabelArc;
	}

//	public ISampler getSampler() {
//		return sampler;
//	}

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
