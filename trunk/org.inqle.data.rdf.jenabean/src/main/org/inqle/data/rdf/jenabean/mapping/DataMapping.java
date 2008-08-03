package org.inqle.data.rdf.jenabean.mapping;

import java.net.URI;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.GlobalJenabean;

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
 * In this case, the attribute mapsSubjectClass is populated with the URI of the class
 * (2) an specific object of a native class, like foaf:Person#David_Donohue
 * In this case, the attribute mapsSubjectClass is populated with the URI of the class,
 * The attribute mapsSubjectInstance is populated with the URI of the object
 * (3) An anonymous object of a subclass of inqle:Data, which has inqle:subject = an anonymous
 * object of a native class.
 * In this case, the attribute mapsDataAboutSubjectClass is populated with the URI of the native class, 
 * which the inqle:Data object is about.
 * (4) An anonymous object of a subclass of inqle:Data, which has inqle:subject a specific object
 * of a native class.
 * In this case, the attribute mapsDataAboutSubjectClass is populated with the URI of the native class, 
 * which the inqle:Data object is about.
 * The attribute mapsDataAboutSubjectInstance is populated with the URI of the object of class specified by 
 * mapsDataAboutSubjectClass.
 * 
 * In any case, the attribute mapsValue is populated with the literal value of the mapped object.
 * This is only populated in cases where the subject is global across the entire table
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
	private String mapsHeader;
	private Resource mapsPredicate;
	private Resource mapsSubjectClass;
	private Resource mapsSubjectInstance;
	private Resource mapsDataAboutSubjectClass;
	private Resource mapsDataAboutSubjectInstance;
	private Object mapsValue;
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[mapsHeader=" + mapsHeader + "]\n";
		s += "[mapsPredicate=" + mapsPredicate + "]\n";
		s += "[mapsSubjectClass=" + mapsSubjectClass + "]\n";
		s += "[mapsSubjectInstance=" + mapsSubjectInstance + "]\n";
		s += "[mapsDataAboutSubjectClass=" + mapsDataAboutSubjectClass + "]\n";
		s += "[mapsDataAboutSubjectInstance=" + mapsDataAboutSubjectInstance + "]\n";
		s += "[mapsValue=" + mapsValue + "]\n";
		s += "}";
		return s;
	}

	public void clone(DataMapping objectToBeCloned) {
		setMapsHeader(objectToBeCloned.getMapsHeader());
		setMapsPredicate(objectToBeCloned.getMapsPredicate());
		setMapsSubjectClass(objectToBeCloned.getMapsSubjectClass());
		setMapsSubjectInstance(objectToBeCloned.getMapsSubjectInstance());
		setMapsDataAboutSubjectClass(objectToBeCloned.getMapsDataAboutSubjectClass());
		setMapsDataAboutSubjectInstance(objectToBeCloned.getMapsDataAboutSubjectInstance());
		super.clone(objectToBeCloned);
	}
	
	public DataMapping createClone() {
		DataMapping dataMapping = new DataMapping();
		dataMapping.clone(this);
		return dataMapping;
	}

	public String getMapsHeader() {
		return mapsHeader;
	}

	public void setMapsHeader(String mapsText) {
		this.mapsHeader = mapsText;
	}

	public Resource getMapsPredicate() {
		return mapsPredicate;
	}

	public void setMapsPredicate(Resource mapsPredicate) {
		this.mapsPredicate = mapsPredicate;
	}

	public Resource getMapsSubjectClass() {
		return mapsSubjectClass;
	}

	public void setMapsSubjectClass(Resource mapsSubjectClass) {
		this.mapsSubjectClass = mapsSubjectClass;
	}

	public Resource getMapsSubjectInstance() {
		return mapsSubjectInstance;
	}

	public void setMapsSubjectInstance(Resource mapsSubjectInstance) {
		this.mapsSubjectInstance = mapsSubjectInstance;
	}

	public Resource getMapsDataAboutSubjectClass() {
		return mapsDataAboutSubjectClass;
	}

	public void setMapsDataAboutSubjectClass(Resource mapsDataAboutSubjectClass) {
		this.mapsDataAboutSubjectClass = mapsDataAboutSubjectClass;
	}

	public Resource getMapsDataAboutSubjectInstance() {
		return mapsDataAboutSubjectInstance;
	}

	public void setMapsDataAboutSubjectInstance(Resource mapsDataAboutSubjectInstance) {
		this.mapsDataAboutSubjectInstance = mapsDataAboutSubjectInstance;
	}

	public Object getMapsValue() {
		return mapsValue;
	}

	public void setMapsValue(Object mapsValue) {
		this.mapsValue = mapsValue;
	}

	

}
