package org.inqle.data.rdf.jena.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;

public class ArcTable {

	public static final int DATA_TYPE_UNKNOWN = 0;
	public static final int DATA_TYPE_NO_VALUES = 1;
	public static final int DATA_TYPE_STRING = 2;
	public static final int DATA_TYPE_NUMERIC = 3;
	public static final int DATA_TYPE_DATE = 4;
	
	public static final int COLUMN_TYPE_UNKNOWN = 0;
	public static final int COLUMN_TYPE_NO_VALUES = 1;
	public static final int COLUMN_TYPE_ONE_VALUE = 2;
	public static final int COLUMN_TYPE_LEARNABLE = 3;
	public static final int COLUMN_TYPE_ALL_UNIQUE = 4;
	

	private static Logger log = Logger.getLogger(ArcTable.class);
	
	private List<LinkedHashSet<Object>> columnDistinctValues= new ArrayList<LinkedHashSet<Object>>();
	
	private LinkedHashSet<Arc> headers = new LinkedHashSet<Arc>();
	private List<List<Object>> rowData;
	private List<Integer> dataTypes = new ArrayList<Integer>();
	private List<Integer> columnTypes = new ArrayList<Integer>();
	private List<List<Object>> columnData;
	private List<Arc> headerList = new ArrayList<Arc>();
	public LinkedHashSet<Arc> getHeaders() {
		return headers;
	}
	public void setHeaders(LinkedHashSet<Arc> headers) {
		this.headers = headers;
	}
	public void addHeader(Arc header) {
		headers.add(header);
	}
	public void addHeaders(List<Arc> headers) {
		headers.addAll(headers);
	}
	public int getHeaderIndex(Arc arc) {
		
		return getHeaderList().indexOf(arc);
	}
	
	public List<Arc> getHeaderList() {
		if (headerList.size() != headers.size()) {
			headerList = new ArrayList<Arc>();
			for (Arc header: headers) {
				headerList.add(header);
			}
		}
		return headerList;
	}
	public List<List<Object>> getRows() {
		return rowData;
	}
	public void setRows(List<List<Object>> rows) {
		this.rowData = rows;
	}
	public void addRow(List<Object> row) {
		rowData.add(row);
	}
	
	/**
	 * Add an ArcSet, representing a row of data, to this table
	 * @param row
	 */
	public void addArcSet(ArcSet newRowArcSet) {
		List<Object> row = new ArrayList<Object>();
		List<Arc> newArcs = newRowArcSet.getArcs();
		//add any new Arcs to the headers
		addHeaders(newArcs);
		for (Arc arc: newArcs) {
			int columnIndex = getHeaderIndex(arc);
			Object value = newRowArcSet.getValue(arc);
			row.add(columnIndex, value);
		}
		addRow(row);
	}
	public List<Object> getRow(int rowIndex) {
		return rowData.get(rowIndex);
	}
	public Object getCell(int rowIndex, int columnIndex) {
		List<Object> row = getRow(rowIndex);
		return row.get(columnIndex);
	}
	public int getDataType(int columnIndex) {
		if (dataTypes.size() < (columnIndex+1)) {
			processColumn(columnIndex);
		}
		return dataTypes.get(columnIndex);
	}
	public int getColumnType(int columnIndex) {
		if (columnTypes.size() < columnIndex+1) {
			processColumn(columnIndex);
		}
		return columnTypes.get(columnIndex);
	}
	public LinkedHashSet<Object> getColumnDistinctValues(int columnIndex) {
		if (columnDistinctValues.size() < columnIndex+1) {
			processColumn(columnIndex);
		}
		return columnDistinctValues.get(columnIndex);
	}
	public List<Object> getColumnValues(int columnIndex) {
		if (columnData.size() < columnIndex+1) {
			processColumn(columnIndex);
		}
		return columnData.get(columnIndex);
	}
	
	/**
	 * Process column of the specified index.  Assign the data type, the column type,
	 * the list of values, and the ordered set of unique values
	 * @param columnIndex
	 */
	private void processColumn(int columnIndex) {
		List<Object> columnValues = new ArrayList<Object>();
		LinkedHashSet<Object> distinctValues = new LinkedHashSet<Object>();
		boolean containsDates = false;
		boolean containsNumbers = false;
		boolean containsStrings = false;
		for (List<Object> row: rowData) {
			Object value = row.get(columnIndex);
			//if the value is a string, attempt to parse it into other data types
			if (value instanceof String) {
				value = TypeConverter.parseStringToType((String)value);
			}
			if (value instanceof String) {
				containsStrings = true;
			}
			if (value instanceof Date) {
				containsDates = true;
			}
			if (value instanceof Integer || value instanceof Double) {
				containsNumbers = true;
			}
			columnValues.add(value);
			distinctValues.add(value);
		}
		
		int dataType = DATA_TYPE_UNKNOWN;
		if (distinctValues.size()==0) {
			dataType = DATA_TYPE_NO_VALUES;
		} else if (!containsStrings && containsNumbers && !containsDates) {
			dataType = DATA_TYPE_NUMERIC;
		} else if (!containsStrings && !containsNumbers && containsDates) {
			dataType = DATA_TYPE_DATE;
		} else {
			dataType = DATA_TYPE_STRING;
		}
		
		int columnType = COLUMN_TYPE_UNKNOWN;
		if (distinctValues.size()==columnValues.size()) {
			columnType = COLUMN_TYPE_ALL_UNIQUE;
		} else if (distinctValues.size()==0) {
			columnType = COLUMN_TYPE_NO_VALUES;
		} else if (distinctValues.size()==1) {
			columnType = COLUMN_TYPE_ONE_VALUE;
		} else if (distinctValues.size()==0) {
			columnType = COLUMN_TYPE_NO_VALUES;
		} else {
			columnType = COLUMN_TYPE_LEARNABLE;
		}
		columnData.set(columnIndex, columnValues);
		columnDistinctValues.set(columnIndex, distinctValues);
		dataTypes.set(columnIndex, dataType);
		columnTypes.set(columnIndex, columnType);
	}
}
