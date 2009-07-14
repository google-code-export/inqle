/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

import com.rapidminer.operator.performance.PerformanceCriterion;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
@TargetDatamodel(ExperimentResult.EXPERIMENTS_DATASET)
@Namespace(RDF.INQLE)
public class ExperimentResult extends UniqueJenabean {

	public static final String EXPERIMENTS_DATASET = "org.inqle.datamodels.experiments";
	
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
	private double spearman_rho;
	private double kendall_tau;
	private double absolute_error;
	private double relative_error;
	private double relative_error_lenient;
	private double relative_error_strict;
	private double normalized_absolute_error;
	private double root_relative_squared_error;
	private double squared_error;
	private double squared_correlation;
	private double cross_entropy;
	private double margin;
	private double soft_margin_loss;
	private double logistic_loss;
	
	private double accuracy;
	private double classification_error;
	private double kappa;
	private double weighted_mean_precision;
	private double weighted_mean_recall;
  
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
		s += "[spearman_rho=" + spearman_rho + "]\n";
		s += "[kendall_tau=" + kendall_tau + "]\n";
		s += "[absolute_error=" + absolute_error + "]\n";
		s += "[relative_error=" + relative_error + "]\n";
		s += "[relative_error_lenient=" + relative_error_lenient + "]\n";
		s += "[relative_error_strict=" + relative_error_strict + "]\n";
		s += "[normalized_absolute_error=" + normalized_absolute_error + "]\n";
		s += "[root_relative_squared_error=" + root_relative_squared_error + "]\n";
		s += "[squared_error=" + squared_error + "]\n";
		s += "[squared_correlation=" + squared_correlation + "]\n";
		s += "[cross_entropy=" + cross_entropy + "]\n";
		s += "[margin=" + margin + "]\n";
		s += "[soft_margin_loss=" + soft_margin_loss + "]\n";
		s += "[logistic_loss=" + logistic_loss + "]\n";
		s += "[accuracy=" + accuracy + "]\n";
		s += "[classification_error=" + classification_error + "]\n";
		s += "[kappa=" + kappa + "]\n";
		s += "[weighted_mean_precision=" + weighted_mean_precision + "]\n";
		s += "[weighted_mean_recall=" + weighted_mean_recall + "]\n";
		
		s += "}";
		return s;
	}
	
