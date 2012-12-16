package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;

//public class DataTable implements Serializable {
/**
 * The INQLE representation of a learnable, minable table of data
 * 
 * TODO implement an interface IDataTable?
 */
@Deprecated
public class DataTable {
	private RdfTable rdfTable = new RdfTable();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2474824355950690870L;
	
	private List<DataColumn> columns = new ArrayList<DataColumn>();
	private List<List<DataCell>> rows = new ArrayList<List<DataCell>>();

	private String query;
	
	private static Logger log = Logger.getLogger(DataTable.class);
	
	/**
	 * By default, the ID column is the first column (index=0)
	 */
	private int idColumnIndex = 0;

	private int labelColumnIndex = -1;

	public void addRow(List<DataCell> row) {
		rows.add(row);
	}

	public DataCell getCell(int rowIndex, int columnIndex) {
		List<DataCell> row = rows.get(rowIndex);
		return row.get(columnIndex);
	}

	public DataColumn getColumn(int columnIndex) {
		return columns.get(columnIndex);
	}

	public List<DataCell> getRow(int rowIndex) {
		return rows.get(rowIndex);
	}

	public List<List<DataCell>> getRows() {
		return rows;
	}

	public List<DataColumn> getColumns() {
		return columns;
	}

	public int getIdColumnIndex() {
		return idColumnIndex;
	}

	public void setIdColumnIndex(int idColumnIndex) {
		this.idColumnIndex = idColumnIndex;
	}
	
//	public int getLabelColumnIndex() {
//		return labelColumnIndex;
//	}
//
//	public void setLabelColumnIndex(int labelColumnIndex) {
//		this.labelColumnIndex  = labelColumnIndex;
//	}
	
	public void addColumn(DataColumn column) {
		columns.add(column);
	}

	public void setColumns(List<DataColumn> dataColumns) {
		columns = dataColumns;
	}
	
	/**
	 * Is this DataTable complete and ready to be run through an experiment?
	 * @return
	 */
	public boolean isLearnable() {
		if (columns != null && columns.size() > 0 && rows != null && rows.size() > 0 && labelColumnIndex > -1 && labelColumnIndex != idColumnIndex) {
			return true;
		}
		return false;
	}

	public RdfTable getRdfTable() {
		return rdfTable;
	}

	public void setRdfTable(RdfTable rdfTable) {
		this.rdfTable = rdfTable;
	}

	/**
	 * Get List of all DataColumns, excluding the ID
	 * @return
	 */
	public List<DataColumn> getLearnableColumns() {
		log.trace("getLearnableColumns() called; getIdColumnIndex()=" + getIdColumnIndex());
		List<DataColumn> learnableColumns = new ArrayList<DataColumn>(getColumns());
		log.trace("all columns=" + learnableColumns);
		try {
			learnableColumns.remove(getIdColumnIndex());
		} catch (Exception e) {
			log.warn("Unable to remove ID column from the list of columns.  All columns will be used for learning.", e);
		}
		log.trace("returning learnable columns=" + learnableColumns);
		return learnableColumns;
	}
	
//	/**
//	 * Get List of all DataColumns, excluding the ID
//	 * @return
//	 */
//	public List<DataColumn> getRegularAttributeColumns() {
//		log.trace("getRegularAttributeColumns() called; getIdColumnIndex()=" + getIdColumnIndex());
//		List<DataColumn> learnableColumns = new ArrayList<DataColumn>(getColumns());
//		log.trace("all columns=" + learnableColumns);
//		DataColumn idColumn = getColumn(getIdColumnIndex());
//		DataColumn labelColumn = getColumn(getColumnIndex());
//		try {
//			learnableColumns.remove(getIdColumnIndex());
//		} catch (Exception e) {
//			log.warn("Unable to remove ID column from the list of columns.  All columns will be used for learning.", e);
//		}
//		log.trace("returning learnable columns=" + learnableColumns);
//		return learnableColumns;
//	}
	
	public static ArcSet getArcSet(List<DataColumn> dataColumns) {
//		List<Arc> learnableArcs = new ArrayList<Arc>();
//		for (DataColumn dataColumn: getLearnableColumns()) {
//			learnableArcs.add(dataColumn.getArc());
//		}
		ArcSet learnableArcs = new ArcSet();
		for (DataColumn dataColumn: dataColumns) {
			learnableArcs.addArcAndValue(dataColumn.getArc(), null);
		}
		return learnableArcs;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
