package org.inqle.ui.rap.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.util.QuerySolutionValueExtractor;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * This widget is a table, generated from a Jena ResultSetRewindable object.
 * For column names, this uses the variable names from the first row
 * in the result set.
 * @author David Donohue
 * Dec 31, 2008
 */
public class ResultSetTable extends AScrolledTable implements SelectionListener {

	public static final String URI_VARIABLE = "URI";
	ResultSetRewindable resultSet;
	protected String uriVariable = URI_VARIABLE;
	private ArrayList<Button> checkboxes;
	protected String linkColumn;
	private boolean hideUriColumn = true;
	private boolean linkUriOnly = true;
	private static final Logger log = Logger.getLogger(ResultSetTable.class);
	
	/**
	 * This class holds the name of a column, and is attached to a link
	 * @author David Donohue
	 * Jan 12, 2009
	 */
	public class UriValData {

		private String uriVal;

		public UriValData(String uriVal) {
			this.uriVal = uriVal;
		}

		public String getUriVal() {
			return uriVal;
		}
		
		@Override
		public String toString() {
			return uriVal;
		}

	}
	
	public ResultSetTable(Composite parent, int style) {
		super(parent, style);
	}
	
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
			if (resultSet==null || resultSet.size()==0) {
				return columnNames;
			}
			resultSet.reset();
			QuerySolution querySolution = resultSet.nextSolution();
			Iterator<?> varNames = querySolution.varNames();
			while (varNames.hasNext()) {
				Object varName = varNames.next();
				if (varName==null) varName="";
				if (hideUriColumn  && varName.equals(uriVariable)) {
//					log.info("Hiding column: " + uriVariable);
					continue;
				}
				columnNames.add(varName.toString());
			}
		}
		return columnNames;
	}

	@Override
	protected void fillTable() {
		resultSet.reset();
		if (addCheckBoxes) {
			checkboxes = new ArrayList<Button>();
		}
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.nextSolution();
			RDFNode uriNode = querySolution.get(uriVariable);
			String uriVal = QuerySolutionValueExtractor.getDisplayable(querySolution, uriVariable);
			//add the checkbox
			if (addCheckBoxes) {
//				if (uriVal != null && uriVal.length() > 0 && !uriVal.equals(QuerySolutionValueExtractor.DISPLAY_NULL)) {
				if (uriNode != null && uriNode.isURIResource()) {
					Button cb = new Button(composite, SWT.CHECK);
					checkboxes.add(cb);
					cb.setData(uriVal);
					cb.addSelectionListener(this);
				} else {
					new Label(composite, SWT.NONE);
				}
			}
			
			for (String columnName: getColumnNames()) {
				String displayableValue = QuerySolutionValueExtractor.getDisplayable(querySolution, columnName);
				
				if (linkColumn != null && linkColumn.equals(columnName)) {
					RDFNode linkColNode = querySolution.get(columnName);
//					log.info("Column: " + columnName + "; linkUriOnly=" + linkUriOnly + "; linkColumn=" + linkColumn + "; linkColNode.isURIResource()=" + linkColNode.isURIResource());
					if (linkUriOnly && (linkColNode==null || ! (linkColNode.isURIResource()))) {
//						Text text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
//						text.setText(displayableValue);
//						text.setLayoutData(new GridData(GridData.FILL_BOTH));
//						Label label = new Label(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//						label.setText(displayableValue);
//						label.setLayoutData(new GridData(GridData.FILL_BOTH));
						Link link = new Link(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
						link.setText(displayableValue);
						link.setLayoutData(new GridData(GridData.FILL_BOTH));
					} else {
//					RDFNode node = querySolution.get(columnName);
						Link link = new Link(composite, SWT.NONE);
	//					link.setToolTipText("Click to sort");
						link.addSelectionListener(listener);
						link.setText("<a>"+displayableValue+"</a>");
						link.setData(new UriValData(displayableValue));
//						log.info("Set link data to: " + displayableValue);
					}
				} else {
				//log.info(columnName + " = " + displayableValue);
//					Text text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
//					text.setText(displayableValue);
//					text.setLayoutData(new GridData(GridData.FILL_BOTH));
//					Label label = new Label(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
//					label.setText(displayableValue);
//					label.setLayoutData(new GridData(GridData.FILL_BOTH));
					Link link = new Link(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
					link.setText(displayableValue);
					link.setLayoutData(new GridData(GridData.FILL_BOTH));
				}
			}
		}

	}

	public List<String> getCheckedItems() {
		List<String> checkedItems = new ArrayList<String>();
		for (Button cb: checkboxes) {
			//log.info("Checkbox " + cb + " has data: " + cb.getData() + "; is checked? " + cb.getSelection());
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

	public void checkAllRows(boolean checked) {
		if (checkboxes==null) return;
		for (Button cb: checkboxes) {
			cb.setSelection(checked);
		}
	}
	
	public void checkItem(Button cb) {
		if (selectionMode == SWT.SINGLE) {
			checkAllRows(false);
			cb.setSelection(true);
		}
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent event) {
		Object source = event.getSource();
		if (source instanceof Button) {
			Button clickedButton = (Button) source;
			if (clickedButton.getSelection()) {
				checkItem(clickedButton);
			}
		}
	}

	public String getLinkColumn() {
		return linkColumn;
	}

	public void setLinkColumn(String linkColumn) {
		this.linkColumn = linkColumn;
	}

	public boolean isHideUriColumn() {
		return hideUriColumn;
	}

	public void setHideUriColumn(boolean hideUriColumn) {
		this.hideUriColumn = hideUriColumn;
	}

	public void setLinkUriOnly(boolean linkUriOnly) {
		this.linkUriOnly  = linkUriOnly;
	}
	
	public int countCheckboxes() {
		if (checkboxes==null) return 0;
		return checkboxes.size();
	}
}