//	public PerformanceVector getPerformanceVector() {
//		return performanceVector;
//	}
	
	/**
	 * this extracts all performance criteria from a RapidMiner PerformanceVector, if they are valid values
	 */
	public void setPerformanceVector(PerformanceVector performanceVector) {
		//this.performanceVector = performanceVector;
		//set key fields
		PerformanceCriterion criterion = performanceVector.getCriterion("correlation");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.correlation = dblVal;
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("root_mean_squared_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.root_mean_squared_error = criterion.getAverage();
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("spearman_rho");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setSpearman_rho(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("kendall_tau");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setKendall_tau(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("absolute_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setAbsolute_error(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("relative_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setRelative_error(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("relative_error_lenient");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setRelative_error_lenient(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("relative_error_strict");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setRelative_error_strict(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("normalized_absolute_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setNormalized_absolute_error(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("root_relative_squared_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setRoot_relative_squared_error(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("squared_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setSquared_error(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("squared_correlation");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setSquared_correlation(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("cross-entropy");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setCross_entropy(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("margin");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setMargin(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("soft_margin_loss");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setSoft_margin_loss(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("logistic_loss");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setLogistic_loss(dblVal);
		} catch (Exception e) { }
		
		criterion = performanceVector.getCriterion("accuracy");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setAccuracy(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("classification_error");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setClassification_error(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("kappa");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setKappa(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("weighted_mean_precision");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setWeighted_mean_precision(dblVal);
		} catch (Exception e) { }
		criterion = performanceVector.getCriterion("weighted_mean_recall");
		try {
			double dblVal = criterion.getAverage();
			if (!(Double.isNaN(dblVal)) && (!(Double.isInfinite(dblVal)))) this.setWeighted_mean_recall(dblVal);
		} catch (Exception e) { }
	}
	
	public void clone(ExperimentResult copyFieldsFrom) {
		setExperimentSubjectArc(copyFieldsFrom.getExperimentSubjectArc());
		setExperimentAttributeArcs(copyFieldsFrom.getExperimentAttributeArcs());
		setExperimentLabelArc(copyFieldsFrom.getExperimentLabelArc());
		setRapidMinerExperimentId(copyFieldsFrom.getRapidMinerExperimentId());
		setCorrelation(copyFieldsFrom.getCorrelation());
		setSpearman_rho(copyFieldsFrom.getSpearman_rho());
		setKendall_tau(copyFieldsFrom.getKendall_tau());
		setAbsolute_error(copyFieldsFrom.getAbsolute_error());
		setRelative_error(copyFieldsFrom.getRelative_error());
		setRelative_error_lenient(copyFieldsFrom.getRelative_error_lenient());
		setRelative_error_strict(copyFieldsFrom.getRelative_error_strict());
		setNormalized_absolute_error(copyFieldsFrom.getNormalized_absolute_error());
		setRoot_relative_squared_error(copyFieldsFrom.getRoot_relative_squared_error());
		setSquared_error(copyFieldsFrom.getSquared_error());
		setSquared_correlation(copyFieldsFrom.getSquared_correlation());
		setCross_entropy(copyFieldsFrom.getCross_entropy());
		setMargin(copyFieldsFrom.getMargin());
		setSoft_margin_loss(copyFieldsFrom.getSoft_margin_loss());
		setLogistic_loss(copyFieldsFrom.getLogistic_loss());
		
		setAccuracy(copyFieldsFrom.getAccuracy());
		setClassification_error(copyFieldsFrom.getClassification_error());
		setKappa(copyFieldsFrom.getKappa());
		setWeighted_mean_precision(copyFieldsFrom.getWeighted_mean_precision());
		setWeighted_mean_recall(copyFieldsFrom.getWeighted_mean_recall());
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
		int i=0;
		for (Arc attributeArc: experimentAttributeArcs) {
			if (i > 0) s += "; \n";
			s += attributeArc.getQnameRepresentation();
			i++;
		}
		return s;
	}

	public void setExperimentAttributeQnameRepresentation(String dummy) {
		//do nothing; this method ensures that the field will be stored by Jenabean
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

	public void setSpearman_rho(double spearman_rho) {
		this.spearman_rho = spearman_rho;
	}

	public double getSpearman_rho() {
		return spearman_rho;
	}

	public void setKendall_tau(double kendall_tau) {
		this.kendall_tau = kendall_tau;
	}

	public double getKendall_tau() {
		return kendall_tau;
	}

	public void setAbsolute_error(double absolute_error) {
		this.absolute_error = absolute_error;
	}

	public double getAbsolute_error() {
		return absolute_error;
	}

	public void setRelative_error(double relative_error) {
		this.relative_error = relative_error;
	}

	public double getRelative_error() {
		return relative_error;
	}

	public void setRelative_error_lenient(double relative_error_lenient) {
		this.relative_error_lenient = relative_error_lenient;
	}

	public double getRelative_error_lenient() {
		return relative_error_lenient;
	}

	public void setRelative_error_strict(double relative_error_strict) {
		this.relative_error_strict = relative_error_strict;
	}

	public double getRelative_error_strict() {
		return relative_error_strict;
	}

	public void setNormalized_absolute_error(double normalized_absolute_error) {
		this.normalized_absolute_error = normalized_absolute_error;
	}

	public double getNormalized_absolute_error() {
		return normalized_absolute_error;
	}

	public void setRoot_relative_squared_error(double root_relative_squared_error) {
		this.root_relative_squared_error = root_relative_squared_error;
	}

	public double getRoot_relative_squared_error() {
		return root_relative_squared_error;
	}

	public void setSquared_error(double squared_error) {
		this.squared_error = squared_error;
	}

	public double getSquared_error() {
		return squared_error;
	}

	public void setSquared_correlation(double squared_correlation) {
		this.squared_correlation = squared_correlation;
	}

	public double getSquared_correlation() {
		return squared_correlation;
	}

	public void setCross_entropy(double cross_entropy) {
		this.cross_entropy = cross_entropy;
	}

	public double getCross_entropy() {
		return cross_entropy;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public double getMargin() {
		return margin;
	}

	public void setSoft_margin_loss(double soft_margin_loss) {
		this.soft_margin_loss = soft_margin_loss;
	}

	public double getSoft_margin_loss() {
		return soft_margin_loss;
	}

	public void setLogistic_loss(double logistic_loss) {
		this.logistic_loss = logistic_loss;
	}

	public double getLogistic_loss() {
		return logistic_loss;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setClassification_error(double classification_error) {
		this.classification_error = classification_error;
	}

	public double getClassification_error() {
		return classification_error;
	}

	public void setKappa(double kappa) {
		this.kappa = kappa;
	}

	public double getKappa() {
		return kappa;
	}

	public void setWeighted_mean_precision(double weighted_mean_precision) {
		this.weighted_mean_precision = weighted_mean_precision;
	}

	public double getWeighted_mean_precision() {
		return weighted_mean_precision;
	}

	public void setWeighted_mean_recall(double weighted_mean_recall) {
		this.weighted_mean_recall = weighted_mean_recall;
	}

	public double getWeighted_mean_recall() {
		return weighted_mean_recall;
	}



}
