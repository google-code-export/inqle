/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.RDF;
import org.inqle.data.sampling.IDataTable;
import org.inqle.experiment.rapidminer.util.RapidMinerProcessCreator;

import org.inqle.experiment.rapidminer.PerformanceVectorResult;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.performance.PerformanceVector;

import thewebsemantic.Id;
import thewebsemantic.Namespace;

/**
 * This class handles execution of any RapidMiner experiment which applies either a 
 * classification or regression algorithm to a data table, followed by cross-validation to 
 * test the validity of the model.
 * @author David Donohue
 * Apr 18, 2008
 */
@TargetDatamodel(IRapidMinerExperiment.RAPID_MINER_EXPERIMENTS_DATAMODEL)
@Namespace(RDF.INQLE)
public class ClassificationRegressionCrossValidationExperiment extends GlobalJenabean implements IRapidMinerExperiment {

	private String experimentClassPath;
	private String experimentXml;

	private static Logger log = Logger.getLogger(ClassificationRegressionCrossValidationExperiment.class);
	private String experimentType;
	public static final String REGRESSION_TYPE = "regression";
	public static final String CLASSIFICATION_TYPE = "classification";
	
	@Override
	@Id
	public String getId() {
		if (experimentClassPath != null) {
			return experimentClassPath;
		} else {
			return super.getId();
		}
	}
	
	/**
	 * @see org.inqle.experiment.rapidminer.IRapidMinerExperiment#getExperimentFilePath()
	 */
	public String getExperimentClassPath() {
		return experimentClassPath;
	}

	/**
	 * If experiment XML has been explicitly set, return it.  otherwise read
	 * it from the file
	 * @see org.inqle.experiment.rapidminer.IRapidMinerExperiment#getExperimentXml()
	 */
	public String getExperimentXml() {
		return experimentXml;
	}
	
	public String readExperimentXml() {
		if (getExperimentClassPath() == null) {
			return null;
		}
		
		InputStream in = this.getClass().getResourceAsStream(getExperimentClassPath());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
		} catch (IOException e) {
			log.error("Unable to load RapidMiner experiment file " + getExperimentClassPath(), e);
		}
		return stringBuffer.toString();
	}

	/** 
	 * @see org.inqle.experiment.rapidminer.IRapidMinerExperiment#setExperimentFilePath(java.lang.String)
	 */
	public void setExperimentClassPath(String experimentClassPath) {
		this.experimentClassPath = experimentClassPath;
	}

	/**
	 * @see org.inqle.experiment.rapidminer.IRapidMinerExperiment#setExperimentXml(java.lang.String)
	 */
	public void setExperimentXml(String experimentXml) {
		this.experimentXml = experimentXml;
	}
	
	/**
	 * Get a RapidMiner Process object
	 */
	public com.rapidminer.Process createProcess() {
		if (getExperimentXml() != null) {
			try {
				return new com.rapidminer.Process(getExperimentXml());
			} catch (Exception e) {
				log.error("Unable to create RapidMiner Process using this XML: " + getExperimentXml(), e);
				return null;
			}
		}
		
		if (getExperimentClassPath() != null) {
			InputStream in = this.getClass().getResourceAsStream(getExperimentClassPath());
			try {
				return new com.rapidminer.Process(in);
			} catch (Exception e) {
				log.error("Unable to create RapidMiner Process using this XML stored in this location: " + getExperimentClassPath(), e);
				return null;
			}
		}
		
		return null;
	}

	public String getExperimentType() {
		return experimentType;
	}
	
	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}
	
	public ClassificationRegressionCrossValidationExperiment createClone() {
		ClassificationRegressionCrossValidationExperiment newObj = new ClassificationRegressionCrossValidationExperiment();
		newObj.clone(this);
		return newObj;
	}

//	public RapidMinerExperiment createReplica() {
//		RapidMinerExperiment newObj = new RapidMinerExperiment();
//		newObj.replicate(this);
//		return newObj;
//	}

	
	public void clone(ClassificationRegressionCrossValidationExperiment objectToBeCloned) {
		super.clone(objectToBeCloned);
		setExperimentXml(objectToBeCloned.getExperimentXml());
		setExperimentType(objectToBeCloned.getExperimentType());
		setExperimentClassPath(objectToBeCloned.getExperimentClassPath());
	}

	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[experimentType=" + experimentType + "]\n";
		s += "[experimentClassPath=" + experimentClassPath + "]\n";
		s += "[experimentXml=" + experimentXml + "]\n";
		s += "}";
		return s;
	}

	@Override
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
			log.info("Experiment: " + getStringRepresentation() + "\nmatches because it is a REGRESSION learner and the data has a numeric label.");
			return true;
		} else if (typeList.contains(ClassificationRegressionCrossValidationExperiment.CLASSIFICATION_TYPE) && dataTable.getDataType(dataTable.getLabelColumnIndex()) == IDataTable.DATA_TYPE_STRING) {
			log.info("Experiment: " + getStringRepresentation() + "\nmatches because it is a CLASSIFICATION learner and the data has a string label.");
			return true;
		}
		log.info("Experiment: " + getStringRepresentation() + "\nDOES NOT MATCH this data table because the data type of the label does not match the capabilities of the experiment (" + getExperimentType() + ").");
		return false;
	}

	/**
	 * Apply this experiment to the IDataTable of data.
	 * @param dataTable
	 * @return
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
		
		//convert the MemoryExampleTable into a RapidMiner ExampleSet
//			int labelIndex = dataTable.getColumns().indexOf(labelDataColumn);
		
//			assert(labelIndex >= 0);
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
	
//	public void replicate(RapidMinerExperiment objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//		super.replicate(objectToClone);
//	}
}
