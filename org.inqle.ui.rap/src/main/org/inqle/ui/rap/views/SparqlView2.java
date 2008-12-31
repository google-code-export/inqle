package org.inqle.ui.rap.views;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.widgets.ResultSetTable;

import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author David Donohue
 * May 6, 2008
 */
public abstract class SparqlView2 extends ViewPart implements SelectionListener {

	private static final Logger log = Logger.getLogger(SparqlView2.class);
	
	private static final int COLUMN_WIDTH = 120;

	private static final int DEFAULT_RECORD_COUNT_INDEX = 0;
	private static final String[] RECORD_COUNT_OPTIONS = {"10", "20", "50", "100", "200", "500", "1000" };
	private static final String DESC = "DESC";
	private static final String ASC = "ASC";

	private static final String DEFAULT_SORT_DIRECTION = DESC;
	
	private Composite composite;
	
//	protected List<QuerySolution> rows = new ArrayList<QuerySolution>();

	//protected Persister persister;
	protected ResultSetTable resultSetTable;
	protected List<String> propertyNames;
	protected Button refreshButton;
	protected String currentSortColumn = "Creation_Date";
	protected String currentSortDirection = DEFAULT_SORT_DIRECTION;
	protected org.eclipse.swt.widgets.List recordCountList;
	protected Button previousButton;
	protected Button nextButton;
	protected Label resultDescription;
	
	private int offset = 0;

	private ResultSetRewindable resultSet;

	private Button deleteButton;

