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
@TargetDatamodel(ILearningCycle.LEARNING_CYCLES_DATASET)
@Namespace(RDF.INQLE)
public class LearningCycle extends UniqueJenabean implements ILearningCycle {
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

	public LearningCycle createClone() {
		LearningCycle newObj = new LearningCycle();
		//newObj.setPersister(persister);
		newObj.clone(this);
		return newObj;
	}

	public LearningCycle createReplica() {
		LearningCycle newObj = new LearningCycle();
		//newObj.setPersister(persister);
		newObj.replicate(this);
		return newObj;
	}
	
	public void clone(LearningCycle objectToBeCloned) {
		super.clone(objectToBeCloned);
		setSampler(objectToBeCloned.getSampler());
//		setSamplerMode(objectToBeCloned.getSamplerMode());
//		setLabelDataColumnIndex(objectToBeCloned.getLabelDataColumnIndex());
		setRapidMinerExperiment(objectToBeCloned.getRapidMinerExperiment());
	}
	
	public void replicate(LearningCycle objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
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
	public ExperimentResult execute() {
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
		ExperimentResult experimentResult = runDataThroughExperiment(resultDataTable, experimentToUse);
		
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
	 * Apply the IDataTable of data to the RapidMiner experiment, using the labelDataColumn as label.
	 * @param dataTable
	 * @return
	 */
	private ExperimentResult runDataThroughExperiment(IDataTable dataTable, IRapidMinerExperiment rapidMinerExperiment) {
		//the results to return
		ExperimentResult experimentResult = new ExperimentResult();
		experimentResult.setExperimentSubjectClass(dataTable.getSubjectClass());
		//get a RapidMiner Process object, representing the Experiment
		com.rapidminer.Process process = rapidMinerExperiment.createProcess();
		
		if (process == null) {
			log.warn("Unable to retrieve a RapidMiner experiment/process object for rapidMinerExperiment" + rapidMinerExperiment.getExperimentClassPath());
			return null;
		}
		//convert the IDataTable into a RapidMiner MemoryExampleTable
		MemoryExampleTableFactory memoryExampleTableFactory = new MemoryExampleTableFactory();
		MemoryExampleTable exampleTable =  memoryExampleTableFactory.createExampleTable(dataTable);
		
		//convert the MemoryExampleTable into a RapidMiner ExampleSet
//		int labelIndex = dataTable.getColumns().indexOf(labelDataColumn);
		
//		assert(labelIndex >= 0);
		Attribute labelAttribute = exampleTable.getAttribute(dataTable.getLabelColumnIndex());
		Attribute weightAttribute = null;
		Attribute idAttribute = null;
		if (dataTable.getIdColumnIndex() >= 0) {
			idAttribute = exampleTable.getAttribute(dataTable.getIdColumnIndex());
		}
		
		//log.info("labelIndex=" + labelIndex + "; labelAttribute=" + labelAttribute.getName());
		ExampleSet exampleSet = 
			exampleTable.createExampleSet(
					labelAttribute, 
					weightAttribute, 
					idAttribute);
		log.info("Created exampleSet of size " + exampleSet.size() + "."
				+ "\n\nID Attribute=" + exampleSet.getAttributes().getId()
				+ "\n\nLABEL Attribute=" + exampleSet.getAttributes().getLabel()
		);
		int i=0;
		Iterator<?> regularAttributeI = exampleSet.getAttributes().iterator();
		while (regularAttributeI.hasNext()) {
			i++;
			Attribute regularAttribute = (Attribute)regularAttributeI.next();
			//log.info("\n\nREGULAR Attribute =" + regularAttribute);
		}
		//run this ExampleSet against the RapidMiner process
		IOObject[] inputIOObjects = new IOObject[] { exampleSet };
		IOContainer input = new IOContainer(inputIOObjects);
		IOContainer results = new IOContainer();
		try {
			results = process.run(input);
		} catch (Exception e) {
			log.error("Error running experiment:", e);
			//experimentResult.setException(e);
		}
		
		//experimentResult.setLearningCycle(this);
		try {
			experimentResult.setPerformanceVector(results.get(PerformanceVector.class));
		} catch (MissingIOObjectException e) {
			//no PerformanceVector present
		}
		return experimentResult;
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