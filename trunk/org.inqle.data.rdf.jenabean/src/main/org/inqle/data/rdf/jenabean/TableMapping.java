package org.inqle.data.rdf.jenabean;

import java.net.URI;
import java.util.Collection;

import org.inqle.data.rdf.RDF;

import thewebsemantic.Namespace;

/**
 * This class is used for importing Data.  It maps terms (like a string of comma-separated 
 * headers in a CSV file) to the collection of DataMappings which map to the table.
 * 
 * A TableMapping object maps to the header line from a CSV file.  
 * It also contains the URI of the subclass of inqle:Data that represents this table. 
 * It contains 1 or more DataMappings, each specifying how to map the data from a single column.
 * 
 * @author David Donohue
 * Jul 12, 2008
 * 
 * TODO extend a base class, which does not have name & description fields
 */
@Namespace(RDF.INQLE)
public class TableMapping extends GlobalJenabean {

	private String mappedText;
	
	/**
	 * ID of the subclass of inqle:Data which represents the table.  Needed because the subclass will be the only
	 * location where some info si stored.
	 * TODO consider remove this
	 */
	private String mappedDataSubclassId;
	
	/**
	 * ID of the dataset where resides the subclass of inqle:Data which represents the table.
	 * This seems to be of local usefulness only?
	 * TODO consider remove this
	 */
	private String mappedDataSubclassDatasetId;
	
	private Collection<DataMapping> dataMappings;
	
	public String getStringRepresentation() {
		String s = getClass().toString() + " {\n";
		s += "[mappedText=" + mappedText + "]\n";
		s += "[mappedDataSubclassId=" + mappedDataSubclassId + "]\n";
		//TODO ensure these mappings are sorted reproducibly each time they are iterated.
		for(DataMapping dataMapping: getDataMappings()) {
			s += "[dataMapping=" + dataMapping + "]\n";
		}
		s += "}";
		return s;
	}

	public void clone(TableMapping objectToBeCloned) {
		setMappedText(objectToBeCloned.getMappedText());
		setDataMappings(objectToBeCloned.getDataMappings());
		super.clone(objectToBeCloned);
	}
	
	public IBasicJenabean createClone() {
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

	public Collection<DataMapping> getDataMappings() {
		return dataMappings;
	}

	public void setDataMappings(Collection<DataMapping> dataMappings) {
		this.dataMappings = dataMappings;
	}

	public String getMappedDataSubclassId() {
		return mappedDataSubclassId;
	}

	public void setMappedDataSubclassId(String mappedDataSubclassId) {
		this.mappedDataSubclassId = mappedDataSubclassId;
	}

	public String getMappedDataSubclassDatasetId() {
		return mappedDataSubclassDatasetId;
	}

	public void setMappedDataSubclassDatasetId(String mappedDataSubclassDatasetId) {
		this.mappedDataSubclassDatasetId = mappedDataSubclassDatasetId;
	}	

}
