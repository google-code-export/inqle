package org.inqle.ui.rap.pages;

import java.util.ArrayList;
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
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.http.lookup.LookupInfo;
import org.inqle.http.lookup.LookupServlet;
import org.inqle.http.lookup.SubjectLookup;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.actions.CreateSubclassAction;
import org.inqle.ui.rap.table.ListMapTableLabelProvider;
import org.inqle.ui.rap.table.SparqlXmlTableLabelProvider;
import org.inqle.ui.rap.widgets.SearchBox;
import org.w3c.dom.Document;

/**
 * This page permits the user to search for an RDF subject class, and to specify a new RDF subject class.
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public abstract class SubjectClassPage extends DynaWizardPage implements SelectionListener{
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = Map.class;

	private static final Logger log = Logger.getLogger(SubjectClassPage.class);

	private static final int COLUMN_WIDTH = 180;

	//must exceed this count of search results, or a 2nd remote query is done
//	private static final int THRESHOLD_DO_REMOTE_SCHEMA_LOOKUP = 0;

	private static final String MAX_NUMBER_REMOTE_SUBJECTS = "100";

//	protected TableViewer tableViewer;

//	private Document xmlDocument;

//	private Text searchText;

	private Table table;

	private SearchBox searchBox;

	private Button enterNewClassButton;

	private String uriFieldName = "URI";

	private Button selectCreatedClassButton;

	private String createdUri;

	private Label selectNewSubjectLabel;

	private List<SortedMap<String, String>> dataRecords;

	private List<String> headerVariables;

//	private static String TITLE = "Type of Subject";
//	private static String DESCRIPTION = "Find and select the type of subject that this data is about.";
//	private Table table;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param title the title of this page
	 * @param titleImage
	 */
	public SubjectClassPage(String title, String description) {
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
		
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		
		searchBox = new SearchBox(selfComposite, SWT.NONE, "Find a subject", "Search");
		searchBox.addSelectionListener(this);
		new Label(selfComposite, SWT.NONE).setText("Choose an existing subject from this table...");
		table = new Table(selfComposite, SWT.WRAP);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addSelectionListener(this);
		
		new Label(selfComposite, SWT.NONE).setText("...or create/register a new subject...");
		enterNewClassButton = new Button(selfComposite, SWT.PUSH);
		enterNewClassButton.setText("Enter a new subject");
		enterNewClassButton.addSelectionListener(this);
		
		selectNewSubjectLabel = new Label(selfComposite, SWT.NONE);
		selectNewSubjectLabel.setText("...or choose this subject you just created...");
		selectNewSubjectLabel.setVisible(false);
		selectCreatedClassButton = new Button(selfComposite, SWT.RADIO);
		selectCreatedClassButton.setVisible(false);
		selectCreatedClassButton.addSelectionListener(this);
//		composite.setLayout (new GridLayout(2, false));
//		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
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

//		SparqlXmlTableLabelProvider labelProvider = new SparqlXmlTableLabelProvider();
//		labelProvider.setXmlDocument(xmlDocument);
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

	/**
	 * Set the rows of data in the table.  The class of items should be set in the setRowItemClass() method
	 * @param beans
	 */
//	public void setXmlDocument(Document xmlDocument) {
//		//this.rdfTable = rdfTable;
//		this.xmlDocument = xmlDocument;
//	}
	
	public String getSearchTextValue() {
		return searchBox.getSearchText();
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(enterNewClassButton)) {
			//log.info("Clicked 'new resource' button");
			table.deselectAll();
//			OntModel ontModel = ModelFactory.createOntologyModel();
//			OntClass ontClass = ontModel.createClass(RDF.SUBJECT);
//			AOntResourceDialog resourceDialog = new AOntResourceDialog(selfComposite.getShell(), ontClass);
//			resourceDialog.open();
//			if (resourceDialog.getReturnCode() == Window.OK) {
//				log.info("Created new <" + RDF.SUBJECT + ">:\n" + JenabeanWriter.modelToString(ontModel));
//			}

			CreateSubclassAction createSubclassAction = new CreateSubclassAction(
					selfComposite.getShell(), 
					Data.DATA_SUBJECT_DATASET_ROLE_ID, 
					RDF.SUBJECT);
			createSubclassAction.run();
			
			if (createSubclassAction.getNewUri() != null) {
				this.createdUri = createSubclassAction.getNewUri();
				
				selectCreatedClassButton.setText(createdUri);
				selectCreatedClassButton.setSelection(true);
				selectNewSubjectLabel.setVisible(true);
				selectCreatedClassButton.setVisible(true);
			}
		} else if (clickedObject.equals(table)) {
			selectCreatedClassButton.setSelection(false);
			//log.info("Clicked table row.  getSubjectUri()=" + getSubjectUri());
		} else if (clickedObject.equals(selectCreatedClassButton)) {
			table.deselectAll();
			//log.info("Clicked radio button.  getSubjectUri()=" + getSubjectUri());
		} else {
			//log.info("Clicked search button");

			//this looks up subclasses of DataSubject, in this internal dataset: Data.DATA_SUBJECT_DATASET_ROLE_ID
			String localDataSubjectXml = SubjectLookup.lookupSubjectsInSubjectsDataset(
					getSearchTextValue(), 
					10, 
					0);
			//log.info("Retrieved this result set from LOCAL query:\n" + localDataSubjectXml);
			Document localDataSubjectDocument = XmlDocumentUtil.getDocument(localDataSubjectXml);;

			//this looks up all RDF classes
			String localRdfClassXml = SubjectLookup.lookupSubclassesInSchemaDatasets(
					getSearchTextValue(), 
					10, 
					0);
			//log.info("Retrieved this result set from LOCAL query:\n" + localRdfClassXml);
			Document localRdfClassDocument = XmlDocumentUtil.getDocument(localRdfClassXml);
			
			Document localDocument = SparqlXmlUtil.merge(localDataSubjectDocument, localRdfClassDocument);
			List<SortedMap<String, String>> localRecords = SparqlXmlUtil.getRowValues(localDocument);
			
			//read the header variables from the merged local document
			headerVariables = SparqlXmlUtil.getHeaderVariables(localDocument);
			
			//log.info("Looking up classes from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
			//do the search
			Map<String, String> params = new HashMap<String, String>();
			params.put(LookupServlet.PARAM_SEARCH_DATA_AND_PREFERRED_ONTOLOGY_CLASS, getSearchTextValue());
			params.put(LookupServlet.PARAM_SEARCH_COUNT_RESULTS, MAX_NUMBER_REMOTE_SUBJECTS);
			
			Document remoteDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
			//log.info("Received Document object:\n" + XmlDocumentUtil.xmlToString(remoteDocument));
			List<SortedMap<String, String>> remoteRecords = SparqlXmlUtil.getRowValues(remoteDocument);
			
			
		//DO THIS WHEN READY: lookup subject info from UMBEL subject service
//		params = new HashMap<String, String>();
//		params.put(LookupInfo.UMBEL_MODE_PARAM, LookupInfo.UMBEL_MODE_ALL);
//		params.put(LookupInfo.UMBEL_SEARCH_PARAM, getSearchTextValue());
//		Document umbelSearchDocument = Requestor.retrieveXmlViaGet(LookupInfo.UMBEL_SUBJECT_URL, params);

//			List<SortedMap<String, String>> interimRecords = ListMapUtil.merge(localRecords, remoteRecords);
//			dataRecords = interimRecords;
			
			dataRecords = ListMapUtil.merge(localRecords, remoteRecords);
			log.info("Merged 2 documents into:\n" + dataRecords);
			
//			Document mergedDocument = SparqlXmlUtil.merge(localDocument, remoteDocument);
//			log.info("Merged 2 documents into:\n" + XmlDocumentUtil.xmlToString(mergedDocument));
			
			//if insufficient results, do an additional query of the remote RDF Schema datafiles
//		  if (interimRecords.size() <= THRESHOLD_DO_REMOTE_SCHEMA_LOOKUP) {
//				//log.info("Doing remote RDF classes lookup...");
//				params = new HashMap<String, String>();
//				params.put(InqleInfo.PARAM_SEARCH_RDF_CLASS, getSearchTextValue());
//				Document remoteRdfClassesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
//				//log.info("Received Document object:\n" + XmlDocumentUtil.xmlToString(remoteRdfClassesDocument));
////				mergedDocument = SparqlXmlUtil.merge(mergedDocument, remoteRdfClassesDocument);
//				List<SortedMap<String, String>> remoteRdfClassesRecords = SparqlXmlUtil.getRowValues(remoteRdfClassesDocument);
//				dataRecords = ListMapUtil.merge(interimRecords, remoteRdfClassesRecords);
//		  } else {
//		  	dataRecords = interimRecords;
//		  }			
			
//			setXmlDocument(mergedDocument);
//			List<SortedMap<String, String>> records = SparqlXmlUtil.getRowValues(mergedDocument);
			
			refreshTableData();
		}
	}
	
