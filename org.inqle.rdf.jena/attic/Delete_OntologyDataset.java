package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

@TargetDatamodel(Persister.METAREPOSITORY_DATASET)
@Namespace(RDF.INQLE)
@Deprecated
public class Delete_OntologyDataset extends Datamodel {

	private String pathToOntology;
	private String ontologyFilesHash;
	
	public Delete_OntologyDataset createClone() {
		Delete_OntologyDataset newObj = new Delete_OntologyDataset();
		newObj.clone(this);
		return newObj;
	}
	
	public Delete_OntologyDataset createReplica() {
		Delete_OntologyDataset newObj = new Delete_OntologyDataset();
		newObj.replicate(this);
		return newObj;
	}

	public String getPathToOntology() {
		return pathToOntology;
	}

	public void setPathToOntology(String pathToOntology) {
		this.pathToOntology = pathToOntology;
	}

//	public String getOntologyFilesHash() {
//		return ontologyFilesHash;
//	}
//
//	public void setOntologyFilesHash(String ontologyFilesHash) {
//		this.ontologyFilesHash = ontologyFilesHash;
//	}

}
