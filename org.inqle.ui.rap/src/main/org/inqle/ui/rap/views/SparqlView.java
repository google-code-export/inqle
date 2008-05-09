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
	
	private static final int COLUMN_WIDTH = 100;

	private static final int DEFAULT_RECORD_COUNT = 100;

	private static final String DESC = "DESC";
	private static final String ASC = "ASC";

	private static final String DEFAULT_SORT_DIRECTION = DESC;
	
	private Composite composite;
	
	protected List<QuerySolution> rows = new ArrayList<QuerySolution>();
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = QuerySolution.class;

	protected Persister persister;
	protected TableViewer tableViewer;
	protected List<String> propertyNames;
	protected Button refreshButton;
	protected String currentSortColumn = "creationDate";
	protected String currentSortDirection = DEFAULT_SORT_DIRECTION;

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
		showTable();
	}
	
	private void showForm() {
		Composite formComposite = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		formComposite.setLayout(rowLayout);
		
		//GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//formComposite.setLayoutData(gridData);
		refreshButton = new Button(formComposite, SWT.PUSH);
		refreshButton.setText("Refresh");
		refreshButton.addSelectionListener(this);
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
		for (String propertyName: propertyNames) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			column.setText(propertyName);
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
		labelProvider.setColumnFields(propertyNames);
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
		if (rows == null || rows.size() == 0) {
			return;
		}
		if (tableViewer == null) {
			createTable();
		}
		
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
		this.propertyNames = rdfTable.getVarNameList();
	}

	public List<QuerySolution> getRows() {
		return rows;
	}
	
	public void doQuery() {
		String sparql = getSparql();
		log.info("Querying w/ SPARQL:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria(getPersister());
		queryCriteria.setQuery(sparql);
		//TODO change to LogModel
		queryCriteria.addNamedModel(persister.getAppInfo().getRepositoryNamedModel());
		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		log.trace("Received these results: " + resultTable.getResultList());
		setRdfTable(resultTable);
	}

	public abstract String getSparql();

	private Persister getPersister() {
		if (persister == null) {
			persister = Persister.createPersister();
		}
		return persister;
	}

	public void setPersister(Persister persister) {
		this.persister = persister;
	}
	
	public int getRecordCount() {
		return DEFAULT_RECORD_COUNT;
	}
	
	public int getOffset() {
		return offset;
	}
	
	/**
	 * SelectionListener method
	 */
	public void widgetSelected(SelectionEvent event) {
		if (event.getSource() == refreshButton) {
			log.info("Refresh clicked");
			doQuery();
			showTable();
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
		log.info("Clicked " + event.widget);
		if (event.widget instanceof TableColumn) {
			
			TableColumn column = (TableColumn) event.widget;
			//reset to the first record
			offset = 0;
			if (currentSortColumn.equals(column.getText())) {
				toggleCurrentSortDirection();
			} else {
				currentSortColumn = column.getText();
				currentSortDirection = DEFAULT_SORT_DIRECTION;
			}
			log.info("Sort by " + currentSortColumn + " " + currentSortDirection);
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
		this.currentSortColumn = currentSortColumn;
	}

	public String getCurrentSortDirection() {
		return currentSortDirection;
	}

	public void setCurrentSortDirection(String currentSortDirection) {
		this.currentSortDirection = currentSortDirection;
	}
}
