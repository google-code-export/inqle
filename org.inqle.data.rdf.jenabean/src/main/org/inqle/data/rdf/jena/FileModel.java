package org.inqle.data.rdf.jena;

import static org.inqle.data.rdf.jena.AssemblerVocabulary.NS;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

/**
 * from http://jena.sourceforge.net/assembler/assembler-howto.html
 * class ja:FileModel subClassOf ja:NamedModel
 * domainOf ja:directory maxCardinality 1
 * domainOf ja:fileEncoding maxCardinality 1
 * domainOf ja:mapName maxCardinality 1
 * @author David Donohue
 * Jan 9, 2008
 */
@Namespace(NS)
public class FileModel extends NamedModel {
	

	private String directory;
	private String fileEncoding;
	private boolean mapName = false;

	@RdfProperty(NS + "directory")
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directoryUri) {
		this.directory = directoryUri;
	}
	
	@RdfProperty(NS + "fileEncoding")
	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}
	
	@RdfProperty(NS + "mapName")
	public boolean getMapName() {
		return mapName ;
	}

	public void setMapName(boolean mapName) {
		this.mapName = mapName;
	}

	public String getFileURI() {
		return getDirectory() + getModelName();
	}

	public void clone(FileModel objectToBeCloned) {
		super.clone(objectToBeCloned);
		setModelName(objectToBeCloned.getModelName());
		setDirectory(objectToBeCloned.getDirectory());
		setMapName(objectToBeCloned.getMapName());
		setFileEncoding(objectToBeCloned.getFileEncoding());
	}
	
	public void replicate(FileModel objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
	}
	
	@Override
	public FileModel createClone() {
		FileModel newObj = new FileModel();
		newObj.clone(this);
		return newObj;
	}

	@Override
	public FileModel createReplica() {
		FileModel newObj = new FileModel();
		newObj.replicate(this);
		return newObj;
	}
}