	private NamedModel namedModel;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		//composite = new Composite(parent, SWT.None);
		composite = parent;
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		composite.setLayoutData(gridData);
		
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
		deleteButton = new Button(descriptionComposite, SWT.PUSH);
		deleteButton.setText("Delete");
		deleteButton.addSelectionListener(this);
	}
	
	public void createTable() {
		//if no data, return
		if (resultSet == null || resultSet.size() == 0) {
			return;
		}
		log.info("Rendering table for result set of size: " + resultSet.size());
		resultSetTable = new ResultSetTable(composite, SWT.NONE, resultSet);
		resultSetTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		resultSetTable.renderTable(this);
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
		if (resultSet == null || resultSet.size() == 0) {
			doQuery();
		}
		
		if (resultSet == null || resultSet.size() == 0) {
			return;
		}
		
		if (resultSetTable == null) {
			createTable();
		}
		//update the results description
		resultDescription.setText("Showing results " + (offset + 1) + " to " + (resultSet.size() + offset));
		
		
	}
	
	@Override
	public void setFocus() {
		showTable();
	}
	
	public void doQuery() {
		String sparql = getSparql();
		log.trace("Querying w/ SPARQL:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(sparql);
		queryCriteria.addNamedModel(getNamedModel());
		this.resultSet = Queryer.selectResultSet(queryCriteria);
	}

	public NamedModel getNamedModel() {
		return namedModel;
	}

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
		log.info("event=" + event);
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
			if (resultSet.size() < getRecordCount()) {
				refreshForm();
				return;
			}
			offset = offset + getRecordCount();
			
			log.trace("Next clicked");
			refreshView();
		}
		
		if (event.getSource() == deleteButton) {
			log.info("Delete clicked");
			deleteSelectedItems();
			refreshView();
		}
		
		if (event.getSource() instanceof Link) {
			Link clickedLink = (Link)event.getSource();
			Object linkData = clickedLink.getData();
			if (linkData ==null) {
				log.warn("Clicked link has no data.");
				return;
			}
			String linkValue = linkData.toString();
			//reset to the first record
			offset = 0;
			if (currentSortColumn.equals(linkValue)) {
				toggleCurrentSortDirection();
				
			} else {
				currentSortColumn = linkValue;
				currentSortDirection = DEFAULT_SORT_DIRECTION;
			}
			log.info("Sort by " + currentSortColumn + " " + currentSortDirection);
			//no need to refresh the form, since we are only changing sort
			doQuery();
			showTable();
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
		if (resultSet.size() < getRecordCount()) {
			log.trace("Disabled nextButton");
			nextButton.setEnabled(false);
		} else {
			log.trace("Enabled nextButton");
			nextButton.setEnabled(true);
		}
		if (resultSet.size()>0) {
			deleteButton.setEnabled(true);
		} else {
			deleteButton.setEnabled(false);
		}
	}

	/**
	 * SelectionListener method
	 */
	public void widgetDefaultSelected(SelectionEvent event) {
		log.info("DefaultSelection: event=" + event);
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

//	/**
//	 * Override to specify the property names of your implementation.  This prevents the 
//	 * problem that optional fields might be missed in the first page
//	 * @return
//	 */
//	public List<String> getPropertyNames() {
//		return propertyNames;
//	}
//
//	public void setPropertyNames(List<String> propertyNames) {
//		this.propertyNames = propertyNames;
//	}
	
	public void deleteSelectedItems() {
		List<String> checkedItems = resultSetTable.getCheckedItems();
		if (checkedItems.size()==0) {
			MessageDialog.openWarning(composite.getShell(), "Unable to Delete Items", "No items are selected below for deletion.  Please check boxes next to any idem(s) you would like to delete.");
			return;
		}
		Persister persister = Persister.getInstance();
		
		boolean confirmDelete = MessageDialog.openConfirm(composite.getShell(), "Delete these items?", "Are you sure you want to delete these " + checkedItems.size() + " items?\nTHIS CANNOT BE UNDONE!");
		if (! confirmDelete) return;
		
		Model modelToDeleteFrom = persister.getModel(getNamedModel());
		long sizeBeforeDelete = modelToDeleteFrom.size();
		int deletedCount = 0;
		for (String uriToDelete: checkedItems) {
			Resource resourceToDelete = modelToDeleteFrom.getResource(uriToDelete);
			if (resourceToDelete==null) {
				log.info("Unable to retrieve resource from model: " + uriToDelete);
				continue;
			}
			modelToDeleteFrom.remove(resourceToDelete, (Property)null, (RDFNode)null);
			modelToDeleteFrom.remove((Resource)null, (Property)null, resourceToDelete);
			deletedCount++;
		}
		long sizeAfterDelete = modelToDeleteFrom.size();
		long totalDeletedStatements = sizeBeforeDelete - sizeAfterDelete;
		if (deletedCount == checkedItems.size()) {
			MessageDialog.openInformation(composite.getShell(), "Success Deleting", "Successfully deleted " + deletedCount + " items, " + totalDeletedStatements + " statements.");
		} else if (deletedCount > 0) {
			MessageDialog.openWarning(composite.getShell(), "Deleted Some Items", "Deleted " + deletedCount + " items, " + totalDeletedStatements + " statements.\nFailed to delete " + (checkedItems.size() - deletedCount)+ " items.");
		} else {
			MessageDialog.openWarning(composite.getShell(), "Failed to Delete Any Items", "No items were deleted.");
		}
	}

	public void setNamedModel(NamedModel namedModel) {
		this.namedModel = namedModel;
	}
	
	public void handleEvent(Event event) {
		log.info("Selection: " + event.text + "; data=" + event.data);
		
//		if (event.widget instanceof Link) {
//			Link clickedLink = (Link)event.widget;
//			Object linkData = clickedLink.getData();
//			if (linkData ==null) {
//				log.warn("Clicked link has no data.");
//				return;
//			}
//			String linkValue = linkData.toString();
//			//reset to the first record
//			offset = 0;
//			if (currentSortColumn.equals(linkValue)) {
//				toggleCurrentSortDirection();
//				
//			} else {
//				currentSortColumn = linkValue;
//				currentSortDirection = DEFAULT_SORT_DIRECTION;
//			}
//			log.info("Sort by " + currentSortColumn + " " + currentSortDirection);
//			//no need to refresh the form, since we are only changing sort
//			doQuery();
//			showTable();
//		}
	}

}
