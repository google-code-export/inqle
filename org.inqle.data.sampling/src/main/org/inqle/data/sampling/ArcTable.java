package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.util.TypeConverter;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;

/**
 * This class contains data from an RDF model, and corresponding metadata.  Such metadata describes the
 * columns (headers), the data type and column type of each column, the values in each cell. 
 * This class is used in the conversion of data in a Jena RDF model into a learnable example set.
 * 
 * @author David Donohue
 * Oct 24, 2008
 */
public class ArcTable implements IDataTable {

	private static Logger log = Logger.getLogger(ArcTable.class);
	
	private List<LinkedHashSet<Object>> columnDistinctValues= new ArrayList<LinkedHashSet<Object>>();
	
	private LinkedHashSet<Arc> headers = new LinkedHashSet<Arc>();
	private List<List<Object>> rowData = new ArrayList<List<Object>>();
	private List<Integer> dataTypes = new ArrayList<Integer>();
	private List<Integer> columnTypes = new ArrayList<Integer>();
	private List<List<Object>> columnData = new ArrayList<List<Object>>();;
	private List<Arc> headerList = new ArrayList<Arc>();
	
	private int idColumnIndex = -1;
	private int labelColumnIndex = -1;

	private String subjectClass;
	
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
		this.headers.addAll(headers);
	}
	public int getHeaderIndex(Arc arc) {
		//log.info("Finding index of Arc " + arc + " among these columns: " + getColumns());
		//log.info("getColumns().contains(arc)? " + getColumns().contains(arc));
		return getColumns().indexOf(arc);
	}
	
	public List<Arc> getColumns() {
		if (headerList.size() != headers.size()) {
			headerList = new ArrayList<Arc>();
			for (Arc header: headers) {
				headerList.add(header);
			}
		}
		return headerList;
	}
	
	public Arc getColumn(int columnIndex) {
		if (columnIndex < 0 || headerList==null || headerList.size() <= columnIndex) {
			return null;
		}
		if (headerList==null || headerList.size()< columnIndex + 1) {
			return null;
		}
		return headerList.get(columnIndex);
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
		//log.info("Adding to ArcTable: ArcSet=" + newRowArcSet);
		ArrayList<Object> row = new ArrayList<Object>();
		List<Arc> newArcs = newRowArcSet.getArcList();
		//add any new Arcs to the headers
		if (getHeaders()==null || getHeaders().size()==0) {
			addHeaders(newArcs);
			//log.info("Added headers:" + newArcs);
		}
		
		for (Arc arc: newArcs) {
			int columnIndex = getHeaderIndex(arc);
			if (columnIndex < 0) {
				log.warn("Arc not found among ArcTable headers:" + arc);
				addHeader(arc);
				//log.info("Added header:" + arc);
				columnIndex = getHeaderIndex(arc);
			}
			Object value = newRowArcSet.getValue(arc);
			if (row.size() > columnIndex) {
				row.set(columnIndex, value);
				//log.info("Set item " + columnIndex + " to " + value);
				continue;
			}
			for (int i=row.size(); i<=columnIndex; i++) {
				if (i==columnIndex) {
					row.add(value);
					//log.info("Add at item " + columnIndex + ": " + value);
					continue;
				}
				row.add(null);
			}
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
		int rowIndex = 0;
		for (List<Object> row: rowData) {
//			log.info("Row #" + rowIndex + ": row=" + row);
			if (row.size()<=columnIndex) {
				log.warn("Column " + columnIndex + " not defined for row #" + rowIndex + ": row=" + row);
				continue;
			}
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
			rowIndex++;
		}
		
		int dataType = IDataTable.DATA_TYPE_UNKNOWN;
		if (distinctValues.size()==0) {
			dataType = IDataTable.DATA_TYPE_NO_VALUES;
		} else if (!containsStrings && containsNumbers && !containsDates) {
			dataType = IDataTable.DATA_TYPE_NUMERIC;
		} else if (!containsStrings && !containsNumbers && containsDates) {
			dataType = IDataTable.DATA_TYPE_DATE;
		} else {
			dataType = IDataTable.DATA_TYPE_STRING;
		}
		
		int columnType = IDataTable.COLUMN_TYPE_UNKNOWN;
		if (distinctValues.size()==columnValues.size()) {
			columnType = IDataTable.COLUMN_TYPE_ALL_UNIQUE;
		} else if (distinctValues.size()==0) {
			columnType = IDataTable.COLUMN_TYPE_NO_VALUES;
		} else if (distinctValues.size()==1) {
			columnType = IDataTable.COLUMN_TYPE_ONE_VALUE;
		} else {
			columnType = IDataTable.COLUMN_TYPE_LEARNABLE;
		}
		
//		columnData.set(columnIndex, columnValues);
//		columnDistinctValues.set(columnIndex, distinctValues);
//		dataTypes.set(columnIndex, dataType);
//		columnTypes.set(columnIndex, columnType);
		addOrSetListItem(columnData, columnIndex, columnValues, null);
		addOrSetListItem(columnDistinctValues, columnIndex, distinctValues, null);
		addOrSetListItem(dataTypes, columnIndex, dataType, IDataTable.DATA_TYPE_UNKNOWN);
		addOrSetListItem(columnTypes, columnIndex, columnType, IDataTable.COLUMN_TYPE_UNKNOWN);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Set a value at the desired list position, specified by index.
	 * If the list is not of sufficient length, add elements of value undefinedValue
	 * in the intervening positions.
	 */
	private void addOrSetListItem(List list, int index, Object value, Object undefindedValue) {
		if (list.size() <= index) {
			for (int i=list.size(); i<index; i++) {
				list.add(undefindedValue);
			}
			list.add(value);
		} else {
			list.set(index, value);
		}
	}
	
	public int getIdColumnIndex() {
		return idColumnIndex;
	}
	public int getLabelColumnIndex() {
		return labelColumnIndex;
	}
	public List<Arc> getLearnableColumns() {
		List<Arc> learnableArcs = new ArrayList<Arc>();
		int colIndex = -1;
		for (Arc arc: getColumns()) {
			colIndex++;
			if (getColumnType(colIndex) == IDataTable.COLUMN_TYPE_LEARNABLE) {
				learnableArcs.add(arc);
			}
		}
		return learnableArcs;
	}
	public void setIdColumnIndex(int idColumnIndex) {
		this.idColumnIndex = idColumnIndex;
	}
	public void setLabelColumnIndex(int labelColumnIndex) {
		this.labelColumnIndex = labelColumnIndex;
	}
	public String getSubjectClass() {
		return subjectClass;
	}
	public void setSubjectClass(String subjectClass) {
		this.subjectClass = subjectClass;
	}
}
