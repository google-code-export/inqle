/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.UniqueJenabean;
import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.DataTable;
import org.inqle.data.sampling.DataTableWriter;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;

import thewebsemantic.Namespace;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.tools.Ontology;

/**
 * Contains the results of an experiment
 * @author David Donohue
 * Apr 16, 2008
 */
@Namespace(RDF.INQLE)
public class LearningCycle extends UniqueJenabean implements ILearningCycle {
	private ISampler sampler;
	private DataColumn labelDataColumn;
	private IRapidMinerExperiment rapidMinerExperiment;
	private Persister persister;

	private static Logger log = Logger.getLogger(LearningCycle.class);
	
	public void setPersister(Persister persister) {
		this.persister = persister;
	}
	
	public ISampler getSampler() {
		return sampler;
	}

	public void setSampler(ISampler sampler) {
//		this.sampler = sampler.createClone();
		this.sampler = sampler;
	}
	
	public DataColumn getLabelDataColumn() {
		return labelDataColumn;
	}
	
	public void setLabelDataColumn(DataColumn labelDataColumn) {
		this.labelDataColumn = labelDataColumn;
	}

	public IRapidMinerExperiment getRapidMinerExperiment() {
		return rapidMinerExperiment;
	}

	public void setRapidMinerExperiment(IRapidMinerExperiment rapidMinerExperiment) {
		this.rapidMinerExperiment = rapidMinerExperiment;
	}

	public LearningCycle createClone() {
		LearningCycle newObj = new LearningCycle();
		newObj.setPersister(persister);
		newObj.clone(this);
		return newObj;
	}

	public LearningCycle createReplica() {
		LearningCycle newObj = new LearningCycle();
		newObj.setPersister(persister);
		newObj.replicate(this);
		return newObj;
	}
	
	public void clone(LearningCycle objectToBeCloned) {
		super.clone(objectToBeCloned);
		setSampler(objectToBeCloned.getSampler());
		setLabelDataColumn(objectToBeCloned.getLabelDataColumn());
		setRapidMinerExperiment(objectToBeCloned.getRapidMinerExperiment());
	}
	
	public void replicate(LearningCycle objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
	}
	
	/**
	 * Execute the Learning Cycle.  
	 * Use the provided sampler to create a DataTable of data for learning.  
	 * Next, ensure that a label has been selected (or randomly choose one).
	 * Next, ensure that a proper RapidMiner experiment has been selected
	 * (or randomly choose one).
	 * Next, convert the DataTable into a RapidMiner ExampleSet.
	 * Finally, run the ExampleSet through the experiment.
	 */
	public ExperimentResult execute() {
		ISampler samplerToUse = selectSampler();
		if (samplerToUse == null) {
			log.warn("Unable to retrieve any sampler.");
			return null;
		}
		DataTable resultDataTable = samplerToUse.execute(persister);
		if (resultDataTable == null) {
			log.warn("Sampler " + samplerToUse + " of class " + samplerToUse.getClass() + " was unable" +
					" to retrieve a DataTable of results.");
			return null;
		}
		DataColumn labelDataColumn = selectLabel(resultDataTable);
		//log.info("LLLLL Selected label DataColumn =" + labelDataColumn + " of data type " + labelDataColumn.getDataType());
		
		//assign data type to this data column
		int labelDataColumnIndex = resultDataTable.getColumns().indexOf(labelDataColumn);
		DataPreparer preparer = new DataPreparer(resultDataTable);
		preparer.assignDataType(labelDataColumnIndex);
		
		IRapidMinerExperiment experimentToUse = selectRapidMinerExperiment(resultDataTable, labelDataColumn);
		if (experimentToUse == null) {
			log.warn("No RapidMiner experiment was found to match the labelDataColumn=" + labelDataColumn);
			return null;
		}
		ExperimentResult experimentResult = runDataThroughExperiment(resultDataTable, experimentToUse, labelDataColumn);
		
		//add metadata to experimentResult
		DataColumn idColumn = resultDataTable.getColumn(resultDataTable.getIdColumnIndex());
		
		experimentResult.setSamplerClassName(samplerToUse.getClass().getName());
		experimentResult.setExperimentSubjectArc(idColumn.getArc());
		experimentResult.setExperimentLabelArc(labelDataColumn.getArc());
		experimentResult.setRapidMinerExperiment(experimentToUse);
		experimentResult.setExperimentAttributeArcs(resultDataTable.getLearnableColumnArcSet());
		//experimentResult.setExperimentSubject(resultDataTable.getColumn(resultDataTable.getIdColumnIndex()).getColumnUri());
		log.info("&&&&&&&&&&&&&&& idColumn.getArc()=" + idColumn.getArc());
		log.info("resultDataTable.getLearnableColumnArcSet()=" + resultDataTable.getLearnableColumnArcSet());
		return experimentResult;
	}

