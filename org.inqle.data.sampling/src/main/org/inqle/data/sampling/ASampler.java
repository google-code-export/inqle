package org.inqle.data.sampling;

import java.util.Collection;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.TargetDataset;
import org.inqle.data.rdf.jenabean.UniqueJenabean;

import thewebsemantic.Namespace;

/**
 * This is a basic, abstract implementation of the ISampler 
 * interface.  It is intended to be subclassed by new Sampler algorithms.
 * New implementations should:
 *  * to enable persisting your class to RDF store,
 *    add a Namespace annotation to the class as in
 *    @Namespace("http://my.namespace.org")
 *    public class MySampler extends ASampler
 *  * add any new fields and associated getters and setters using
 *    the Javabeans convention
 *  * override createClone(): First call super.createClone() to create new object,
 *    then add any new fields to your clone
 *  * override execute(): perform all steps to populate the resultDataTable field with
 *    a final result
 * @author David Donohue
 * Feb 29, 2008
 */
@TargetDataset(ISampler.SAMPLER_DATASET)
@Namespace(RDF.INQLE)
public abstract class ASampler extends UniqueJenabean implements ISampler {

	protected Collection<String> availableNamedModels;
//	protected DataColumn[] dataColumns;
	//protected DataColumn labelDataColumn;
	//protected DataTable resultDataTable = new DataTable();
	protected Collection<String> selectedNamedModels;
//	protected DataColumn subjectDataColumn;
	//protected String id;
	//protected Dictionary<?, ?> properties;
//	private String name;
	
	/**
	 * Add all field values from the provided template sampler to this sampler,
	 * except the ID field
	 * @param sampler the provided sampler
	 */
	public void clone(ISampler templateSampler) {
//		setAvailableNamedModels(templateSampler.getAvailableNamedModels());
		//setDataColumns(templateSampler.getDataColumns());
		//setLabelDataColumn(templateSampler.getLabelDataColumn());
		//setResultDataTable(templateSampler.getResultDataTable());
		setSelectedNamedModels(templateSampler.getSelectedNamedModels());
		//setSubjectDataColumn(templateSampler.getSubjectDataColumn());
		//setProperties(templateSampler.getProperties());
		//setName(templateSampler.getName());
		super.clone(templateSampler);
	}
	
	public void replicate(ISampler objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
	}

//	public Collection<String> getAvailableNamedModels() {
//		return availableNamedModels;
//	}

//	public DataColumn[] getDataColumns() {
//		return dataColumns;
//	}

//	public DataColumn getLabelDataColumn() {
//		return labelDataColumn;
//	}

//	public Dictionary<?, ?> getProperties() {
//		return properties;
//	}

//	public DataTable getResultDataTable() {
//		return resultDataTable;
//	}

	public Collection<String> getSelectedNamedModels() {
		return selectedNamedModels;
	}

//	public DataColumn getSubjectDataColumn() {
//		return subjectDataColumn;
//	}

//	/**
//	 * (Called prior to saving the object).  Remove all values not desired when saving this object
//	 */
//	public void removeInterimData() {
//		availableNamedModels = null;
//		dataColumns = null;
//		resultDataTable = null;
//	}

//	public void setAvailableNamedModels(Collection<String> availableNamedModels) {
//		this.availableNamedModels = availableNamedModels;
//	}

//	public void setDataColumns(DataColumn[] dataColumns) {
//		this.dataColumns = dataColumns;
//	}

//	public void setLabelDataColumn(DataColumn labelDataColumn) {
//		this.labelDataColumn = labelDataColumn;
//	}

//	public void setResultDataTable(DataTable resultDataTable) {
//		this.resultDataTable = resultDataTable;
//	}

	public void setSelectedNamedModels(Collection<String> selectedNamedModels) {
		this.selectedNamedModels = selectedNamedModels;
	}

//	public void setSubjectDataColumn(DataColumn subjectDataColumn) {
//		this.subjectDataColumn = subjectDataColumn;
//	}

//	public void setProperties(Dictionary<?, ?> properties) {
//		this.properties = properties;
//	}

	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getName();
		}
		return name;
	}

//	public void setName(String name) {
//		this.name = name;		
//	}

//	/**
//	 * Override to perform all steps of this sampler
//	 */
//	public DataTable execute(Persister persister) {
//		return null;
//	}

}
