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
import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.DataTable;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;

import thewebsemantic.Namespace;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * Contains the results of an experiment
 * @author David Donohue
 * Apr 16, 2008
 */
@Namespace(RDF.INQLE)
public class LearningCycle extends BasicJenabean implements ILearningCycle {
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

	@Override
	public LearningCycle createClone() {
		LearningCycle newObj = new LearningCycle();
		newObj.setPersister(persister);
		newObj.clone(this);
		return newObj;
	}

	@Override
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
		selectSampler();
		DataTable dataTable = sampler.execute(persister);
		selectLabel(dataTable);
		selectRapidMinerExperiment(dataTable);
		return runDataThroughExperiment(dataTable);
	}

	private void selectSampler() {
		if (getSampler() != null) {
			return;
		}
		
		List<ISampler> availableSamplers = SamplerLister.listSamplers();
		int randomIndex = RandomListChooser.chooseRandomIndex(availableSamplers.size());
		setSampler(availableSamplers.get(randomIndex));
	}

	/**
	 * Ensure that the labelDataColumn field is populated with an appropriate field
	 * @param dataTable
	 */
	private void selectLabel(DataTable dataTable) {
		assert(dataTable != null);
		List<DataColumn> columns = dataTable.getColumns();
		if (getLabelDataColumn() != null && columns.contains(getLabelDataColumn())) {
			return;
		}
		
		//otherwise, randomly select
		int randomIndex = RandomListChooser.chooseRandomIndex(columns.size());
		setLabelDataColumn(columns.get(randomIndex));
	}
	
	private void selectRapidMinerExperiment(DataTable dataTable) {
		assert(dataTable.getColumns().contains(getLabelDataColumn()));
		List<IRapidMinerExperiment> acceptableExperiments = RapidMinerExperimentLister.listMatchingExperiments(persister, dataTable, getLabelDataColumn());
		if (getRapidMinerExperiment() != null && acceptableExperiments.contains(getRapidMinerExperiment())) {
			return;
		}
		
		//otherwise, randomly select
		int randomIndex = RandomListChooser.chooseRandomIndex(acceptableExperiments.size());
		setRapidMinerExperiment(acceptableExperiments.get(randomIndex));
	}
	
	/**
	 * @param dataTable
	 * @return
	 */
	private ExperimentResult runDataThroughExperiment(DataTable dataTable) {
		//the results to return
		ExperimentResult experimentResult = new ExperimentResult();
		
		//get a RapidMiner Process object, representing the Experiment
		com.rapidminer.Process process = getRapidMinerExperiment().getProcess();
		
		//convert the DataTable into a RapidMiner MemoryExampleTable
		DataPreparer preparer = new DataPreparer(dataTable);
		MemoryExampleTable exampleTable = preparer.createExampleTable();
		
		//convert the MemoryExampleTable into a RapidMiner ExampleSet
		int labelIndex = dataTable.getColumns().indexOf(getLabelDataColumn());
		Attribute labelAttribute = exampleTable.getAttribute(labelIndex);
		Attribute weightAttribute = null;
		Attribute idAttribute = exampleTable.getAttribute(dataTable.getIdColumnIndex());
		ExampleSet exampleSet = 
			exampleTable.createExampleSet(
					labelAttribute, 
					weightAttribute, 
					idAttribute);
		
		//run this ExampleSet against the RapidMiner process
		IOObject[] inputIOObjects = new IOObject[] { exampleSet };
		IOContainer input = new IOContainer(inputIOObjects);
		IOContainer results = new IOContainer();
		try {
			results = process.run(input);
		} catch (OperatorException e) {
			log.error("Error running experiment:", e);
			experimentResult.setOperatorException(e);
		}
		
		experimentResult.setLearningCycle(this);
		try {
			experimentResult.setPerformanceVector(results.get(PerformanceVector.class));
		} catch (MissingIOObjectException e) {
			//no PerformanceVector present
		}
		return experimentResult;
	}
	
}
