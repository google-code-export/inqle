package org.inqle.data.sampling;

import java.util.List;

import org.inqle.data.rdf.jenabean.Arc;

import com.hp.hpl.jena.query.QuerySolution;

public interface IDataTable {

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

	public List<Arc> getLearnableColumns();

	public List<Arc> getColumns();

	public int getIdColumnIndex();
	
	public int getLabelColumnIndex();

	public int getDataType(int columnIndex);

	public Arc getColumn(int columnIndex);

	public List<List<Object>> getRows();

//	public void setLabelColumnIndex(int labelDataColumnIndex);
}
