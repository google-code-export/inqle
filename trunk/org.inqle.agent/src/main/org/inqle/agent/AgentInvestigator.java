package org.inqle.agent;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.osgi.framework.Bundle;



/**
 * This generates DataExtractor, runs it, feeds resulting DataTable thru an experiment
 * @author David Donohue
 * May 28, 2007
 * 
 * TODO use random seed
 */
public class AgentInvestigator extends Thread {

	//initial status is: RESOLVED (stopped)
	private int status = Bundle.RESOLVED;

	private DataTable dataTable;

	private DataExtractionInfo dataExtractionInfo;

	private ExperimentInfo experimentInfo;

	private String experimentID;

	static Logger log = Logger.getLogger(inqle.agent.equinox.AgentInvestigator.class);
	
	public void signalStop() {
		status = Bundle.STOPPING;
	}
	
	public int getStatus() {
		return status;
	}
	
	/**
	 * Conduct experiments in a new thread
	 * TODO cache some info like AppConfig, ExperimentAModel, etc. for quicker execution
	 */
	public void run() {
		//if this is already running, return
		if (status == Bundle.ACTIVE) {
			log.warn("Signalled to start the AgentInvestigator, but it is already running!");
			return;
		}
		log.info("Started AgentInvestigator.");
		status = Bundle.ACTIVE;
		while(status == Bundle.ACTIVE) {
			//first select & run a DataExtractor
			List allExtractors = EquinoxUtils.getExtensions(InqleInfo.EXTENSION_POINT_EXTRACTOR);
			
			//select a random data extractor
			log.info("Select random number from 0 to " + (allExtractors.size() - 1) + "...");
			int randint = 0;
			if (allExtractors.size() > 1) {
				Random r = new Random(System.currentTimeMillis());
				randint = r.nextInt(allExtractors.size()-1);
			}
			log.info("Random number =" + randint);
			SortableConfigInfo extractorInfo = (SortableConfigInfo) allExtractors.get(randint);
			
			String extractorClassName = extractorInfo.getAttribute("class");
			log.info("Creating object of class " + extractorClassName + "...");
			Class extractorClass;
			ADataExtractorTool extractorObj = null;
			try {
				extractorClass = Class.forName(extractorClassName);
				extractorObj = (ADataExtractorTool)extractorClass.newInstance();
				log.info("Created object " + extractorObj.getId());
			} catch (Exception e) {
				log.error("Unable to create class " + extractorClassName + ".  Restarting.", e);
				continue;
			}
			
			//run the data extractor
			extractorObj.init();
			extractorObj.doAllSteps();
			log.info("Ran all data extraction steps.");
			
			RdfTable rdfTable = extractorObj.getRdfTable();
			RdfTableFactory rdfTableFactory = new RdfTableFactory();
		  rdfTable = rdfTableFactory.replaceBlankNodes(rdfTable);
		  DataTableFactory factory = new DataTableFactory();
		  dataTable = null;
			try {
				dataTable = factory.createDataTable(rdfTable);
			} catch (Exception e) {
				log.error("Unable to create DataTable from RdfTable.", e);
			}
			
			//if result table is null, restart a new cycle
			if (dataTable == null) {
				log.info("DataTable is null.  Restart cycle.");
				continue;
			}
			
			//next run the DataTable through a random experiment
			IOContainer resultContainer = runRandomExperiment();
			if (resultContainer == null) {
				log.info("Result IOContainer is null.  Restart cycle.");
				continue;
			}
			
			//log pertinent info
			dataExtractionInfo = extractorObj.getDataExtractionInfo();
			experimentInfo = new ExperimentInfo();
		  experimentInfo.init();
		  experimentInfo.addMemberAttribute(dataExtractionInfo.getClassUri(), dataExtractionInfo);
		  
		  //now that DataTable has label info, capture its DataColumn info in the ExperimentInfo object
		  experimentInfo.addDataColumns(dataTable);
		  
		  log.info(getResultSummary(resultContainer));
		  experimentInfo.setResultContainer(resultContainer);

		  //save the lab book entry
		  log.info("Persisting results as RDF.");
		  ARQTeller teller = new ARQTeller(null);
		  ARQTeller.persist(experimentInfo, teller.getExperimentModel(), true);

		}
		
		//exited therefore status is not: ACTIVE (running).  Set to: RESOLVED (stopped) and exit
		status = Bundle.RESOLVED;
		log.info("Stopped AgentInvestigator.");
	}
	
	/**
	 * Runs an experiment, given the DataTable of data.
	 * Selects an experiment at random
	 * @param dataTable the DataTable of data to do experiment on
	 * @param experimentInfo the ExperimentInfo object 
	 * @return
	 * 
	 * TODO consider more sophisticated method of selecting the label.  curently selects last column
	 */
	public IOContainer runRandomExperiment() {
		log.info("Running experiment on data:" + dataTable);
		
		//convert the DataTable into a learnable table of examples
		DataPreparer preparer = new DataPreparer(dataTable);
		MemoryExampleTable exampleTable = preparer.createExampleTable();
		Attribute[] attributes = exampleTable.getAttributes();
		
		//TODO loop thru non-string attributes and run an experiment for each OR randomly select the label
		//for now use the last attribute as the label
		log.info("exampleTable has " + attributes.length + " attributes.");
		Attribute labelAttribute = exampleTable.getAttribute(attributes.length - 1);
		log.info("Setting label attribute to " + labelAttribute.getName());
		dataTable.setLabelColumnID(labelAttribute.getName());
		
		ExampleSet exampleSet = null;
		try {
			exampleSet = exampleTable.createExampleSet(labelAttribute);
			//set the first attribute as the ID
			Iterator attsI = exampleSet.getAttributes().iterator();
			Attribute idattribute = (Attribute) attsI.next();
      exampleSet.getAttributes().remove(idattribute);
      exampleSet.getAttributes().setId(idattribute);
      
      //capture info about the example set in the lab book entry
      experimentInfo.setExampleSet(exampleSet);
		} catch (Exception e) {
			log.error("Error creating ExampleSet", e);
		}
		
		//SortableConfigInfo experimentInfo = ExperimentMatcher.getRandomExperiment();
		SortableConfigInfo experimentConfig = ExperimentMatcher.getRandomExperimentMatchingLabel(labelAttribute);
		experimentID  = experimentConfig.getAttribute("id");
		try {
			Experimenter experimenter = new Experimenter(experimentID);
			return experimenter.run(exampleSet);
		} catch (Exception e) {
			log.error("Error creating an Experimenter", e);
		}
		return null;
		
	}

	public boolean isStopped() {
		if (status == Bundle.RESOLVED) return true;
		return false;
	}

	public String getResultSummary(IOContainer resultContainer) {
		if (resultContainer == null) return null;
		String str = "-------------------------------------------------------\n";
		str += "Begin Results of experiment '" + experimentID + "\n";
		IOObject[] containedObjects =	resultContainer.getIOObjects();
		for (int i=0; i<containedObjects.length; i++) {
			IOObject ioobject = containedObjects[i];
			str += "------------Object #" + i + ") " + ioobject.getClass() + "\n";
			str += ioobject;
		}
		str += "End Results of experiment '" + experimentID + "\n";
		str += "-------------------------------------------------------\n";
		return str;
	}
}
