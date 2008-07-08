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
@TargetDataset(Persister.METAREPOSITORY_DATASET)
@Namespace(RDF.INQLE)
public class Datafile extends NamedModel {

	private String fileUrl;
	
//	private String directory;
//	private String fileEncoding;
//	private boolean mapName = false;

//	@RdfProperty(NS + "directory")
//	public String getDirectory() {
//		return directory;
//	}
//
//	public void setDirectory(String directoryUri) {
//		this.directory = directoryUri;
//	}
//	
//	@RdfProperty(NS + "fileEncoding")
//	public String getFileEncoding() {
//		return fileEncoding;
//	}
//
//	public void setFileEncoding(String fileEncoding) {
//		this.fileEncoding = fileEncoding;
//	}
//	
//	@RdfProperty(NS + "mapName")
//	public boolean getMapName() {
//		return mapName ;
//	}
//
//	public void setMapName(boolean mapName) {
//		this.mapName = mapName;
//	}

//	public String getFileURI() {
//		return getDirectory() + getModelName();
//	}

	public void clone(Datafile objectToBeCloned) {
		super.clone(objectToBeCloned);
		setFileUrl(objectToBeCloned.getFileUrl());
		//setModelName(objectToBeCloned.getModelName());
//		setDirectory(objectToBeCloned.getDirectory());
//		setMapName(objectToBeCloned.getMapName());
//		setFileEncoding(objectToBeCloned.getFileEncoding());
	}
	
//	public void replicate(Datafile objectToClone) {
//		clone(objectToClone);
//		setId(objectToClone.getId());
//		super.replicate(objectToClone);
//	}
	
	public Datafile createClone() {
		Datafile newObj = new Datafile();
		newObj.clone(this);
		return newObj;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

//	public Datafile createReplica() {
//		Datafile newObj = new Datafile();
//		newObj.replicate(this);
//		return newObj;
//	}
}
