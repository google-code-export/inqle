package org.inqle.data.rdf.jena;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;

import thewebsemantic.Namespace;

/**
 * originally used Jena Assembler (JA) language
 * http://jena.sourceforge.net/assembler/assembler-howto.html
 * but revamped to not use JA.
 * @author David Donohue
 * Jan 9, 2008
 */
@TargetDatamodel(Persister.METAREPOSITORY_DATAMODEL)
@Namespace(RDF.INQLE)
public class Datafile extends Datamodel {

	private String fileUrl;

//	public void clone(Datafile objectToBeCloned) {
//		super.clone(objectToBeCloned);
//		setFileUrl(objectToBeCloned.getFileUrl());
//	}
	
//	public void replicate(Datafile objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//		super.replicate(objectToClone);
//	}
	
//	public Datafile createClone() {
//		Datafile newObj = new Datafile();
//		newObj.clone(this);
//		return newObj;
//	}
//
//	public Datafile createReplica() {
//		Datafile newObj = new Datafile();
//		newObj.replicate(this);
//		return newObj;
//	}
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
