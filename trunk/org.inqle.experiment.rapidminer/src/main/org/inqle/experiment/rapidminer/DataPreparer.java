package org.inqle.experiment.rapidminer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.sampling.DataCell;
import org.inqle.data.sampling.DataColumn;
import org.inqle.data.sampling.DataTable;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.NominalMapping;
import com.rapidminer.example.table.PolynominalMapping;
import com.rapidminer.tools.Ontology;

public class DataPreparer {

	private DataTable dataTable;

	public DataPreparer(DataTable dataTable) {
		this.dataTable = dataTable;
	}
	
	/** the default maximum ratio of number of distinct values for a non-numeric data column's values to be considered nominal (otherwise it is a string) */
	public static final int DEFAULT_MAX_NOMINAL_VALUES = 500;
	
	/** the default minimum ratio of number of rows over distinct values for a non-numeric data column's values to be considered nominal (otherwise it is a string)*/
	public static final double DEFAULT_MIN_NOMINAL_RATIO = 1.01;
	
	private int maxCountForNominal = DEFAULT_MAX_NOMINAL_VALUES;
	private double minRowsToCountRatio = DEFAULT_MIN_NOMINAL_RATIO;

	private List<List<String>> valuesByColumn;
	
	private static Logger log = Logger.getLogger(DataPreparer.class);
	
	public void setMaxCountForNominal(int num) {
		this.maxCountForNominal = num;
	}
	
	public void setMinRowsToCountRatio(int num) {
		this.minRowsToCountRatio = num;
	}
	
	/**
	 * Given an inqle.rdf.data.DataTable, converts to a edu.udo.cs.yale.example.MemoryExampleTable
	 * @param dataTable the DataTable of data to convert
	 * @return table the MemoryExampleTable of data
	 */
	public MemoryExampleTable createExampleTable() {
		

		//Attribute label = AttributeFactory.createAttribute("label", Ontology.NOMINAL));
		//attributes.add(label);
		//create table
		List<Attribute> attributes = processColumnsIntoAttributes();
		MemoryExampleTable table = new MemoryExampleTable(attributes);
		
		//loop through rows of the DataTable, filling the table
		for (int rowIndex = 0; rowIndex < dataTable.getRows().size(); rowIndex++) {//loop thru each row
			double[] data = new double[attributes.size()];

			List<DataCell> row = dataTable.getRow(rowIndex);
			//loop thru each column
			for (int colIndex = 0; colIndex < dataTable.getColumns().size(); colIndex++) {
				Attribute attribute = attributes.get(colIndex);
				
				DataColumn column = dataTable.getColumn(colIndex);
				DataCell cell = row.get(colIndex);

				double val = 0;
				if (column.getDataType() == Ontology.REAL) {//Numeric
					try {
						val = Double.parseDouble(cell.toString());
					} catch (Exception e) {
						log.error("Unable to cast datum '" + cell.toString() + "' as a number.", e);
					}
				} else {//Nominal or String
					NominalMapping mapping = attribute.getMapping();
					//val = column.getValuesSet().indexOf(cell.toString());
					val = mapping.mapString(cell.toString());
					if (column.getDataType() == Ontology.NOMINAL) {
						log.info("Cell val '" + cell + "' maps to " + val);
					}
				}
				//fill with proper data here
				data[colIndex] = val;
			}//loop back to next column
			
			//add data row
			table.addDataRow(new DoubleArrayDataRow(data));
		}//loop back to next row
		
		return table;
	}
	
	/**
	 * Process the data table, assigning a data type to each column.
	 * @return List of DataMiner Attributes, with mappings to the data
	 */
	public List<Attribute> processColumnsIntoAttributes() {
		//create attribute list
		List<Attribute> attributes = new LinkedList<Attribute>();
		List<DataColumn> columns = dataTable.getColumns();
		int columnCounter = 0;
		for (DataColumn column: columns) {
			int dataType = assignDataType(columnCounter);
			
			Attribute thisAttribute = AttributeFactory.createAttribute(column.getColumnIdentifier(), dataType);
			if (dataType != Ontology.REAL) {
				PolynominalMapping mapping = new PolynominalMapping();
				LinkedHashSet<String> valuesSet = column.getValuesSet();
				int i=0;
				for (String strVal: valuesSet) {
					log.debug("Mapping " + i + ": " + strVal);
					mapping.mapString(strVal.trim());
					i++;
				}
				if (dataType == Ontology.NOMINAL) {
					log.info("Created mapping NOMINAL column: " + column + "\n\rMapping has " + mapping.size() + " values:" + mapping.getValues());
				}
				thisAttribute.setMapping(mapping);
			}
			attributes.add(thisAttribute);
			columnCounter++;
		}
		return attributes;
	}
	
	
	public int assignDataType(int columnIndex) {
		return assignDataType(columnIndex, maxCountForNominal, minRowsToCountRatio);
	}

