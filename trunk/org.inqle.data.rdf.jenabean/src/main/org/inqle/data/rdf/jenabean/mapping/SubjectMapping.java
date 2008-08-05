package org.inqle.data.rdf.jenabean.mapping;

import java.util.Collection;

import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Maps data to a RDFS class.  This mapping is used for importing data
 * into INQLE as RDF.  Each SubjectMapping represents a single subclass of inqle:DataSubject.
 * 
 * The subjectClass should contain either:
 *  * the RDF URI of the (native) RDFS class of the object to which the DataMappings should map, or
 *  * the RDF URI of the inqle:subject of the inqle:Data object, to which the DataMappings shoulr map
 * 
 * subjectInstance is populated with the URI of the instance of this subject class,
 * in cases where this subject mapping represents a object, to which
 * all rows of data within the table will apply.  
 * When subjectInstance is null, then rows have different subjects.
 * In this case, one of these methods will be used to generate the subject
 * URI:
 *  * When subjectUriPrefix is null, INQLE will generate a unique URI,
 *    using prefix inqle:DataSubject
 *  * When subjectUriPrefix is populated and subjectHeader is null,
 *    a random UUID will be appended onto the provided subjectUriPrefix.
 *  * When subjectUriPrefix and subjectHeader are both populated,
 *    the value in the column identified by the header will be 
 *    appended onto the provided subjectUriPrefix.
 * @author David Donohue
 * Aug 4, 2008
 */
public class SubjectMapping extends GlobalJenabean {

	public static final String[] URI_TYPES = {
		"INQLE Generated (always safe)",
		"Your prefix + Random UUID (always safe)",
		"Your prefix + Value from specified column (gets converted into a URI-safe format)"
	};
	
	private Collection<DataMapping> dataMappings;
	private Resource subjectClass;
	private Resource subjectInstance;
	private Resource subjectUriPrefix;
	private String subjectHeader;
	
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

	public Collection<DataMapping> getDataMappings() {
		return dataMappings;
	}

	public void setDataMappings(Collection<DataMapping> dataMappings) {
		this.dataMappings = dataMappings;
	}

	public Resource getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(Resource subjectClass) {
		this.subjectClass = subjectClass;
	}

	public Resource getSubjectInstance() {
		return subjectInstance;
	}

	public void setSubjectInstance(Resource subjectInstance) {
		this.subjectInstance = subjectInstance;
	}

	public Resource getSubjectUriPrefix() {
		return subjectUriPrefix;
	}

	public void setSubjectUriPrefix(Resource subjectUriPrefix) {
		this.subjectUriPrefix = subjectUriPrefix;
	}

	public String getSubjectHeader() {
		return subjectHeader;
	}

	public void setSubjectHeader(String subjectHeader) {
		this.subjectHeader = subjectHeader;
	}

}
