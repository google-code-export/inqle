package org.inqle.data.rdf.jenabean.mapping;

import java.net.URI;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.GlobalJenabean;

import com.hp.hpl.jena.rdf.model.Resource;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.  It maps terms (like headers in a CSV file) to 
 * the respective RDF property.  
 * A DataMapping can be used to import all values from a single
 * column in a CSV file.
 * 
 * For attribute mapsSubjectType, a DataMapping object can have 1 of these values:
 *  * inqle:DataSubject
 *    The DataMapping maps to the native subject instance, 
 *  * inqle:Data
 *    The DataMapping maps to an inqle:Data instance, which is about the native subject instance
 *    
 * The attribute mapsValue is is only populated in cases where the subject has a global 
 * value for all rows of the table.  In this case, it contains the literal value of the 
 * mapped object.
 * 
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
	private Resource mapsSubjectType;
	
//	private Resource mapsSubjectClass;
//	private Resource mapsSubjectInstance;
//	private Resource mapsDataAboutSubjectClass;
//	private Resource mapsDataAboutSubjectInstance;
	private Object mapsValue;
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[mapsHeader=" + mapsHeader + "]\n";
		s += "[mapsPredicate=" + mapsPredicate.toString() + "]\n";
		s += "[mapsSubjectType=" + mapsSubjectType.toString() + "]\n";
//		s += "[mapsSubjectClass=" + mapsSubjectClass + "]\n";
//		s += "[mapsSubjectInstance=" + mapsSubjectInstance + "]\n";
//		s += "[mapsDataAboutSubjectClass=" + mapsDataAboutSubjectClass + "]\n";
//		s += "[mapsDataAboutSubjectInstance=" + mapsDataAboutSubjectInstance + "]\n";
		s += "[mapsValue=" + mapsValue + "]\n";
		s += "}";
		return s;
	}

	public void clone(DataMapping objectToBeCloned) {
		setMapsHeader(objectToBeCloned.getMapsHeader());
		setMapsPredicate(objectToBeCloned.getMapsPredicate());
		setMapsSubjectType(objectToBeCloned.getMapsSubjectType());
		setMapsValue(objectToBeCloned.getMapsValue());
//		setMapsSubjectClass(objectToBeCloned.getMapsSubjectClass());
//		setMapsSubjectInstance(objectToBeCloned.getMapsSubjectInstance());
//		setMapsDataAboutSubjectClass(objectToBeCloned.getMapsDataAboutSubjectClass());
//		setMapsDataAboutSubjectInstance(objectToBeCloned.getMapsDataAboutSubjectInstance());
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

//	public Resource getMapsSubjectClass() {
//		return mapsSubjectClass;
//	}
//
//	public void setMapsSubjectClass(Resource mapsSubjectClass) {
//		this.mapsSubjectClass = mapsSubjectClass;
//	}
//
//	public Resource getMapsSubjectInstance() {
//		return mapsSubjectInstance;
//	}
//
//	public void setMapsSubjectInstance(Resource mapsSubjectInstance) {
//		this.mapsSubjectInstance = mapsSubjectInstance;
//	}
//
//	public Resource getMapsDataAboutSubjectClass() {
//		return mapsDataAboutSubjectClass;
//	}
//
//	public void setMapsDataAboutSubjectClass(Resource mapsDataAboutSubjectClass) {
//		this.mapsDataAboutSubjectClass = mapsDataAboutSubjectClass;
//	}
//
//	public Resource getMapsDataAboutSubjectInstance() {
//		return mapsDataAboutSubjectInstance;
//	}
//
//	public void setMapsDataAboutSubjectInstance(Resource mapsDataAboutSubjectInstance) {
//		this.mapsDataAboutSubjectInstance = mapsDataAboutSubjectInstance;
//	}

	public Object getMapsValue() {
		return mapsValue;
	}

	public void setMapsValue(Object mapsValue) {
		this.mapsValue = mapsValue;
	}

	public Resource getMapsSubjectType() {
		return mapsSubjectType;
	}

	public void setMapsSubjectType(Resource mapsSubjectType) {
		this.mapsSubjectType = mapsSubjectType;
	}

	

}
