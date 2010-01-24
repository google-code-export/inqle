/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.data.sampling.IDataTable;
import org.inqle.experiment.rapidminer.util.RapidMinerProcessCreator;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetModelName;

import thewebsemantic.Namespace;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * This class handles execution of those RapidMiner experiments, which apply either 
 * a classification or regression algorithm to a data table, followed by cross-validation to 
 * test the validity of the model, and output a PerfomanceVector.
 * @author David Donohue
 * July 30, 2009
 */
@TargetModelName(IRapidMinerExperiment.RAPID_MINER_EXPERIMENTS_DATAMODEL)
@Namespace(RDF.INQLE)
public class ClassificationRegressionCrossValidationExperiment extends ARapidMinerExperiment {

	private static Logger log = Logger.getLogger(ClassificationRegressionCrossValidationExperiment.class);
	public static final String REGRESSION_TYPE = "regression";
	public static final String CLASSIFICATION_TYPE = "classification";
	
//	@Override
//	public ClassificationRegressionCrossValidationExperiment createClone() {
//		ClassificationRegressionCrossValidationExperiment newObj = new ClassificationRegressionCrossValidationExperiment();
//		newObj.clone(this);
//		return newObj;
//	}

	@Override
	/**
	 * If this experiment can handle the IDataTable, return true
	 * Otherwise, return false
	 */
	public boolean handlesDataTable(IDataTable dataTable) {
		String[] types = getExperimentType().split("\\|");
		ArrayList<String> typeList = new ArrayList<String>();
		for (String type: types) {
			if (type == null) {
				continue;
			}
			typeList.add(type.trim().toLowerCase());
		}
		
		if (typeList.contains(ClassificationRegressionCrossValidationExperiment.REGRESSION_TYPE) && dataTable.getDataType(dataTable.getLabelColumnIndex()) == IDataTable.DATA_TYPE_NUMERIC) {
			log.info("Experiment: " + toString() + "\nmatches because it is a REGRESSION learner and the data has a numeric label.");
			return true;
		} else if (typeList.contains(ClassificationRegressionCrossValidationExperiment.CLASSIFICATION_TYPE) && dataTable.getDataType(dataTable.getLabelColumnIndex()) == IDataTable.DATA_TYPE_STRING) {
			log.info("Experiment: " + toString() + "\nmatches because it is a CLASSIFICATION learner and the data has a string label.");
			return true;
		}
		log.info("Experiment: " + toString() + "\nDOES NOT MATCH this data table because the data type of the label does not match the capabilities of the experiment (" + getExperimentType() + ").");
		return false;
	}

	/**
	 * Apply this experiment to the IDataTable of data.
	 * @param dataTable
	 * @return the IExperimentResult object
	 */
	@Override
	public IExperimentResult runExperiment(IDataTable dataTable) {
		
		//the results to return
		PerformanceVectorResult experimentResult = new PerformanceVectorResult();
		experimentResult.setExperimentSubjectClass(dataTable.getSubjectClass());
		//get a RapidMiner Process object, representing the Experiment
		com.rapidminer.Process process = RapidMinerProcessCreator.createProcess(this);
		
		if (process == null) {
			log.warn("Unable to retrieve a RapidMiner experiment/process object for rapidMinerExperiment" + getExperimentClassPath());
			return null;
		}

		//convert the IDataTable into a RapidMiner MemoryExampleTable
		MemoryExampleTableFactory memoryExampleTableFactory = new MemoryExampleTableFactory();
		MemoryExampleTable exampleTable =  memoryExampleTableFactory.createExampleTable(dataTable);
		
		Attribute labelAttribute = exampleTable.getAttribute(dataTable.getLabelColumnIndex());
		Attribute weightAttribute = null;
		Attribute idAttribute = null;
		if (dataTable.getIdColumnIndex() >= 0) {
			idAttribute = exampleTable.getAttribute(dataTable.getIdColumnIndex());
		}
		
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
