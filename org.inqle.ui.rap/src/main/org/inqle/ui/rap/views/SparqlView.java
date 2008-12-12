package org.inqle.ui.rap.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.table.QuerySolutionTableLabelProvider;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * @author David Donohue
 * May 6, 2008
 */
public abstract class SparqlView extends ViewPart implements SelectionListener, Listener {

	private static final Logger log = Logger.getLogger(SparqlView.class);
	
	private static final int COLUMN_WIDTH = 120;

	private static final int DEFAULT_RECORD_COUNT_INDEX = 0;
	private static final String[] RECORD_COUNT_OPTIONS = {"10", "20", "50", "100", "200", "500", "1000", "5000", "10000" };
	private static final String DESC = "DESC";
	private static final String ASC = "ASC";

	private static final String DEFAULT_SORT_DIRECTION = DESC;
	
	private Composite composite;
	
	protected List<QuerySolution> rows = new ArrayList<QuerySolution>();
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = QuerySolution.class;

	//protected Persister persister;
	protected TableViewer tableViewer;
	protected List<String> propertyNames;
	protected Button refreshButton;
	protected String currentSortColumn = "Creation_Date";
	protected String currentSortDirection = DEFAULT_SORT_DIRECTION;
	protected org.eclipse.swt.widgets.List recordCountList;
	protected Button previousButton;
	protected Button nextButton;
	protected Label resultDescription;
	
	private int offset = 0;
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		//composite = new Composite(parent, SWT.None);
		composite = parent;
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		composite.setLayoutData(gridData);
		
		GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    composite.setLayout(gridLayout);
    
