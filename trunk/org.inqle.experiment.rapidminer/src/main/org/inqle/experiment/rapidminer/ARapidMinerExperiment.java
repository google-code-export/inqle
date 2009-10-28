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
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;
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
 * This is the abstract base class for RapidMiner experiments
 * @author David Donohue
 * July 30, 2009
 */

public abstract class ARapidMinerExperiment extends GlobalJenabean implements IRapidMinerExperiment {

	private String experimentClassPath;
	private String experimentXml;

	private static Logger log = Logger.getLogger(ARapidMinerExperiment.class);
	private String experimentType;
	
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

	public String getExperimentType() {
		return experimentType;
	}
	
	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}
	
//	public void clone(ARapidMinerExperiment objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		setExperimentXml(objectToBeCloned.getExperimentXml());
//		setExperimentType(objectToBeCloned.getExperimentType());
//		setExperimentClassPath(objectToBeCloned.getExperimentClassPath());
//	}
//
//	public String getStringRepresentation() {
//		String s = getClass().toString() + " {\n";
//		s += "[experimentType=" + experimentType + "]\n";
//		s += "[experimentClassPath=" + experimentClassPath + "]\n";
//		s += "[experimentXml=" + experimentXml + "]\n";
//		s += "}";
//		return s;
//	}
}