	/**
	 * For the provided DataTable, assigns a RapidMiner data type to the
	 * column of the specified index
	 * @param columnIndex the index number of the column
	 * @param maxCountForNominal the maximum number of values that can be present for an
	 * attribute to be nominal
	 * @param minRowsToCountRatio the minimum ratio of # of rows to # of distinct values
	 * for a column to be considered to have nominal value
	 * @return dataType the integer data type
	 */
	public int assignDataType(int columnIndex, int maxCountForNominal, double minRowsToCountRatio) {
		DataColumn column = dataTable.getColumn(columnIndex);
		
		log.debug("CCCCCCCCCCCCCCCC assignDataType() for Column " + columnIndex + "=" + column);
		
		//if dataType is already assigned, return
		if (column.getDataType() != DataColumn.DATA_TYPE_UNASSIGNED) {
			return column.getDataType();
		}
		
		List<String> columnValues = getValuesByColumn().get(columnIndex);
		//default to data type STRING
		int dataType = Ontology.STRING;
		
		boolean allNumericSoFar = true;
		LinkedHashSet<String> valuesSet = new LinkedHashSet<String>();
		ArrayList<String> valuesList = new ArrayList<String>();
		for (String val: columnValues) {
			valuesList.add(val);
			valuesSet.add(val);
			if (val.length() > 0) {
				try {
					double dbl = Double.parseDouble(val);
					log.trace(val + " is a number " + dbl);
				} catch (Exception e) {
					log.trace(val + " is NOT a number ");
					allNumericSoFar = false;
				}
			}
		}
		int numDistinctValues = valuesSet.size();
		if (numDistinctValues < 2) {
			dataType = Ontology.SINGLE_VALUE;
			log.debug("column " + column + ": set to SINGLE_VALUE data type = " + Ontology.SINGLE_VALUE);
		} else {
			if (allNumericSoFar) {
				log.debug("column " + column + ": set to REAL data type = " + Ontology.REAL);
				dataType = Ontology.REAL;
			} else {
				if (numDistinctValues < maxCountForNominal && (columnValues.size() / numDistinctValues > minRowsToCountRatio)) {
					dataType = Ontology.NOMINAL;
					log.debug("column " + column + ": set to NOMINAL data type = " + Ontology.NOMINAL);
				} else {
					//otherwise leave as String
					dataType = Ontology.STRING;
					log.debug("column " + column + ": set to STRING data type = " + Ontology.STRING);
				}
			}
		}
		log.debug("ddddddddddddddddd dataType=" + dataType);
		log.debug("vvvvvvvvvvvvvvvvv valuesSet=" + valuesSet);
		column.setValuesSet(valuesSet);
		column.setValuesList(valuesList);
		column.setDataType(dataType);
		return dataType;
	}

	private List<List<String>> getValuesByColumn() {
		if (this.valuesByColumn == null) {
			List<List<String>> valsByColumn = new ArrayList<List<String>>();
			List<List<DataCell>> rows = dataTable.getRows();
			
			//initialize each column
			for (@SuppressWarnings("unused")
			DataColumn column: dataTable.getColumns()) {
				valsByColumn.add(new ArrayList<String>());
			}
			
			//loop thru each row
			for (List<DataCell> row: rows) {
				int colIndex = 0;
				//loop thru each column
				for (DataCell cell: row) {
					valsByColumn.get(colIndex).add(cell.toString());
					colIndex++;
				}
			}
			this.valuesByColumn = valsByColumn;
		}
		return this.valuesByColumn;
	}
	
}
