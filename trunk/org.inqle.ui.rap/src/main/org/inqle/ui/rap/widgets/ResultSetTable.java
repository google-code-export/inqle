package org.inqle.ui.rap.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;
import org.inqle.data.rdf.jena.util.TypeConverter;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetRewindable;

/**
 * This widget is a table, generated from a Jena ResultSetRewindable object
 * @author David Donohue
 * Dec 31, 2008
 */
public class ResultSetTable extends AScrolledTable {

	private static final String URI_VARIABLE = "uri";
	ResultSetRewindable resultSet;
	protected String uriVariable = URI_VARIABLE;
	private ArrayList<Button> checkboxes;
	
	private static final Logger log = Logger.getLogger(ResultSetTable.class);
	
	public ResultSetTable(Composite parent, int style, ResultSetRewindable resultSet) {
		super(parent, style);
		setResultSet(resultSet);
	}

//	@Override
//	protected void fillColumnNames() {
//		getColumnNames();
//		super.fillColumnNames();
//	}
	
	@Override
	protected List<String> getColumnNames() {
		if (columnNames == null || columnNames.size()==0) {
			columnNames = new ArrayList<String>();
			
			resultSet.reset();
			QuerySolution querySolution = resultSet.nextSolution();
			Iterator<?> varNames = querySolution.varNames();
			while (varNames.hasNext()) {
				Object varName = varNames.next();
				if (varName==null) varName="";
				if (! varName.equals(uriVariable)) {
					columnNames.add(varName.toString());
				}
			}
		}
		return columnNames;
	}

	@Override
	protected void fillTable() {
		resultSet.reset();
		checkboxes = new ArrayList<Button>();
		while (resultSet.hasNext()) {
			//add the checkbox
			Button cb = new Button(composite, SWT.CHECK);
			checkboxes.add(cb);
			QuerySolution querySolution = resultSet.nextSolution();
		
			for (String columnName: getColumnNames()) {
				String displayableValue = QuerySolutionValueExtractor.getDisplayable(querySolution, columnName);
				log.trace(columnName + " = " + displayableValue);
				if (columnName.equals(uriVariable)) {
					cb.setData(displayableValue);
				} else {
					Text text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
					text.setText(displayableValue);
					text.setLayoutData(new GridData(GridData.FILL_BOTH));
				}
			}
		}

	}

	public List<String> getCheckedItems() {
		List<String> checkedItems = new ArrayList<String>();
		for (Button cb: checkboxes) {
			if (cb.getSelection()) {
				Object uri = cb.getData();
				if (uri==null) {
					log.error("Checkbox has an URI of null");
					continue;
				}
				checkedItems.add(uri.toString());
			}
		}
		return checkedItems;
	}

	public ResultSetRewindable getResultSet() {
		return resultSet;
	}


	public void setResultSet(ResultSetRewindable resultSet) {
		this.resultSet = resultSet;
	}

	public String getUriVariable() {
		return uriVariable;
	}

	public void setUriVariable(String uriVariable) {
		this.uriVariable = uriVariable;
	}

}