		showForm();
		showResultDescription();
		showTable();
		refreshForm();
	}

	private void showForm() {
		Composite formComposite = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		formComposite.setLayout(rowLayout);
		
		//select list for number of records to show
		recordCountList = new org.eclipse.swt.widgets.List(formComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		recordCountList.setItems(RECORD_COUNT_OPTIONS);
		recordCountList.setSelection(DEFAULT_RECORD_COUNT_INDEX);
		//GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//formComposite.setLayoutData(gridData);
		refreshButton = new Button(formComposite, SWT.PUSH);
		refreshButton.setText("Refresh");
		refreshButton.addSelectionListener(this);
		
	}

	private void showResultDescription() {
		Composite descriptionComposite = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		descriptionComposite.setLayout(rowLayout);
		resultDescription = new Label(descriptionComposite, SWT.NONE);
		resultDescription.setText("No Experiment Results found.");
		
		previousButton = new Button(descriptionComposite, SWT.PUSH);
		previousButton.setText("<--");
		previousButton.addSelectionListener(this);
		nextButton = new Button(descriptionComposite, SWT.PUSH);
		nextButton.setText("-->");
		nextButton.addSelectionListener(this);
	}
	
	public void createTable() {
		//if no data, return
		if (rows == null || rows.size() == 0) {
			return;
		}
		
		Table table = new Table(composite, SWT.NONE);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		//add columns
		for (String propertyName: getPropertyNames()) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			if (propertyName==null) propertyName="";
			column.setText(propertyName.replaceAll("_", " "));
			column.setToolTipText("Click to sort");
			column.setResizable(true);
			column.setWidth(getColumnWidth());
			column.addListener(SWT.Selection, this);
			log.trace("Added column: " + propertyName);
		}
		
		tableViewer = new TableViewer(table);
		ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(olContentProvider);
		
		QuerySolutionTableLabelProvider labelProvider = new QuerySolutionTableLabelProvider();
		labelProvider.setColumnFields(getPropertyNames());
		tableViewer.setLabelProvider(labelProvider);
	}
	
	/**
	 * Override and set the default column width
	 * By default, set to 100
	 * @return
	 */
	private int getColumnWidth() {
		return COLUMN_WIDTH;
	}

	public void showTable() {
		if (rows == null || rows.size() == 0) {
			doQuery();
		}
		
		if (tableViewer == null) {
			//if no data, do not create the table
			if (rows == null || rows.size() == 0) {
				return;
			}
			//otherwise, do create the table
			createTable();
		}
		//update the results description
		resultDescription.setText("Showing results " + (offset + 1) + " to " + (rows.size() + offset));
		
		WritableList writableListInput = new WritableList(rows, tableBeanClass);
		log.trace("writableListInput=" + writableListInput);
		tableViewer.setInput(writableListInput);
		//tableViewer.getTable().pack();
		tableViewer.refresh(false);
		//composite.getShell().pack();
	}
	
	@Override
	public void setFocus() {
		showTable();
	}
	
	/**
	 * Set the rows of data in the table.  The class of items should be set in the setRowItemClass() method
	 * @param beans
	 */
	public void setRdfTable(RdfTable rdfTable) {
		//this.rdfTable = rdfTable;
		this.rows = rdfTable.getResultList();
		if (getPropertyNames() == null) {
			setPropertyNames(rdfTable.getVarNameList());
		}
	}

	public List<QuerySolution> getRows() {
		return rows;
	}
	
	public void doQuery() {
		Persister persister = Persister.getInstance();
		String sparql = getSparql();
		log.trace("Querying w/ SPARQL:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(sparql);
		//TODO change to LogModel
		queryCriteria.addNamedModel(persister.getInternalDataset(getDatasetRole()));
		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		log.trace("Received these results: " + resultTable.getResultList());
		setRdfTable(resultTable);
	}

	public abstract String getDatasetRole();

	public abstract String getSparql();

//	private Persister getPersister() {
//		if (persister == null) {
//			persister = Persister.getInstance();
//		}
//		return persister;
//	}

//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}
	
	public int getRecordCount() {
		int selectedIndex = recordCountList.getSelectionIndex();
		String recordCountStr = RECORD_COUNT_OPTIONS[selectedIndex];
		int recordCount = Integer.parseInt(recordCountStr);
		return recordCount;
	}
	
	public int getOffset() {
		return offset;
	}
	
	/**
	 * SelectionListener method
	 */
	public void widgetSelected(SelectionEvent event) {
		log.trace("event=" + event);
		if (event.getSource() == refreshButton) {
			log.trace("Refresh clicked");
			refreshView();
		}
		
		if (event.getSource() == previousButton) {
			offset = offset - getRecordCount();
			if (offset < 0) {
				offset = 0;
			}
			log.trace("Previous clicked");
			refreshView();
		}
		
		if (event.getSource() == nextButton) {
			if (rows.size() < getRecordCount()) {
				refreshForm();
				return;
			}
			offset = offset + getRecordCount();
			
			log.trace("Next clicked");
			refreshView();
		}
	}
	
	private void refreshView() {
		doQuery();
		refreshForm();
		showTable();
	}

	private void refreshForm() {
		if (offset <= 0) {
			log.trace("Disabled previousButton");
			previousButton.setEnabled(false);
		} else {
			log.trace("Enabled previousButton");
			previousButton.setEnabled(true);
		}
		if (rows.size() < getRecordCount()) {
			log.trace("Disabled nextButton");
			nextButton.setEnabled(false);
		} else {
			log.trace("Enabled nextButton");
			nextButton.setEnabled(true);
		}
		
	}

	/**
	 * SelectionListener method
	 */
	public void widgetDefaultSelected(SelectionEvent event) {
		// do nothing
	}
	
	/**
	 * Listener method (for clicking of column header)
	 */
	public void handleEvent(Event event) {
		if (event.widget instanceof TableColumn) {
			
			TableColumn column = (TableColumn) event.widget;
			String columnHeader = column.getText();
			if (columnHeader==null) columnHeader="";
			columnHeader = columnHeader.replaceAll(" ", "_");
			//reset to the first record
			offset = 0;
			if (currentSortColumn.equals(columnHeader)) {
				toggleCurrentSortDirection();
			} else {
				currentSortColumn = column.getText();
				currentSortDirection = DEFAULT_SORT_DIRECTION;
			}
			log.trace("Sort by " + currentSortColumn + " " + currentSortDirection);
			//no need to refresh the form, since we are only changing sort
			doQuery();
			showTable();
		}
  }

	private void toggleCurrentSortDirection() {
		if (currentSortDirection.equals(DESC)) {
			currentSortDirection = ASC;
		} else {
			currentSortDirection = DESC;
		}
		
	}

	public String getCurrentSortColumn() {
		return currentSortColumn;
	}

	public void setCurrentSortColumn(String currentSortColumn) {
		if (currentSortColumn == null) {
			this.currentSortColumn = null;
			return;
		}
		this.currentSortColumn = currentSortColumn.replaceAll(" ", "_");
	}

	public String getCurrentSortDirection() {
		return currentSortDirection;
	}

	public void setCurrentSortDirection(String currentSortDirection) {
		this.currentSortDirection = currentSortDirection;
	}

	/**
	 * Override to specify the property names of your implementation.  This prevents the 
	 * problem that optional fields might be missed in the first page
	 * @return
	 */
	public List<String> getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(List<String> propertyNames) {
		this.propertyNames = propertyNames;
	}
}
