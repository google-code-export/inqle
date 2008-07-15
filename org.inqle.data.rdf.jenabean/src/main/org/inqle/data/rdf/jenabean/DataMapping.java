package org.inqle.data.rdf.jenabean;

import java.net.URI;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.  It maps terms (like headers in a CSV file) to 
 * the respective class or data class.  A DataMapping is used to import all values from a single
 * column in a CSV file.
 * 
 * A DataMapping object can specify that a value should populate an RDF attribute, which
 * is in any of 4 types of locations
 * (1) an anonymous object of a native class, like foaf:Person
 * In this case, the attribute uriOfMappedNativeClass is populated with the URI of the class
 * (2) an specific object of a native class, like foaf:Person#David_Donohue
 * In this case, the attribute uriOfMappedNativeClass is populated with the URI of the class,
 * The attribute uriOfMappedNativeObject is populated with the URI of the object
 * (3) An anonymous object of a subclass of inqle:Data, which inqle:isAbout an anonymous
 * object of a native class.
 * In this case, the attribute uriOfWrappedClass is populated with the URI of the native class, 
 * which the inqle:Data object isAbout.
 * (4) An anonymous object of a subclass of inqle:Data, which inqle:isAbout a specific object
 * of a native class.
 * In this case, the attribute uriOfWrappedClass is populated with the URI of the native class, 
 * which the inqle:Data object isAbout.
 * The attribute uriOfWrappedObject is populated with the URI of the object of class specified by 
 * uriOfWrappedClass.
 * 
 * @author David Donohue
 * Jul 12, 2008
 * 
 * TODO extend a base class, which does not have name & description fields
 */
@TargetDataset(DataMapping.MAPPING_DATASET_ROLE_ID)
@Namespace(RDF.INQLE)
public class DataMapping extends GlobalJenabean {

	public static final String MAPPING_DATASET_ROLE_ID = "org.inqle.datasets.mapping";
	private String mappedText;
	private URI mappedPredicate;
	private URI uriOfMappedNativeClass;
	private URI uriOfMappedNativeObject;
	private URI uriOfWrappedClass;
	private URI uriOfWrappedObject;
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[mappedText=" + mappedText + "]\n";
		s += "[mappedPredicate=" + mappedPredicate + "]\n";
		s += "[uriOfMappedNativeClass=" + uriOfMappedNativeClass + "]\n";
		s += "[uriOfMappedNativeObject=" + uriOfMappedNativeObject + "]\n";
		s += "[uriOfWrappedClass=" + uriOfWrappedClass + "]\n";
		s += "[uriOfWrappedObject=" + uriOfWrappedObject + "]\n";
		s += "}";
		return s;
	}

	public void clone(DataMapping objectToBeCloned) {
		setMappedText(objectToBeCloned.getMappedText());
		setMappedPredicate(objectToBeCloned.getMappedPredicate());
		setUriOfMappedNativeClass(objectToBeCloned.getUriOfMappedNativeClass());
		setUriOfMappedNativeObject(objectToBeCloned.getUriOfMappedNativeObject());
		setUriOfWrappedClass(objectToBeCloned.getUriOfWrappedClass());
		setUriOfWrappedObject(objectToBeCloned.getUriOfWrappedObject());
		super.clone(objectToBeCloned);
	}
	
	public DataMapping createClone() {
		DataMapping dataMapping = new DataMapping();
		dataMapping.clone(this);
		return dataMapping;
	}

	public String getMappedText() {
		return mappedText;
	}

	public void setMappedText(String mappedText) {
		this.mappedText = mappedText;
	}

	public URI getMappedPredicate() {
		return mappedPredicate;
	}

	public void setMappedPredicate(URI mappedPredicate) {
		this.mappedPredicate = mappedPredicate;
	}

	public URI getUriOfMappedNativeClass() {
		return uriOfMappedNativeClass;
	}

	public void setUriOfMappedNativeClass(URI uriOfMappedNativeClass) {
		this.uriOfMappedNativeClass = uriOfMappedNativeClass;
	}

	public URI getUriOfMappedNativeObject() {
		return uriOfMappedNativeObject;
	}

	public void setUriOfMappedNativeObject(URI uriOfMappedNativeObject) {
		this.uriOfMappedNativeObject = uriOfMappedNativeObject;
	}

	public URI getUriOfWrappedClass() {
		return uriOfWrappedClass;
	}

	public void setUriOfWrappedClass(URI uriOfWrappedClass) {
		this.uriOfWrappedClass = uriOfWrappedClass;
	}

	public URI getUriOfWrappedObject() {
		return uriOfWrappedObject;
	}

	public void setUriOfWrappedObject(URI uriOfWrappedObject) {
		this.uriOfWrappedObject = uriOfWrappedObject;
	}

	

}
