package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.List;

public class DataTableReader {

	public static List<DataCell> getColumnCells(DataTable table, int columnIndex) {
		List<DataCell> columnVals = new ArrayList<DataCell>();
		List<List<DataCell>> rows = table.getRows();
		for (List<DataCell> row: rows) {
			DataCell cell = row.get(columnIndex);
			columnVals.add(cell);
		}
		return columnVals;
	}
	
	public static List<String> getColumnStringValues(DataTable table, int columnIndex) {
		List<String> columnVals = new ArrayList<String>();
		List<List<DataCell>> rows = table.getRows();
		for (List<DataCell> row: rows) {
			DataCell cell = row.get(columnIndex);
			String cellVal = cell.toString();
			columnVals.add(cellVal);
		}
		return columnVals;
	}
}
