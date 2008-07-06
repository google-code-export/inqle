/**
 * 
 */
package org.inqle.data.rdf.jena;

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
