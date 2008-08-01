package org.inqle.ui.rap.table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.XmlDocumentSerializer;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.http.lookup.OwlInstanceLookup;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.CreateOwlInstanceAction;
import org.inqle.ui.rap.pages.BeanWizardPage;
import org.inqle.ui.rap.pages.DynaWizardPage;
import org.inqle.ui.rap.widgets.AResourceDialog;
import org.inqle.ui.rap.widgets.SearchBox;
import org.inqle.ui.rap.xml.SparqlXmlMerger;
import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * This generates a wizard page which has a table of data, for display only.  
 * The data in the table is represented by a RdfTable object,
 * which contains the result of a SPARQL query.
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public class LookupRdfPage extends DynaWizardPage implements SelectionListener{
	
	/**
	 * the class of items in the table's List field.
	 */
	protected Class<?> tableBeanClass = Map.class;

	private static final Logger log = Logger.getLogger(LookupRdfPage.class);

	private static final int COLUMN_WIDTH = 120;

//	protected TableViewer tableViewer;

	private Document xmlDocument;

//	private Text searchText;

	private Table table;

	private SearchBox searchBox;

	private Button enterNewClassButton;

	private String uriFieldName = "URI";

	private Button selectCreatedClassButton;

	private String createdUri;

	private Label selectNewSubjectLabel;

//	private Table table;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param title the title of this page
	 * @param titleImage
	 */
	public LookupRdfPage(String title, String description, ImageDescriptor titleImage) {
		super(title, titleImage);
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
		table = new Table(selfComposite, SWT.NONE);
		
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

		log.info("Refreshing table...");

		SparqlXmlTableLabelProvider labelProvider = new SparqlXmlTableLabelProvider();
		labelProvider.setXmlDocument(xmlDocument);
		
		//add columns
		if (table.getColumns().length == 0)  {
			for (String propertyName: labelProvider.getHeaderVariables()) {
				TableColumn column = new TableColumn(table,SWT.LEFT);
				propertyName = propertyName.replace("_", " ");
				column.setText(propertyName);
				column.setResizable(true);
				column.setWidth(COLUMN_WIDTH);
				
				//column.pack();
				log.debug("Added column: " + propertyName);
			}
		} else {
			log.info("Skip adding columns");
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
	public void setXmlDocument(Document xmlDocument) {
		//this.rdfTable = rdfTable;
		this.xmlDocument = xmlDocument;
	}
	
	public String getSearchTextValue() {
		return searchBox.getSearchText();
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(enterNewClassButton)) {
			log.info("Clicked 'new resource' button");
			table.deselectAll();
//			OntModel ontModel = ModelFactory.createOntologyModel();
//			OntClass ontClass = ontModel.createClass(RDF.DATA_SUBJECT);
//			AResourceDialog resourceDialog = new AResourceDialog(selfComposite.getShell(), ontClass);
//			resourceDialog.open();
//			if (resourceDialog.getReturnCode() == Window.OK) {
//				log.info("Created new <" + RDF.DATA_SUBJECT + ">:\n" + JenabeanWriter.modelToString(ontModel));
//			}
			Persister persister = Persister.getInstance();
			CreateOwlInstanceAction createOwlInstanceAction = new CreateOwlInstanceAction(
					selfComposite.getShell(), 
					Data.DATA_SUBJECT_DATASET_ROLE_ID, 
					RDF.DATA_SUBJECT);
			createOwlInstanceAction.run();
			this.createdUri = createOwlInstanceAction.getNewUri();
			selectCreatedClassButton.setText(createdUri);
			selectCreatedClassButton.setSelection(true);
			selectNewSubjectLabel.setVisible(true);
			selectCreatedClassButton.setVisible(true);
		} else if (clickedObject.equals(table)) {
			selectCreatedClassButton.setSelection(false);
			log.info("Clicked table row.  getSubjectUri()=" + getSubjectUri());
		} else if (clickedObject.equals(selectCreatedClassButton)) {
			table.deselectAll();
			log.info("Clicked radio button.  getSubjectUri()=" + getSubjectUri());
		} else {
			log.info("Clicked search button");
			//do the search
			Map<String, String> params = new HashMap<String, String>();
			params.put(InqleInfo.PARAM_SEARCH_DATA_SUBJECT, getSearchTextValue());
			
			Document localDocument = null;
			
			String localResultXml = OwlInstanceLookup.lookup(
					getSearchTextValue(), 
					RDF.DATA_SUBJECT, 
					Data.DATA_SUBJECT_DATASET_ROLE_ID, 
					10, 
					0);
			log.info("Retrieved this result set from LOCAL query:\n" + localResultXml);
			InputStream in = new ByteArrayInputStream(localResultXml.getBytes());
			DocumentBuilder builder;
			try {
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				localDocument = builder.parse(in);
			} catch (Exception e) {
				log.error("Unable to build/parse XML from local SPARQL query", e);
			}
	    
			log.info("Looking up classes from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
			Document remoteDocument = Requestor.retrieveXml(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
			
			// XERCES 1 or 2 additional classes.
//			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
//			of.setIndent(1);
//			of.setIndenting(true);
////			of.setDoctype(null,"users.dtd");
//			XMLSerializer serializer = new XMLSerializer(System.out,of);
//			log.info("Received Document object:");
//			// As a DOM Serializer
//			try {
//				serializer.asDOMSerializer();
//				serializer.serialize( remoteDocument.getDocumentElement() );
//			} catch (IOException e) {
//				log.warn("Unable to serialize received XML Document");
//			}
			
			log.info("Received Document object:\n" + XmlDocumentSerializer.xmlToString(remoteDocument));
			
			Document mergedDocument = null;
			if (localDocument != null && remoteDocument != null) {
				log.info("merging...");
				mergedDocument = SparqlXmlMerger.merge(localDocument, remoteDocument);
	    	try {
					log.info("Merged 2 documents into:\n" + XmlDocumentSerializer.xmlToString(mergedDocument));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    } else if (remoteDocument != null) {
	    	mergedDocument = remoteDocument;
	    } else {
	    	 mergedDocument = localDocument;
	    }
			if (localDocument == null) {
				log.warn("Received NULL from the local INQLE server and from Central INQLE Server.  " +
						"Perhaps your internet connection is down.");
			}
			
			setXmlDocument(mergedDocument);
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

	private String getSubjectUri() {
		if (createdUri != null && selectCreatedClassButton.getSelection()) {
			return createdUri;
		}
		TableItem[] selectedItems = table.getSelection();
		if (selectedItems == null || selectedItems.length < 1) {
			return null;
		}
		TableItem selectedItem = selectedItems[0];
		Map<String, String> selectedItemVals = (Map<String, String>)selectedItem.getData();
		log.info("getting val for " + uriFieldName + "...");
		String val = selectedItemVals.get(uriFieldName);
		if (val != null) {
			val = val.trim();
		}
		return val;
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

}
