/**
 * 
 */
package org.inqle.ui.rap.table;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * Sets the values in a Table, using a QuerySolution as the element (model).
 * To use this, set the column names using the <code>setColumnFields</code> 
 * method.  The field names in this list should correspond to records (columns) in the QuerySolution
 * 
 * @author David Donohue
 * Mar 11, 2008
 */
public class QuerySolutionTableLabelProvider extends CellLabelProvider {

	protected List<String> columnFields = new ArrayList<String>();
	
	public static final Logger log = Logger.getLogger(QuerySolutionTableLabelProvider.class);
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		
		Object querySolutionObj = cell.getElement();
		QuerySolution querySolution = (QuerySolution)querySolutionObj;
		String fieldName = columnFields.get(cell.getColumnIndex());
		String cellValue = QuerySolutionValueExtractor.getDisplayable(querySolution, fieldName);
		log.debug("Update cell " + querySolution + ":" + fieldName + "=" + cellValue);
		cell.setText(cellValue);
	}

	public void setColumnFields(List<String> columnFields) {
		this.columnFields = columnFields;
	}
}
