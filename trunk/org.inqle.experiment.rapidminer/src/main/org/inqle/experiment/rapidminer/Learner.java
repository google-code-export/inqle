/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.util.RandomListChooser;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.TargetDatamodelName;
import org.inqle.data.rdf.jenabean.UniqueJenabean;
import org.inqle.data.sampling.IDataTable;
import org.inqle.data.sampling.ISampler;
import org.inqle.data.sampling.SamplerLister;

import thewebsemantic.Namespace;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.MissingIOObjectException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * Executes a learning algorithm on an IDataTable of data.
 * 
 * @author David Donohue
 * Apr 16, 2008
 */
public class Learner {
	
	private static Logger log = Logger.getLogger(Learner.class);
	
	/**
	 * Execute the Learning Cycle.  
	 * Use the provided sampler to create a IDataTable of data for learning.  
	 * Next, ensure that a label has been selected (or randomly choose one).
	 * Next, ensure that a proper RapidMiner experiment has been selected
	 * (or randomly choose one).
	 * Next, convert the IDataTable into a RapidMiner ExampleSet.
	 * Finally, run the ExampleSet through the experiment.
	 */
	public static IExperimentResult execute(IDataTable resultDataTable, IRapidMinerExperiment rapidMinerExperiment) {
		
		//test to see if the RM experiment matches 
		if (! rapidMinerExperiment.handlesDataTable(resultDataTable)) {
			return null;
		}
		
		IExperimentResult experimentResult = rapidMinerExperiment.runExperiment(resultDataTable);
		
		if (resultDataTable.getIdColumnIndex() >= 0) {
			experimentResult.setExperimentSubjectArc(resultDataTable.getColumn(resultDataTable.getIdColumnIndex()));
		}
		experimentResult.setExperimentLabelArc(resultDataTable.getColumn(resultDataTable.getLabelColumnIndex()));
		experimentResult.setRapidMinerExperimentId(rapidMinerExperiment.getId());
		List<Arc> learnableArcs = resultDataTable.getLearnableColumns();
		experimentResult.setExperimentAttributeArcs(learnableArcs);
		
		return experimentResult;
	}
	
}