	private ISampler selectSampler() {
		if (getSampler() != null) {
			return getSampler();
		}
		
		List<ISampler> availableSamplers = SamplerLister.listSamplers();
		int randomIndex = RandomListChooser.chooseRandomIndex(availableSamplers.size());
		return availableSamplers.get(randomIndex);
	}

	/**
	 * Ensure that the labelDataColumn field is populated with an appropriate field
	 * @param dataTable
	 */
	private DataColumn selectLabel(DataTable dataTable) {
		assert(dataTable != null);
		List<DataColumn> learnableColumns = dataTable.getLearnableColumns();
		if (getLabelDataColumn() != null && learnableColumns.contains(getLabelDataColumn())) {
			return getLabelDataColumn();
		}
		
		//otherwise, randomly select
		int randomIndex = RandomListChooser.chooseRandomIndex(learnableColumns.size());
		return learnableColumns.get(randomIndex);
	}
	
	private IRapidMinerExperiment selectRapidMinerExperiment(DataTable dataTable, DataColumn labelDataColumn) {
		assert(dataTable.getColumns().contains(labelDataColumn));
		assert(persister != null);
		//log.info("Finding matching RapidMinerExperiment for label: " + labelDataColumn + "\nDataTable=" + DataTableWriter.dataTableToString(dataTable));
		List<IRapidMinerExperiment> acceptableExperiments = RapidMinerExperimentLister.listMatchingExperiments(persister, dataTable, labelDataColumn);
		
		if (acceptableExperiments == null || acceptableExperiments.size() == 0) {
			//log.warn("selectRapidMinerExperiment() finds no acceptable Experiments");
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
		return acceptableExperiments.get(randomIndex);
	}
	
	/**
	 * Apply the DataTable of data to the RapidMiner experiment, using the labelDataColumn as label.
	 * @param dataTable
	 * @return
	 */
	private ExperimentResult runDataThroughExperiment(DataTable dataTable, IRapidMinerExperiment rapidMinerExperiment, DataColumn labelDataColumn) {
		//the results to return
		ExperimentResult experimentResult = new ExperimentResult();
		//get a RapidMiner Process object, representing the Experiment
		com.rapidminer.Process process = rapidMinerExperiment.createProcess();
		
		if (process == null) {
			log.warn("Unable to retrieve a RapidMiner experiment/process object for rapidMinerExperiment" + rapidMinerExperiment.getExperimentClassPath());
			return null;
		}
		//convert the DataTable into a RapidMiner MemoryExampleTable
		DataPreparer preparer = new DataPreparer(dataTable);
		MemoryExampleTable exampleTable = preparer.createExampleTable();
		
		//convert the MemoryExampleTable into a RapidMiner ExampleSet
		int labelIndex = dataTable.getColumns().indexOf(labelDataColumn);
		
		assert(labelIndex >= 0);
		Attribute labelAttribute = exampleTable.getAttribute(labelIndex);
		Attribute weightAttribute = null;
		Attribute idAttribute = exampleTable.getAttribute(dataTable.getIdColumnIndex());
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
			log.info("\n\nREGULAR Attribute =" + regularAttribute);
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
	
}
