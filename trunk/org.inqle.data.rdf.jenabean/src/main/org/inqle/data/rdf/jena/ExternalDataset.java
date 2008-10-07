/**
 * 
 */
package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.Collection;

import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.RDF;
import thewebsemantic.Namespace;

/**
 * A Dataset, which contains external (minable) data
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDataset(Persister.METAREPOSITORY_DATASET)
@Namespace(RDF.INQLE)
public class ExternalDataset extends Dataset {

	private Collection<String> datasetFunctions = new ArrayList<String>();
	
	public ExternalDataset createClone() {
		ExternalDataset newObj = new ExternalDataset();
		newObj.clone(this);
		return newObj;
	}

	public ExternalDataset createReplica() {
		ExternalDataset newObj = new ExternalDataset();
		newObj.replicate(this);
		return newObj;
	}

	public Collection<String> getDatasetFunctions() {
		return datasetFunctions;
	}

	public void setDatasetFunctions(Collection<String> datasetFunctions) {
		this.datasetFunctions = datasetFunctions;
	}
	
	public void addDatasetFunction(String datasetFunction) {
		datasetFunctions.add(datasetFunction);
	}
	
	public void clone(ExternalDataset objectToBeCloned) {
		super.clone(objectToBeCloned);
		//setConnection(objectToBeCloned.getConnection());
		setDatasetFunctions(objectToBeCloned.getDatasetFunctions());
	}
	
	public void replicate(ExternalDataset objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}
	
}
