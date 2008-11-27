/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

import com.rapidminer.operator.performance.PerformanceVector;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
@TargetDataset(ExperimentResult.EXPERIMENTS_DATASET)
@Namespace(RDF.INQLE)
public class ExperimentResult extends UniqueJenabean {

	public static final String EXPERIMENTS_DATASET = "org.inqle.datasets.experiments";
	
	private static Logger log = Logger.getLogger(ExperimentResult.class);
	
	public ExperimentResult() {
		setCreationDate(new Date());
		//log.info("Created ExperimentResult and set creationDate to " + getCreationDate());
	}
	
	@Override
	@Id
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}
	
	//private PerformanceVector performanceVector;
	//private LearningCycle learningCycle;
	//private Exception learningException;
	private String samplerClassName;
	private Arc experimentLabelArc;
	private Collection<Arc> experimentAttributeArcs;
	private Arc experimentSubjectArc;
	private String experimentSubjectClass;
	//private IRapidMinerExperiment rapidMinerExperiment;
	private String rapidMinerExperimentId;
	private double correlation;
	private double root_mean_squared_error;
	
	public String toString() {
		String s = getClass().toString() + " {";
		s += super.toString();
		//s += "[learningCycle=" + learningCycle + "]\n";
		s += "[samplerClassName=" + samplerClassName + "]\n";
		s += "[experimentSubject=" + experimentSubjectArc + "]\n";
		s += "[experimentAttributeArcs=" + experimentAttributeArcs + "]\n";
		s += "[experimentLabelArc=" + experimentLabelArc + "]\n";
		s += "[rapidMinerExperimentId=" + rapidMinerExperimentId + "]\n";
		//s += "[learningException=" + learningException + "]\n";
		s += "[correlation=" + correlation + "]\n";
		s += "[root_mean_squared_error=" + root_mean_squared_error + "]\n";
		s += "}";
		return s;
	}
	
//	public PerformanceVector getPerformanceVector() {
//		return performanceVector;
//	}
	
	public void setPerformanceVector(PerformanceVector performanceVector) {
		//this.performanceVector = performanceVector;
		//set key fields
		this.correlation = performanceVector.getCriterion("correlation").getAverage();
		this.root_mean_squared_error = performanceVector.getCriterion("root_mean_squared_error").getAverage();
	}
	
	public void clone(ExperimentResult copyFieldsFrom) {
		setExperimentSubjectArc(copyFieldsFrom.getExperimentSubjectArc());
		setExperimentAttributeArcs(copyFieldsFrom.getExperimentAttributeArcs());
		setExperimentLabelArc(copyFieldsFrom.getExperimentLabelArc());
		setRapidMinerExperimentId(copyFieldsFrom.getRapidMinerExperimentId());
		setCorrelation(copyFieldsFrom.getCorrelation());
		setRoot_mean_squared_error(copyFieldsFrom.getRoot_mean_squared_error());
		super.clone(copyFieldsFrom);
	}
	
	public void replicate(ExperimentResult objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}
	
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
	public ExperimentResult createReplica() {
		ExperimentResult newExperimentResult = new ExperimentResult();
		newExperimentResult.replicate(this);
		return newExperimentResult;
	}

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

	public void setExperimentSubjectArc(Arc experimentSubjectArc) {
		this.experimentSubjectArc = experimentSubjectArc;
	}

	public void setRapidMinerExperimentId(String rapidMinerExperimentId) {
		this.rapidMinerExperimentId = rapidMinerExperimentId;
	}

	public String getRapidMinerExperimentId() {
		return rapidMinerExperimentId;
	}

	public Arc getExperimentSubjectArc() {
		return experimentSubjectArc;
	}

	public Collection<Arc> getExperimentAttributeArcs() {
		return experimentAttributeArcs;
	}
	
	public String getExperimentAttributeQnameRepresentation() {
		if (experimentAttributeArcs==null) return null;
		String s = "";
		for (Arc attributeArc: experimentAttributeArcs) {
			s += attributeArc.getQnameRepresentation() + ";\n";
		}
		return s;
	}

	public void setExperimentAttributeQnameRepresentation() {
		//do nothing; this method ensure that the field will be stored by Jenabean
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

	public String getSamplerClassName() {
		return samplerClassName;
	}

	public void setSamplerClassName(String samplerClassName) {
		this.samplerClassName = samplerClassName;
	}

	public String getExperimentSubjectClass() {
		return experimentSubjectClass;
	}

	public void setExperimentSubjectClass(String experimentSubjectClass) {
		this.experimentSubjectClass = experimentSubjectClass;
	}



}
