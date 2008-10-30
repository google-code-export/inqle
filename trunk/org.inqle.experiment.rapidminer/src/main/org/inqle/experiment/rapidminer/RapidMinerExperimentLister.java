package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.IDataTable;

import com.rapidminer.tools.Ontology;

public class RapidMinerExperimentLister {

	private static Logger log = Logger.getLogger(RapidMinerExperimentLister.class);
	
	public static List<IRapidMinerExperiment> listRapidMinerExperiments(Persister persister) {
		List<IRapidMinerExperiment> experiments = new ArrayList<IRapidMinerExperiment>();
		
		List<IExtensionSpec> extensionSpecs = 
			ExtensionFactory.getExtensionSpecs(IRapidMinerExperiment.ID);
		
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
		Persister persister = Persister.getInstance();
		List<IRapidMinerExperiment> allExperiments = listRapidMinerExperiments(persister);
		List<IRapidMinerExperiment> matchingExperiments = new ArrayList<IRapidMinerExperiment>();
		for (IRapidMinerExperiment experiment: allExperiments) {
			
			String[] types = experiment.getExperimentType().split("\\|");
			ArrayList<String> typeList = new ArrayList<String>();
			for (String type: types) {
				if (type == null) {
					continue;
				}
				typeList.add(type.trim().toLowerCase());
			}
			
			if (dataTable.getDataType(dataTable.getLabelColumnIndex()) == Ontology.REAL && typeList.contains(IRapidMinerExperiment.REGRESSION_TYPE)) {
				matchingExperiments.add(experiment);
			}
			if (dataTable.getDataType(dataTable.getLabelColumnIndex()) == Ontology.NOMINAL && typeList.contains(IRapidMinerExperiment.CLASSIFICATION_TYPE)) {
				matchingExperiments.add(experiment);
			}
		}
		return matchingExperiments;
	}
}