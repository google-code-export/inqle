/**
 * 
 */
package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

/**
 * A Dataset, which contains internal INQLE data
 * @author David Donohue
 * Jul 2, 2008
 */
@TargetDataset(Persister.METAREPOSITORY_DATASET)
@Namespace(RDF.INQLE)
public class InternalDataset extends Dataset {

	private String datasetRole;

	public String getDatasetRole() {
		return datasetRole;
	}
	public void setDatasetRole(String datasetRole) {
		this.datasetRole = datasetRole;
	}
	
	public void clone(InternalDataset objectToBeCloned) {
		super.clone(objectToBeCloned);
		setDatasetRole(objectToBeCloned.getDatasetRole());
	}
	
	public void replicate(InternalDataset objectToBeCloned) {
		clone(objectToBeCloned);
		setId(objectToBeCloned.getId());
	}
	
	public InternalDataset createClone() {
		InternalDataset newObj = new InternalDataset();
		newObj.clone(this);
		return newObj;
	}

	public InternalDataset createReplica() {
		InternalDataset newObj = new InternalDataset();
		newObj.replicate(this);
		return newObj;
	}
	
}
