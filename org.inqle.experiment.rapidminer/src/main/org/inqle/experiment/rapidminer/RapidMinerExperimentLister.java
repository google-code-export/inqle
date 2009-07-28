package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.IDataTable;
import org.inqle.ui.rap.util.ExtensionSecurityManager;

public class RapidMinerExperimentLister {

	private static Logger log = Logger.getLogger(RapidMinerExperimentLister.class);
	
	public static List<IRapidMinerExperiment> listRapidMinerExperiments() {
		List<IRapidMinerExperiment> experiments = new ArrayList<IRapidMinerExperiment>();
		
		List<IExtensionSpec> extensionSpecs = 
			ExtensionSecurityManager.getPermittedExtensionSpecs(IRapidMinerExperiment.ID);
		
		for (IExtensionSpec extensionSpec: extensionSpecs) {
			if (extensionSpec == null) continue;
			IRapidMinerExperiment experiment = RapidMinerExperimentFactory.createRapidMinerExperiment(extensionSpec);
			experiments.add(experiment);
		}
		
		//TODO v0.2: add experiments from RDF
		
		return experiments;
	}

	/**
	 * Of the list of all experiments, return a sublist, 
	 * containing only those which are applicable to the IDataTable and the 
	 * label attribute
	 * @param dataTable
	 * @param labelDataColumn
	 * @return
	 * 
	 * TODO make this more sophisticated, matching algorithms to experiments
	 * also on basis of the attributes and other factors
	 */
	public static List<IRapidMinerExperiment> listMatchingExperiments(IDataTable dataTable) {
		List<IRapidMinerExperiment> allExperiments = listRapidMinerExperiments();
		List<IRapidMinerExperiment> matchingExperiments = new ArrayList<IRapidMinerExperiment>();
		//log.info("dataTable.getLabelColumnIndex()=" + dataTable.getLabelColumnIndex());
		//log.info("dataTable.getDataType(dataTable.getLabelColumnIndex())=" + dataTable.getDataType(dataTable.getLabelColumnIndex()));
		
		for (IRapidMinerExperiment experiment: allExperiments) {
			if (experiment.handlesDataTable(dataTable)) {
				matchingExperiments.add(experiment);
			}
		}
		return matchingExperiments;
	}
}
