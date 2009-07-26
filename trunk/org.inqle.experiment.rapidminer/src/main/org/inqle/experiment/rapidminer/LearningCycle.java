/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.UniqueJenabean;
import org.inqle.data.sampling.IDataTable;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;

import thewebsemantic.Namespace;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * Executes a cycle of learning.  This cycle consists of:
 * (1) Find a sampler to use.  A LearningCycle object may
 * be configured to use a particular ISampler object.  If not then if
 * a single customized sampler exists, it will select that one.  Otherwise
 * it will randomly select one.  State information is preserved from
 * 1 cycle to the next, unless a random selection of sampler is made, switching samplers.
 * In the latter case, state info is lost.
 * (2) Run this sampler.
 * (3) Select a RapidMiner experiment to use.
 * (4) Apply the sampled data to the selected RapidMiner experiment
 * (4) Store the resulting ExperimentResult.
 * 
 * @author David Donohue
 * Apr 16, 2008
 */
public class LearningCycle {
//	public static final int USE_RANDOM_SAMPLER = 0;
//	public static final int USE_SELECTED_SAMPLER = 1;
	
	private ISampler sampler;
//	private int samplerMode = USE_RANDOM_SAMPLER;
//	private int labelDataColumnIndex;
	private IRapidMinerExperiment rapidMinerExperiment;
	//private Persister persister;

	private ISampler lastSampler;
	private transient boolean readyToStopCycling = false;
	
	private static Logger log = Logger.getLogger(LearningCycle.class);
	
//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}
//	
	public ISampler getSampler() {
		return sampler;
	}

	public void setSampler(ISampler sampler) {
//		this.sampler = sampler.createClone();
		this.sampler = sampler;
	}
	
//	public int getLabelDataColumnIndex() {
//		return labelDataColumnIndex;
//	}
//	
//	public void setLabelDataColumnIndex(int labelDataColumnIndex) {
//		this.labelDataColumnIndex = labelDataColumnIndex;
//	}

	public IRapidMinerExperiment getRapidMinerExperiment() {
		return rapidMinerExperiment;
	}

	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment) {
		this.rapidMinerExperiment = rapidMinerExperiment;
	}
	
	/**
	 * Execute the Learning Cycle.  
	 * Use the provided sampler to create a IDataTable of data for learning.  
	 * Next, ensure that a label has been selected (or randomly choose one).
	 * Next, ensure that a proper RapidMiner experiment has been selected
	 * (or randomly choose one).
	 * Next, convert the IDataTable into a RapidMiner ExampleSet.
	 * Finally, run the ExampleSet through the experiment.
	 */
	public IExperimentResult execute() {
		ISampler samplerToUse = selectSampler(lastSampler);
		if (samplerToUse == null) {
			log.warn("Unable to retrieve any sampler.");
			return null;
		}
		IDataTable resultDataTable = samplerToUse.execute();
		log.info("Got resultDataTable=\n" + resultDataTable);
		if (resultDataTable == null) {
			log.warn("Sampler " + samplerToUse + " of class " + samplerToUse.getClass() + " was unable" +
					" to retrieve a DataTable of results.");
			return null;
		}
		
		IRapidMinerExperiment experimentToUse = selectRapidMinerExperiment(resultDataTable);
		if (experimentToUse == null) {
			log.warn("No RapidMiner experiment was found to match the IDataTable");
			return null;
		}
		IExperimentResult experimentResult = experimentToUse.runExperiment(resultDataTable);
		
		//add metadata to experimentResult
//		IDataColumn idColumn = resultDataTable.getColumn(resultDataTable.getIdColumnIndex());
		
		experimentResult.setSamplerClassName(samplerToUse.getClass().getName());
		if (resultDataTable.getIdColumnIndex() >= 0) {
			experimentResult.setExperimentSubjectArc(resultDataTable.getColumn(resultDataTable.getIdColumnIndex()));
		}
		experimentResult.setExperimentLabelArc(resultDataTable.getColumn(resultDataTable.getLabelColumnIndex()));
		experimentResult.setRapidMinerExperimentId(experimentToUse.getId());
		List<Arc> learnableArcs = resultDataTable.getLearnableColumns();
		experimentResult.setExperimentAttributeArcs(learnableArcs);
		
		this.readyToStopCycling = samplerToUse.isFinishedSamplingStrategy();
		//experimentResult.setExperimentSubject(resultDataTable.getColumn(resultDataTable.getIdColumnIndex()).getColumnUri());
//		log.trace("&&&&&&&&&&&&&&& idColumn.getArc()=" + idColumn.getArc());
		//log.info("resultDataTable.getLearnableColumns()=" + resultDataTable.getLearnableColumns());
		return experimentResult;
	}

	private ISampler selectSampler(ISampler previousSampler) {
//		if (samplerMode == USE_RANDOM_SAMPLER || getSampler() == null) {
//			return selectRandomSampler();
//		}
		if (getSampler()==null) {
			return selectRandomSampler(previousSampler);
		}
		ISampler theSampler = getSampler();
		theSampler.setPreviousSampler(previousSampler);
		return theSampler;
	}
	
	/**
	 * Select a random sampler.  Choose among all customized samplers.  If none
	 * exist, choose among all base (uncustomized) samplers
	 * @return
	 */
	public ISampler selectRandomSampler(ISampler sampler) {
		List<ISampler> availableSamplers = SamplerLister.listSamplers(false);
		log.info("LC.selectRandomSampler(): availableSamplers(false)=" + availableSamplers);
		if (availableSamplers==null || availableSamplers.size()==0) {
			availableSamplers = SamplerLister.listSamplers(true);
		}
		int randomIndex = RandomListChooser.chooseRandomIndex(availableSamplers.size());
		return availableSamplers.get(randomIndex);
	}

	/**
	 * Ensure that the labelDataColumn field is populated with an appropriate field
	 * @param dataTable
	 */
