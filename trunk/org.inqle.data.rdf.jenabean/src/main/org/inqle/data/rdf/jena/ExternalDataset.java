/**
 * 
 */
package org.inqle.data.rdf.jena;

/**
 * A Dataset, which contains external (minable) data
 * @author David Donohue
 * Jul 2, 2008
 */
public class ExternalDataset extends Dataset {

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
}
