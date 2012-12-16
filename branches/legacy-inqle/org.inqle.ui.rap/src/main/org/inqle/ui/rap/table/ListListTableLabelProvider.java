/**
 * 
 */
package org.inqle.ui.rap.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * Sets the values in a Table, using a QuerySolution as the element (model).
 * To use this, set the column names using the <code>setColumnFields</code> 
 * method.  The field names in this list should correspond to records (columns) in the QuerySolution
 * 
 * @author David Donohue
 * Mar 11, 2008
 */
public class ListListTableLabelProvider extends CellLabelProvider {

//	protected int rowNumColumnIndex = -1;
	
//	protected List<String> columnFields = new ArrayList<String>();
	
	public static final Logger log = Logger.getLogger(ListListTableLabelProvider.class);
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		
//		if (columnFields==null || columnFields.size()==0) return;
		Object cellObj = cell.getElement();
//		log.info("Received cell:" + cell + " = " + cellObj);
		List<Object> row = (List<Object>)cellObj;
//		String fieldName = columnFields.get(cell.getColumnIndex());
		if (cell.getColumnIndex() >= row.size()) {
			log.info("Skipping cell: " + cell);
			return;
		}
		Object cellValue = row.get(cell.getColumnIndex());
		if (cellValue == null) cellValue = "";
//		log.trace("Update cell: " + fieldName + "=" + cellValue);
		cell.setText(cellValue.toString());
	}

//	public void setColumnFields(List<String> columnFields) {
//		this.columnFields = columnFields;
//	}

//	public void setRowNumColumnIndex(int rowNumColumnIndex) {
//		this.rowNumColumnIndex = rowNumColumnIndex;
//	}
}
