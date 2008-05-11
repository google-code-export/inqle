/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.RDF;

import thewebsemantic.Namespace;

/**
 * @author David Donohue
 * Apr 18, 2008
 */
@Namespace(RDF.INQLE)
public class RapidMinerExperiment extends GlobalJenabean implements IRapidMinerExperiment {

	private String experimentClassPath;
	private String experimentXml;

	private static Logger log = Logger.getLogger(RapidMinerExperiment.class);
	private String experimentType;
	
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
	public com.rapidminer.Process getProcess() {
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
	
	public void setExperimentType(String type) {
		this.experimentType = type;
	}
	
	public RapidMinerExperiment createClone() {
		RapidMinerExperiment newObj = new RapidMinerExperiment();
		newObj.clone(this);
		return newObj;
	}

//	public RapidMinerExperiment createReplica() {
//		RapidMinerExperiment newObj = new RapidMinerExperiment();
//		newObj.replicate(this);
//		return newObj;
//	}

	
	public void clone(RapidMinerExperiment objectToBeCloned) {
		super.clone(objectToBeCloned);
		setExperimentXml(objectToBeCloned.getExperimentXml());
		setExperimentType(objectToBeCloned.getExperimentType());
		setExperimentClassPath(objectToBeCloned.getExperimentClassPath());
	}
	
//	public void replicate(RapidMinerExperiment objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//		super.replicate(objectToClone);
//	}
}
