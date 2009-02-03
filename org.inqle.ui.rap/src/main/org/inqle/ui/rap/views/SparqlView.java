package org.inqle.ui.rap.views;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.inqle.data.rdf.jena.Datamodel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.actions.IDatamodelView;
import org.inqle.ui.rap.widgets.ResultSetTable;
import org.inqle.ui.rap.widgets.AScrolledTable.ColumnNameData;

import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author David Donohue
 * May 6, 2008
 */
public abstract class SparqlView extends ViewPart implements SelectionListener, IDatamodelView {

	private static final Logger log = Logger.getLogger(SparqlView.class);
	
//	private static final int COLUMN_WIDTH = 120;

	private static final int DEFAULT_RECORD_COUNT_INDEX = 0;
	private static final String[] RECORD_COUNT_OPTIONS = {"10", "20", "50", "100", "200", "500", "1000" };
	public static final String DESC = "DESC";
	public static final String ASC = "ASC";

	private static final String DEFAULT_SORT_DIRECTION = DESC;
	
	private Composite composite;
	
//	protected List<QuerySolution> rows = new ArrayList<QuerySolution>();

	//protected Persister persister;
	protected ResultSetTable resultSetTable;
	protected List<String> propertyNames;
	protected Button refreshButton;
	protected String currentSortColumn = "Creation_Date";
	protected String currentSortDirection = DEFAULT_SORT_DIRECTION;
	protected Combo recordCountCombo;
	protected Button previousButton;
	protected Button nextButton;
	protected Label resultDescription;
	
	private int offset = 0;

	private ResultSetRewindable resultSet;

	private Button deleteButton;

	protected Datamodel datamodel;

	private Button checkAllButton;

	private Button uncheckAllButton;

	protected boolean hideUriColumn = true;

	protected String linkColumn;
	protected String titleText;
	
	public boolean linkUriOnly = true;

	private Text title;
	
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
    
    showHeader();
		showForm();
		showResultDescription();
//		showTable();
//		refreshForm();
//		refreshView();
	}

	private void showHeader() {
		Composite headerComposite = new Composite(composite, SWT.NONE);
		headerComposite.setLayout(new GridLayout(1, true));
		title = new Text(headerComposite, SWT.BOLD);
		if (titleText==null || titleText.length()==0) return;
		title.setText(titleText);
	}

	private void showForm() {
		Composite formComposite = new Composite(composite, SWT.NONE);

		formComposite.setLayout(new GridLayout(6, false));
		
		//select list for number of records to show
		new Label(formComposite, SWT.NONE).setText("Number of rows");
		recordCountCombo = new Combo(formComposite, SWT.BORDER | SWT.READ_ONLY);
		recordCountCombo.setItems(RECORD_COUNT_OPTIONS);
		recordCountCombo.select(DEFAULT_RECORD_COUNT_INDEX);
		//GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		//formComposite.setLayoutData(gridData);
		refreshButton = new Button(formComposite, SWT.PUSH);
		refreshButton.setText("Refresh");
		refreshButton.addSelectionListener(this);
	}

	private void showResultDescription() {
		Composite descriptionComposite = new Composite(composite, SWT.NONE);
//		RowLayout layout = new RowLayout();
		GridLayout layout = new GridLayout(6, false);
		descriptionComposite.setLayout(layout);
		resultDescription = new Label(descriptionComposite, SWT.NONE);
		resultDescription.setText("No results found.");
		
		previousButton = new Button(descriptionComposite, SWT.PUSH);
		previousButton.setText("<--");
		previousButton.addSelectionListener(this);
		previousButton.setEnabled(false);
		nextButton = new Button(descriptionComposite, SWT.PUSH);
		nextButton.setText("-->");
		nextButton.addSelectionListener(this);
		nextButton.setEnabled(false);
		checkAllButton = new Button(descriptionComposite, SWT.PUSH);
		checkAllButton.setText("Check All");
		checkAllButton.addSelectionListener(this);
		checkAllButton.setEnabled(false);
		uncheckAllButton = new Button(descriptionComposite, SWT.PUSH);
		uncheckAllButton.setText("Uncheck All");
		uncheckAllButton.addSelectionListener(this);
		uncheckAllButton.setEnabled(false);
		deleteButton = new Button(descriptionComposite, SWT.PUSH);
		deleteButton.setText("Delete");
		deleteButton.addSelectionListener(this);
		deleteButton.setEnabled(false);
	}
	
	public void createTable() {
		resultSetTable = new ResultSetTable(composite, SWT.MULTI | SWT.CHECK, resultSet);
		resultSetTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		resultSetTable.setHideUriColumn(hideUriColumn);
		resultSetTable.setLinkColumn(linkColumn);
		resultSetTable.setLinkUriOnly(linkUriOnly);
		resultSetTable.renderTable(this);
		
		if (resultSet == null || resultSet.size() == 0) {
			return;
		}
		log.info("Rendered table for result set of size: " + resultSet.size());
	}
	

	public void recreateTable() {
		resultSetTable.dispose();
		createTable();
		resultSetTable.redraw();
//		resultSetTable.clearTable();
//		resultSetTable.setResultSet(resultSet);
//		resultSetTable.renderTable(this);
//		if (resultSet == null || resultSet.size() == 0) {
//			return;
//		}
//		log.info("Rendered table for result set of size: " + resultSet.size());
	}
	
	/**
	 * Override and set the default column width
	 * By default, set to 100
	 * @return
	 */
