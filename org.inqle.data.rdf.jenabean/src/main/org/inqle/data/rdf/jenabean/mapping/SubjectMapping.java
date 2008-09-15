package org.inqle.data.rdf.jenabean.mapping;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.inqle.data.rdf.jenabean.GlobalJenabean;

/**
 * Maps data to a RDFS class.  This mapping is used for importing data
 * into INQLE as RDF.  Each SubjectMapping can generate a single instance of inqle:Data and an 
 * associated instance of inqle:Subject.
 * 
 * SubjectMappings may have a subjectInstance, in which case the mapping points to a single instance.
 * Alternatively, SubjectMappings will point to a new instance for each row of the data table.
 * 
 * subjectInstance is populated with the URI of the instance of this subject class,
 * in cases where this subject mapping represents a object, to which
 * all rows of data within the table will apply.  
 * When subjectInstance is null, then rows have different subjects.
 * In this case, one of these methods will be used to generate the subject
 * URI:
 *  * When subjectUriPrefix is null, INQLE will generate a unique URI
 *  * When subjectUriPrefix is populated and subjectHeader is null,
 *    a random UUID will be appended onto the provided subjectUriPrefix.
 *  * When subjectUriPrefix and subjectHeader are both populated,
 *    the value in the column identified by the header will be 
 *    appended onto the provided subjectUriPrefix.
 * @author David Donohue
 * Aug 4, 2008
 */
public class SubjectMapping extends GlobalJenabean {

	public static final String URI_TYPE_INQLE_GENERATED = "INQLE-generated";
	public static final String URI_TYPE_RANDOM_UUID = "URI prefix + random ID";
	public static final String URI_TYPE_COLUMN_VALUE = "URI prefix + value from specified column";
	
	public static final String[] SUBJECT_URI_CREATION_METHODS = {
		URI_TYPE_INQLE_GENERATED,
		URI_TYPE_RANDOM_UUID,
		URI_TYPE_COLUMN_VALUE
	};
	public static java.util.List<String> SUBJECT_URI_CREATION_METHOD_LIST = Arrays.asList(SUBJECT_URI_CREATION_METHODS);
	
	private List<DataMapping> dataMappings = new ArrayList<DataMapping>();
	private URI subjectClass;
	private URI subjectInstance;
	private URI subjectUriPrefix;
	private String subjectHeader;
	private int subjectUriType;
	
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[subjectClass=" + subjectClass.toString() + "]\n";
		s += "[subjectInstance=" + subjectInstance.toString() + "]\n";
		//TODO ensure these mappings are sorted reproducibly each time they are iterated.
		for(DataMapping dataMapping: getDataMappings()) {
			s += "[dataMapping=" + dataMapping + "]\n";
		}
		s += "}";
		return s;
	}

	public void clone(SubjectMapping objectToBeCloned) {
		setSubjectClass(objectToBeCloned.getSubjectClass());
		setSubjectInstance(objectToBeCloned.getSubjectInstance());
		setDataMappings(objectToBeCloned.getDataMappings());
		super.clone(objectToBeCloned);
	}
	
	public SubjectMapping createClone() {
		SubjectMapping subjectMapping = new SubjectMapping();
		subjectMapping.clone(this);
		return subjectMapping;
	}

	public void addDataMapping(DataMapping dataMapping) {
		dataMappings.add(dataMapping);
	}
	
	public Collection<DataMapping> getDataMappings() {
		return dataMappings;
	}

	public void setDataMappings(Collection<DataMapping> dataMappings) {
		this.dataMappings = new ArrayList<DataMapping>();
		this.dataMappings.addAll(dataMappings);
	}

	public URI getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(URI subjectClass) {
		this.subjectClass = subjectClass;
	}

	public URI getSubjectInstance() {
		return subjectInstance;
	}

	public void setSubjectInstance(URI subjectInstance) {
		this.subjectInstance = subjectInstance;
	}

	public URI getSubjectUriPrefix() {
		return subjectUriPrefix;
	}

	public void setSubjectUriPrefix(URI subjectUriPrefix) {
		this.subjectUriPrefix = subjectUriPrefix;
	}

	public String getSubjectHeader() {
		return subjectHeader;
	}

	public void setSubjectHeader(String subjectHeader) {
		this.subjectHeader = subjectHeader;
	}

	public int getSubjectUriType() {
		return subjectUriType;
	}

	public void setSubjectUriType(int subjectUriType) {
		this.subjectUriType = subjectUriType;
	}
	
	public static int getSubjectUriCreationIndex(String label) {
		return SUBJECT_URI_CREATION_METHOD_LIST.indexOf(label);
	}

}