//	private int selectLabelColumnIndex(IDataTable dataTable) {
//		assert(dataTable != null);
//		if (getLabelDataColumnIndex() >=0 && dataTable.getLearnableColumns().contains(getLabelDataColumnIndex())) {
//			return getLabelDataColumnIndex();
//		}
//		List<Arc> learnableColumns = dataTable.getLearnableColumns();
//		//otherwise, randomly select
//		int randomIndex = RandomListChooser.chooseRandomIndex(learnableColumns.size());
//		return learnableColumns.get(randomIndex);
//	}
	
	private IRapidMinerExperiment selectRapidMinerExperiment(IDataTable dataTable) {
//		assert(dataTable.getColumns().contains(labelDataColumn));
		log.info("Finding matching RapidMinerExperiment...");
		List<IRapidMinerExperiment> acceptableExperiments = RapidMinerExperimentLister.listMatchingExperiments(dataTable);
		log.info("Acceptable experiments include:" + acceptableExperiments);
		if (acceptableExperiments == null || acceptableExperiments.size() == 0) {
			log.warn("selectRapidMinerExperiment() finds no acceptable Experiments");
			return null;
		}
		
		//if a RapidMiner experiment has been specified and if it is acceptable, use it
		if (getRapidMinerExperiment() != null && acceptableExperiments.contains(getRapidMinerExperiment())) {
			return getRapidMinerExperiment();
		}
		
		//otherwise, randomly select from the list of acceptable RapidMiner experiments
		int randomIndex = RandomListChooser.chooseRandomIndex(acceptableExperiments.size());
		if (randomIndex < 0) {
			log.warn("random index from 0 to acceptableExperiments.size()-1 = " + randomIndex);
			return null;
		}
		log.info("Selected this RM Experiment: " + acceptableExperiments.get(randomIndex));
		return acceptableExperiments.get(randomIndex);
	}
	

	/** 
	 * Is it time to quit?  Subclasses may override and halt execution under some circumstances.
	 * @return
	 */
	public boolean isReadyToStopCycling() {
		return readyToStopCycling ;
	}

//	public int getSamplerMode() {
//		return samplerMode;
//	}
//
//	public void setSamplerMode(int samplerMode) {
//		this.samplerMode = samplerMode;
//	}
	
}