//	private int getColumnWidth() {
//		return COLUMN_WIDTH;
//	}

	public void showTable() {
//		if (resultSet == null || resultSet.size() == 0) {
//			doQuery();
//		}
//		
//		if (resultSet == null || resultSet.size() == 0) {
//			return;
//		}
//		
		if (resultSetTable == null) {
			createTable();
		} else {
			recreateTable();
		}
		//update the results description
		String message = "No items found.";
		if (resultSet != null && resultSet.size() > 0) {
			message = "Showing results " + (offset + 1) + " to " + (resultSet.size() + offset);
		}
		resultDescription.setText(message);
		
		
	}
	
	@Override
	public void setFocus() {
//		showTable();
	}
	
	public void doQuery() {
		String sparql = getSparql();
		log.info("Querying w/ SPARQL:" + sparql);
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(sparql);
		queryCriteria.addDatamodel(getDatamodel());
		this.resultSet = Queryer.selectResultSet(queryCriteria);
	}

	public Datamodel getDatamodel() {
		return datamodel;
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
		int selectedIndex = recordCountCombo.getSelectionIndex();
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
			return;
		}
		
		if (event.getSource() == previousButton) {
			offset = offset - getRecordCount();
			if (offset < 0) {
				offset = 0;
			}
			log.trace("Previous clicked");
			refreshView();
			return;
		}
		
		if (event.getSource() == nextButton) {
			if (resultSet.size() < getRecordCount()) {
				refreshForm();
				return;
			}
			offset = offset + getRecordCount();
			
			log.trace("Next clicked");
			refreshView();
			return;
		}
		
		if (event.getSource() == checkAllButton) {
//			log.info("Delete clicked");
			checkAllItems(true);
			return;
		}
		
		if (event.getSource() == uncheckAllButton) {
//		log.info("Delete clicked");
		checkAllItems(false);
		return;
	}
		
		if (event.getSource() == deleteButton) {
//			log.info("Delete clicked");
			deleteSelectedItems();
			return;
		}
		
		//was a column header clicked?
		if (event.getSource() instanceof Link) {
			Link clickedLink = (Link)event.getSource();
			Object linkData = clickedLink.getData();
			if (linkData ==null) {
				log.warn("Clicked link has no data.");
				return;
			}
			if (linkData instanceof ColumnNameData) {
				
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
				refreshView();
				return;
			}
		}
	}
	
	private void checkAllItems(boolean checked) {
		resultSetTable.checkAllRows(checked);
	}

	public void refreshView() {
		log.info("refresh view...");
		doQuery();
		
		showTable();
		refreshForm();
//		resultSetTable.recomputeSize();
		resultSetTable.layout(true, true);
		resultSetTable.setVisible(false);
		resultSetTable.redraw();
		resultSetTable.setVisible(true);
	}

	private void refreshForm() {
		log.info("refresh form...");
		if (resultSet==null) return;
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
		if (resultSet.size()>0 && resultSetTable != null && resultSetTable.countCheckboxes() > 0) {
			checkAllButton.setEnabled(true);
			uncheckAllButton.setEnabled(true);
			deleteButton.setEnabled(true);
		} else {
			checkAllButton.setEnabled(false);
			uncheckAllButton.setEnabled(false);
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
	
	public void deleteSelectedItems() {
		
		List<String> checkedItems = resultSetTable.getCheckedItems();
		if (checkedItems.size()==0) {
			MessageDialog.openWarning(composite.getShell(), "Unable to Delete Items", "No items are selected below for deletion.  Please check boxes next to any idem(s) you would like to delete.");
			composite.forceFocus();
			return;
		}
		Persister persister = Persister.getInstance();
		
		boolean confirmDelete = MessageDialog.openConfirm(composite.getShell(), "Delete these items?", "Are you sure you want to delete these " + checkedItems.size() + " items?\nTHIS CANNOT BE UNDONE!\n" + checkedItems);
		if (! confirmDelete) {
			composite.forceFocus();
			return;
		}
		
		Model modelToDeleteFrom = persister.getModel(getDatamodel());
		long sizeBeforeDelete = modelToDeleteFrom.size();
		int deletedCount = 0;
		log.info("Deleting these items: " + checkedItems);
		for (String uriToDelete: checkedItems) {
			if (uriToDelete==null || uriToDelete.length()==0) continue;
			
			Resource resourceToDelete = modelToDeleteFrom.getResource(uriToDelete);
//			if (resourceToDelete==null) {
//				resourceToDelete = modelToDeleteFrom.createResource(new AnonId(uriToDelete));
//			}
			if (resourceToDelete==null) {
				log.info("Unable to retrieve resource from model: " + uriToDelete);
				continue;
			}
			log.info("Deleting resource: " + resourceToDelete);
//			DonohueUtil.removeAllStatements(modelToDeleteFrom, resourceToDelete, (Property)null, (RDFNode)null);
//			DonohueUtil.removeAllStatements(modelToDeleteFrom, (Resource)null, (Property)null, resourceToDelete);
			
			modelToDeleteFrom.removeAll(resourceToDelete, (Property)null, (RDFNode)null);
			modelToDeleteFrom.removeAll((Resource)null, (Property)null, resourceToDelete);
			deletedCount++;
		}
		long sizeAfterDelete = modelToDeleteFrom.size();
		modelToDeleteFrom.close();
		long totalDeletedStatements = sizeBeforeDelete - sizeAfterDelete;
		if (deletedCount == checkedItems.size() && totalDeletedStatements > 0) {
			MessageDialog.openInformation(composite.getShell(), "Success Deleting", "Successfully deleted " + deletedCount + " items, " + totalDeletedStatements + " statements.");
			refreshView();
		} else if (deletedCount > 0 && totalDeletedStatements > 0) {
			MessageDialog.openWarning(composite.getShell(), "Deleted Some Items", "Deleted " + deletedCount + " items, " + totalDeletedStatements + " statements.\nFailed to delete " + (checkedItems.size() - deletedCount)+ " items.");
			refreshView();
		} else {
			MessageDialog.openWarning(composite.getShell(), "Failed to Delete Any Items", "No items were deleted.");
		}
		composite.forceFocus();
	}

	public void setDatamodel(Datamodel dataodel) {
		this.datamodel = dataodel;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
		if (title != null) {
			title.setText(titleText);
		}
	}

}
