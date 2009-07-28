package org.inqle.data.rdf.jenabean.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDatamodel;
import org.inqle.data.rdf.jenabean.GlobalJenabean;
import org.inqle.data.rdf.jenabean.IUniqueJenabean;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.  It maps terms (like a string of comma-separated 
 * headers in a CSV file) to the collection of DataMappings which map to the table.
 * 
 * A TableMapping object maps to the header line from a CSV file.  
 * It contains 1 or more DataMappings, each specifying how to map the data from a single column.
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
	
	private List<SubjectMapping> subjectMappings = new ArrayList<SubjectMapping>();
	
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
		setSubjectMappings(objectToBeCloned.getSubjectMappings());
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

	public Collection<SubjectMapping> getSubjectMappings() {
		return subjectMappings;
	}

	public void setSubjectMappings(Collection<SubjectMapping> subjectMappings) {
		this.subjectMappings = new ArrayList<SubjectMapping>();
		this.subjectMappings.addAll(subjectMappings);
	}
	
	public void addSubjectMapping(SubjectMapping subjectMapping) {
		subjectMappings.add(subjectMapping);
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

	/**
	 * Returns the first DataMapping found which matches the specified predicate
	 * @param dateProperty
	 * @return
	 */
	public DataMapping findMappingByPredicate(String predicate) {
		for (DataMapping dataMapping: getDataMappings()) {
			if ((dataMapping.getMapsPredicate()).toString().equals(predicate)) {
				return dataMapping;
			}
		}
		return null;
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