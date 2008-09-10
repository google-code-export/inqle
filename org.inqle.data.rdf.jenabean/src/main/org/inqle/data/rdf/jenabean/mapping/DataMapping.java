package org.inqle.data.rdf.jenabean.mapping;

import java.net.URI;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.GlobalJenabean;

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

	public static final URI MAPPING_TYPE_DATA = URI.create(RDF.DATA);
	public static final URI MAPPING_TYPE_SUBJECT = URI.create(RDF.SUBJECT);
	
	public static final String MAPPING_DATASET_ROLE_ID = "org.inqle.datasets.mapping";
	private String mapsHeader;
	private URI mapsPredicate;
	private URI mapsSubjectType;
	
//	private URI mapsSubjectClass;
//	private URI mapsSubjectInstance;
//	private URI mapsDataAboutSubjectClass;
//	private URI mapsDataAboutSubjectInstance;
	private Object mapsValue;
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[mapsHeader=" + mapsHeader + "]\n";
		s += "[mapsPredicate=" + mapsPredicate.toString() + "]\n";
		s += "[mapsSubjectType=" + mapsSubjectType.toString() + "]\n";
		s += "[mapsValue=" + mapsValue + "]\n";
		s += "}";
		return s;
	}

	public void clone(DataMapping objectToBeCloned) {
		setMapsHeader(objectToBeCloned.getMapsHeader());
		setMapsPredicate(objectToBeCloned.getMapsPredicate());
		setMapsSubjectType(objectToBeCloned.getMapsSubjectType());
		setMapsValue(objectToBeCloned.getMapsValue());
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

	public URI getMapsPredicate() {
		return mapsPredicate;
	}

	public void setMapsPredicate(URI mapsPredicate) {
		this.mapsPredicate = mapsPredicate;
	}

	public Object getMapsValue() {
		return mapsValue;
	}

	public void setMapsValue(Object mapsValue) {
		this.mapsValue = mapsValue;
	}

	public URI getMapsSubjectType() {
		return mapsSubjectType;
	}

	public void setMapsSubjectType(URI mapsSubjectType) {
		this.mapsSubjectType = mapsSubjectType;
	}

}
