package org.inqle.data.rdf.jenabean.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.GlobalCloneableJenabean;
import org.inqle.data.rdf.jenabean.IUniqueJenabean;
import org.inqle.data.rdf.jenabean.UniqueCloneableJenabean;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.  It maps terms (like a string of comma-separated 
 * headers in a CSV file) to a SubjectMapping and to the collection of DataMappings which map to the table.
 * 
 * A TableMapping object maps to the header line from a CSV file.  
 * It contains 1 SubjectMapping and 1 or more DataMappings, 
 * each specifying how to map the data from a single column.
 * 
 * @author David Donohue
 * Jul 12, 2008
 * 
 * TODO extend a base class, which does not have name & description fields
 */
@TargetDatamodel(DataMapping.MAPPING_DATASET_ROLE_ID)
@Namespace(RDF.INQLE)
//public class TableMapping extends GlobalJenabean {
public class TableMapping extends SubjectMapping {
	
	private String mappedText;
	
	private SubjectMapping subjectMapping = null;
	
//	public String getStringRepresentation() {
//		String s = getClass().toString() + " {\n";
//		s += "[mappedText=" + mappedText + "]\n";
//		//s += "[mappedDataSubclassId=" + mappedDataSubclassId + "]\n";
//		//TODO ensure these mappings are sorted reproducibly each time they are iterated.
//		for(SubjectMapping subjectMapping: getSubjectMappings()) {
//			s += "[subjectMapping=" + subjectMapping + "]\n";
//		}
//		s += "}";
//		return s;
//	}

	public void clone(TableMapping objectToBeCloned) {
		setMappedText(objectToBeCloned.getMappedText());
		setSubjectMapping(objectToBeCloned.getSubjectMapping());
		super.clone(objectToBeCloned);
	}
	
	public TableMapping createClone() {
		TableMapping dataMapping = new TableMapping();
		dataMapping.clone(this);
		return dataMapping;
	}

	public String getMappedText() {
		return mappedText;
	}

	public void setMappedText(String mappedText) {
		this.mappedText = mappedText;
	}

	public SubjectMapping getSubjectMapping() {
		return subjectMapping;
	}

	public void setSubjectMapping(SubjectMapping subjectMapping) {
		this.subjectMapping = subjectMapping;
	}

	public void replicate(TableMapping objectToBeReplicated) {
		clone(objectToBeReplicated);
		setId(objectToBeReplicated.getId());
	}
	
	public TableMapping createReplica() {
		TableMapping dataMapping = new TableMapping();
		dataMapping.replicate(this);
		return dataMapping;
	}

//	public String getMappedDataSubclassId() {
//		return mappedDataSubclassId;
//	}

//	public void setMappedDataSubclassId(String mappedDataSubclassId) {
//		this.mappedDataSubclassId = mappedDataSubclassId;
//	}
//
//	public String getMappedDataSubclassDatasetId() {
//		return mappedDataSubclassDatasetId;
//	}
//
//	public void setMappedDataSubclassDatasetId(String mappedDataSubclassDatasetId) {
//		this.mappedDataSubclassDatasetId = mappedDataSubclassDatasetId;
//	}	

}
