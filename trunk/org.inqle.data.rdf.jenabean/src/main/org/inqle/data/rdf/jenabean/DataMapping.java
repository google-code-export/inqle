package org.inqle.data.rdf.jenabean;

import java.net.URI;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;

import com.hp.hpl.jena.rdf.model.Resource;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.  It maps terms (like headers in a CSV file) to 
 * the respective class or data class.  A DataMapping is used to import all values from a single
 * column in a CSV file.
 * 
 * A DataMapping object can specify that a value should populate an RDF attribute, which
 * is in any of 4 types of locations
 * (1) an anonymous object of a native class, like foaf:Person
 * In this case, the attribute mapsOwlClass is populated with the URI of the class
 * (2) an specific object of a native class, like foaf:Person#David_Donohue
 * In this case, the attribute mapsOwlClass is populated with the URI of the class,
 * The attribute mapsOwlObject is populated with the URI of the object
 * (3) An anonymous object of a subclass of inqle:Data, which has inqle:subject = an anonymous
 * object of a native class.
 * In this case, the attribute mapsDataAboutOwlClass is populated with the URI of the native class, 
 * which the inqle:Data object is about.
 * (4) An anonymous object of a subclass of inqle:Data, which has inqle:subject a specific object
 * of a native class.
 * In this case, the attribute mapsDataAboutOwlClass is populated with the URI of the native class, 
 * which the inqle:Data object is about.
 * The attribute mapsDataAboutOwlObject is populated with the URI of the object of class specified by 
 * mapsDataAboutOwlClass.
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
	private String mapsText;
	private Resource mapsPredicate;
	private Resource mapsOwlClass;
	private Resource mapsOwlObject;
	private Resource mapsDataAboutOwlClass;
	private Resource mapsDataAboutOwlObject;
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[mapsText=" + mapsText + "]\n";
		s += "[mapsPredicate=" + mapsPredicate + "]\n";
		s += "[mapsOwlClass=" + mapsOwlClass + "]\n";
		s += "[mapsOwlObject=" + mapsOwlObject + "]\n";
		s += "[mapsDataAboutOwlClass=" + mapsDataAboutOwlClass + "]\n";
		s += "[mapsDataAboutOwlObject=" + mapsDataAboutOwlObject + "]\n";
		s += "}";
		return s;
	}

	public void clone(DataMapping objectToBeCloned) {
		setMapsText(objectToBeCloned.getMapsText());
		setMapsPredicate(objectToBeCloned.getMapsPredicate());
		setMapsOwlClass(objectToBeCloned.getMapsOwlClass());
		setMapsOwlObject(objectToBeCloned.getMapsOwlObject());
		setMapsDataAboutOwlClass(objectToBeCloned.getMapsDataAboutOwlClass());
		setMapsDataAboutOwlObject(objectToBeCloned.getMapsDataAboutOwlObject());
		super.clone(objectToBeCloned);
	}
	
	public DataMapping createClone() {
		DataMapping dataMapping = new DataMapping();
		dataMapping.clone(this);
		return dataMapping;
	}

	public String getMapsText() {
		return mapsText;
	}

	public void setMapsText(String mapsText) {
		this.mapsText = mapsText;
	}

	public Resource getMapsPredicate() {
		return mapsPredicate;
	}

	public void setMapsPredicate(Resource mapsPredicate) {
		this.mapsPredicate = mapsPredicate;
	}

	public Resource getMapsOwlClass() {
		return mapsOwlClass;
	}

	public void setMapsOwlClass(Resource mapsOwlClass) {
		this.mapsOwlClass = mapsOwlClass;
	}

	public Resource getMapsOwlObject() {
		return mapsOwlObject;
	}

	public void setMapsOwlObject(Resource mapsOwlObject) {
		this.mapsOwlObject = mapsOwlObject;
	}

	public Resource getMapsDataAboutOwlClass() {
		return mapsDataAboutOwlClass;
	}

	public void setMapsDataAboutOwlClass(Resource mapsDataAboutOwlClass) {
		this.mapsDataAboutOwlClass = mapsDataAboutOwlClass;
	}

	public Resource getMapsDataAboutOwlObject() {
		return mapsDataAboutOwlObject;
	}

	public void setMapsDataAboutOwlObject(Resource mapsDataAboutOwlObject) {
		this.mapsDataAboutOwlObject = mapsDataAboutOwlObject;
	}

	

}