//	/**
//	 * Create in the provided OntModel, 
//	 * the RDF statements that represent the selected subject
//	 * @param ontModel
//	 * @return
//	 */
//	public Individual getIndividual(OntModel ontModel) {
//		Resource resource = ontModel.createResource(getSubjectUri());
//		Individual selectedIndividual = ontModel.createIndividual(resource);
//	}

	public String getSubjectUri() {
		if (createdUri != null && selectCreatedClassButton.getSelection()) {
			return createdUri;
		}
		TableItem[] selectedItems = table.getSelection();
		if (selectedItems == null || selectedItems.length < 1) {
			return null;
		}
		TableItem selectedItem = selectedItems[0];
		Map<String, String> selectedItemVals = (Map<String, String>)selectedItem.getData();
		
		String val = selectedItemVals.get(uriFieldName);
		if (val != null) {
			val = val.trim();
		}
		//log.info("getting val for " + uriFieldName + " = " + val);
		return val;
	}
	
	@Override
	public boolean onNextPage() {
		String subjUri = getSubjectUri();
		if (subjUri == null || (! UriMapper.isUri(subjUri))) {
			return false;
		}
		return true;
	}

	/**
	 * the name of the column which contains the URI of the selected row
	 * @return
	 */
	public String getUriFieldName() {
		return uriFieldName;
	}

	public void setUriFieldName(String uriFieldName) {
		this.uriFieldName = uriFieldName;
	}

	public List<SortedMap<String, String>> getSortedMap() {
		return dataRecords;
	}


}
