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
import org.inqle.ui.rap.csv.CsvImporter;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * Sets the values in a Table, using a QuerySolution as the element (model).
 * To use this, set the column names using the <code>setColumnFields</code> 
 * method.  The field names in this list should correspond to records (columns) in the QuerySolution
 * 
 * @author David Donohue
 * Mar 11, 2008
 */
public class CsvTableLabelProvider extends CellLabelProvider {

//	protected int rowNumColumnIndex = -1;
	
	protected List<String> columnFields = new ArrayList<String>();

//	private CsvImporter csvImporter;
	
	public static final Logger log = Logger.getLogger(CsvTableLabelProvider.class);
	
//	public CsvTableLabelProvider(CsvImporter csvImporter) {
//		this.csvImporter = csvImporter;
//	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		String[] row = (String[])cell.getElement();
		String cellValue = null;
		try {
			cellValue = row[cell.getColumnIndex()];
		} catch (Exception e) {
			log.error("Unable to get value for column at index=" + cell.getColumnIndex(), e);
		}
//		log.info("Update cell for col #" + cell.getColumnIndex() + ":" + cellValue + "=" + cellValue);
		cell.setText(cellValue);
	}

}
