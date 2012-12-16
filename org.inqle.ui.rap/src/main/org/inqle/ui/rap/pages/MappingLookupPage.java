package org.inqle.ui.rap.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.ListMapUtil;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.mapping.TableMapping;
import org.inqle.http.lookup.LookupServlet;
import org.inqle.http.lookup.Requestor;
import org.inqle.http.lookup.SubjectsSearcher;
import org.inqle.http.lookup.TableMappingsSearcher;
import org.inqle.rdf.RDF;
import org.inqle.ui.rap.actions.CreateSubclassAction;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvReaderWizard;
import org.inqle.ui.rap.csv.CsvReader;
import org.inqle.ui.rap.table.ListMapTableLabelProvider;
import org.inqle.ui.rap.widgets.SearchBox;
import org.w3c.dom.Document;

/**
 * This page permits the user to search for an RDF subject class, and to specify a new RDF subject class.
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public class MappingLookupPage extends DynaWizardPage implements SelectionListener{
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = Map.class;

	private static final Logger log = Logger.getLogger(MappingLookupPage.class);

	private static final int COLUMN_WIDTH = 180;

	//must exceed this count of search results, or a 2nd remote query is done
//	private static final int THRESHOLD_DO_REMOTE_SCHEMA_LOOKUP = 0;

//	private static final String MAX_NUMBER_REMOTE_MAPPINGS = "10";

//	protected TableViewer tableViewer;

//	private Document xmlDocument;

//	private Text searchText;

	private Table table;

	private List<SortedMap<String, String>> dataRecords;

	private List<String> headerVariables;

	private Button newImportButton;

	private static String TITLE = "Find a Matching Data Mapping";
	private static String DESCRIPTION = "If your text table has columns which match a saved import strategy, " +
			"you can reuse that strategy in this import.";
	
	public MappingLookupPage() {
		this(TITLE, DESCRIPTION);
	}
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param title the title of this page
	 * @param titleImage
	 */
	public MappingLookupPage(String title, String description) {
		super(title, null);
		setMessage(description);
	}

	/**
	 * Add form elements upon initial construction
	 */
	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		
		newImportButton = new Button(selfComposite, SWT.RADIO);
		newImportButton.setText("New import: do not use a matching import strategy.");
		newImportButton.setSelection(true);
		
		new Label(selfComposite, SWT.NONE).setText("Or reuse a matching import strategy (select below)");
		table = new Table(selfComposite, SWT.WRAP);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addSelectionListener(this);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		//this looks up subclasses of DataSubject, in this system datamodel: Data.DATA_SUBJECT_DATASET_ROLE_ID
		String localMappingsXml = TableMappingsSearcher.lookupMappings(
				getHeaderText(), 
				10, 
				0);
		//log.info("Retrieved this result set from LOCAL query:\n" + localDataSubjectXml);
		Document localMappingsDocument = XmlDocumentUtil.getDocument(localMappingsXml);
		List<SortedMap<String, String>> localRecords = SparqlXmlUtil.getRowValues(localMappingsDocument);
		
		//read the header variables from the merged local document
		headerVariables = SparqlXmlUtil.getHeaderVariables(localMappingsDocument);
		
		//TODO: support lookup of TableMappings from Central INQLE Server (need to add lookup at lookup service, uncomment below, and enhance getSelectedTableMapping()
		//log.info("Looking up classes from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
		//do the search
//		Map<String, String> params = new HashMap<String, String>();
//		params.put(LookupServlet.PARAM_SEARCH_DATA_MAPPINGS, getHeaderText());
//		params.put(LookupServlet.PARAM_SEARCH_COUNT_RESULTS, MAX_NUMBER_REMOTE_MAPPINGS);
//		Document remoteMappingsDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
//		//log.info("Received Document object:\n" + XmlDocumentUtil.xmlToString(remoteDocument));
//		List<SortedMap<String, String>> remoteRecords = SparqlXmlUtil.getRowValues(remoteMappingsDocument);
//		dataRecords = ListMapUtil.merge(localRecords, remoteRecords);
//		log.info("Merged 2 documents into:\n" + dataRecords);
		
		dataRecords = localRecords;
		refreshTableData();
	}

	private String getHeaderText() {
		ICsvReaderWizard wizard = (ICsvReaderWizard)getWizard();
		CsvReader csvReader = wizard.getCsvReader();
		if (csvReader==null) return null;
		return csvReader.getHeaderString();
	}

	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
		if (table != null) {
//			table.dispose();
			table.clearAll();
			table.removeAll();
		}

		//log.info("Refreshing table...");
		ListMapTableLabelProvider labelProvider = new ListMapTableLabelProvider();
		labelProvider.setRowElements(dataRecords);
		labelProvider.setHeaderVariables(headerVariables);
		
		//add columns
		if (table.getColumns().length == 0)  {
			for (String propertyName: labelProvider.getHeaderVariables()) {
				TableColumn column = new TableColumn(table,SWT.LEFT | SWT.WRAP);
				propertyName = propertyName.replace("_", " ");
				column.setText(propertyName);
				column.setResizable(true);
				column.setWidth(COLUMN_WIDTH);
				
				//column.pack();
				log.debug("Added column: " + propertyName);
			}
		} else {
			//log.info("Skip adding columns");
		}
		TableViewer tableViewer = new TableViewer(table);
		
		ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
		tableViewer.setContentProvider(olContentProvider);
		
		
		tableViewer.setLabelProvider(labelProvider);
		
		WritableList writableListInput = new WritableList(labelProvider.getRowElements(), tableBeanClass);
		tableViewer.setInput(writableListInput);
		tableViewer.refresh();
		//this.getShell().pack(true);
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(newImportButton)) {
			//log.info("Clicked 'new import' button");
			table.deselectAll();
		} else if (clickedObject.equals(table)) {
			newImportButton.setSelection(false);
			//log.info("Clicked table row.  getSubjectUri()=" + getSubjectUri());
		}
	}

	public String getSelectedValue(String fieldName) {
		
		TableItem[] selectedItems = table.getSelection();
		if (selectedItems == null || selectedItems.length < 1) {
			return null;
		}
		TableItem selectedItem = selectedItems[0];
		Map<String, String> selectedItemVals = (Map<String, String>)selectedItem.getData();
		
		String val = selectedItemVals.get(fieldName);
		if (val != null) {
			val = val.trim();
		}
		//log.info("getting val for " + uriFieldName + " = " + val);
		return val;
	}
	
	@Override
	public boolean onNextPage() {
		FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
		if (isTableMappingSelected()) {
			wizard.setTableMapping(getSelectedTableMapping());
		} else {
			wizard.setTableMapping(null);
		}
		return true;
	}

	public List<SortedMap<String, String>> getSortedMap() {
		return dataRecords;
	}

	public boolean isTableMappingSelected() {
		return (! newImportButton.getSelection());
	}
	
	private TableMapping getSelectedTableMapping() {
		if (newImportButton.getSelection()) {
			return null;
		} else {
			String localTableMappingId = getSelectedValue(TableMappingsSearcher.MAPPING_ID);
			Persister persister = Persister.getInstance();
			TableMapping selectedTableMapping = (TableMapping) persister.reconstitute(TableMapping.class, localTableMappingId, true);
			return selectedTableMapping;
		}
	}
}
