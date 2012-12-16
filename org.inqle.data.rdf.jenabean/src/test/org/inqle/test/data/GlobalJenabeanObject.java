package org.inqle.test.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.inqle.rdf.beans.GlobalJenabean;

import thewebsemantic.Id;

public class GlobalJenabeanObject extends GlobalJenabean {

	private String experimentClassPath;
	private String experimentXml;

	private static Logger log = Logger.getLogger(GlobalJenabeanObject.class);
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
//	public com.rapidminer.Process createProcess() {
//		if (getExperimentXml() != null) {
//			try {
//				return new com.rapidminer.Process(getExperimentXml());
//			} catch (Exception e) {
//				log.error("Unable to create RapidMiner Process using this XML: " + getExperimentXml(), e);
//				return null;
//			}
//		}
//		
//		if (getExperimentClassPath() != null) {
//			InputStream in = this.getClass().getResourceAsStream(getExperimentClassPath());
//			try {
//				return new com.rapidminer.Process(in);
//			} catch (Exception e) {
//				log.error("Unable to create RapidMiner Process using this XML stored in this location: " + getExperimentClassPath(), e);
//				return null;
//			}
//		}
//		
//		return null;
//	}

	public String getExperimentType() {
		return experimentType;
	}
	
	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}
	
//	public GlobalJenabeanObject createClone() {
//		GlobalJenabeanObject newObj = new GlobalJenabeanObject();
//		newObj.clone(this);
//		return newObj;
//	}

//	public RapidMinerExperiment createReplica() {
//		RapidMinerExperiment newObj = new RapidMinerExperiment();
//		newObj.replicate(this);
//		return newObj;
//	}

	
//	public void clone(GlobalJenabeanObject objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		setExperimentXml(objectToBeCloned.getExperimentXml());
//		setExperimentType(objectToBeCloned.getExperimentType());
//		setExperimentClassPath(objectToBeCloned.getExperimentClassPath());
//	}

	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[experimentType=" + experimentType + "]\n";
		s += "[experimentClassPath=" + experimentClassPath + "]\n";
		s += "[experimentXml=" + experimentXml + "]\n";
		s += "}";
		return s;
	}

}
