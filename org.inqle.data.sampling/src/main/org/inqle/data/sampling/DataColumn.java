package org.inqle.data.sampling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;

import thewebsemantic.Namespace;

/**
 * Stores info related to a column of data in a LegacyDataTable.
 * 
 * @author David Donohue
 * May 23, 2007
 * 
 * TODO use Arc to represent the uriPath
 * TODO move data-containing fields (valuesSet and valuesList) into DataTable class
 */
@Namespace(RDF.INQLE)
public class DataColumn extends BasicJenabean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2076932473606244248L;

	public static final int DATA_TYPE_UNASSIGNED = -999;

	/** the RDF URI of this column */
	private String columnUri;
	
	/** the label applied to this predicate in the SPARQL query */
	private String queryLabel;
	
	/** the RapidMiner data type, from the edu.udo.cs.yale.tools.Ontology class.
	 * Possible values = Ontology.NOMINAL, Ontology.REAL, Ontology.String */
	private int dataType = DATA_TYPE_UNASSIGNED;
	
	/**
	 * an array containing the path of predicate URIs to this column
	 */
	private String[] uriPath = new String[] {};

	private LinkedHashSet<String> valuesSet = new LinkedHashSet<String>();

	private List<String> valuesList = new ArrayList<String>();
	
	public DataColumn() {}
	
	public DataColumn(String queryLabel, String columnUri) {
		this.queryLabel = queryLabel;
		this.columnUri = columnUri;
		//by default, assume the Arc to this column = a single step (i.e. this column only)
		this.uriPath = new String[] {columnUri};
	}
	
	/**
	 * How many RDF steps were taken from start point to find this minable value
	 * @return the number of steps
	 */
	public int getNumSteps() {
		return uriPath.length;
	}

	public void setDataType(int rapidMinerOntologyDataType) {
		this.dataType = rapidMinerOntologyDataType;
	}
	
	public int getDataType() {
		return this.dataType;
	}
	
	/**
	 * This must be set if the arc of URIs to get to this column 
	 * (from the subject) is more than a single step
	 * 
	 * @return a String array of (predicate) URIs, representing
	 * the steps from the subject to this column
	 */
	public String[] getUriPath() {
		return uriPath;
	}
	
	public String getColumnUri() {
		return this.columnUri;
	}
	
	public void setColumnUri(String columnUri) {
		this.columnUri = columnUri;
	}
	
	/**
	 * @return queryLabel the label used in the SPARQL, which was used to
	 * retrieve this object
	 */
	public String getQueryLabel() {
		return queryLabel;
	}
	
	public void setQueryLabel(String queryLabel) {
		this.queryLabel = queryLabel;
	}

	/**
	 * Get the LinkedHashSet of values that this DataColumn contains
	 * @return
	 */
	public LinkedHashSet<String> getValuesSet() {
		return valuesSet;
	}
	
	public List<String> getValuesList() {
		return valuesList;
	}
	
	/**
	 * Set the LinkedHashSet of values that this DataColumn contains
	 * @param the LinkedHashSet of values
	 */
	public void setValuesSet(LinkedHashSet<String> valuesSet) {
		this.valuesSet = valuesSet;
	}

	public String getColumnIdentifier() {
		return queryLabel + Arrays.asList(uriPath).toString();
	}

	public void setValuesList(List<String> valuesList) {
		this.valuesList = valuesList;
	}

	public void clone(DataColumn objectToClone) {
		setColumnUri(objectToClone.getColumnUri());
		setQueryLabel(objectToClone.getQueryLabel());
		setUriPath(objectToClone.getUriPath());
		setDataType(objectToClone.getDataType());
		super.clone(objectToClone);
	}
	
	public void replicate(DataColumn objectToClone) {
		clone(objectToClone);
		setId(objectToClone.getId());
		super.replicate(objectToClone);
	}
	
	@Override
	public IBasicJenabean createClone() {
		DataColumn newDataColumn = new DataColumn();
		newDataColumn.clone(this);
		return newDataColumn;
	}

	@Override
	public IBasicJenabean createReplica() {
		DataColumn newDataColumn = new DataColumn();
		newDataColumn.replicate(this);
		return newDataColumn;
	}

	public void setUriPath(String[] uriPath) {
		this.uriPath = uriPath;
	}
	
	@Override
	public String toString() {
		return getColumnIdentifier();
	}
}
