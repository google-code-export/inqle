package org.inqle.ui.rap.table;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
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
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.pages.BeanWizardPage;
import org.inqle.ui.rap.pages.DynaWizardPage;
import org.inqle.ui.rap.widgets.SearchBox;
import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.AnonId;
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

	private Button enterNewClass;

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
		
		enterNewClass = new Button(selfComposite, SWT.RADIO);
		enterNewClass.setText("Enter a new subject");
		enterNewClass.addSelectionListener(this);
		
		table = new Table(selfComposite, SWT.NONE);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addSelectionListener(this);
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
		if (clickedObject.equals(enterNewClass)) {
			log.info("Clicked radio button");
			table.deselectAll();
		} else if (clickedObject.equals(table)) {
			log.info("Clicked table row #" + table.getSelectionIndex());
			enterNewClass.setSelection(false);
		} else {
			log.info("Clicked search button");
			//do the search
			Map<String, String> params = new HashMap<String, String>();
			params.put(InqleInfo.PARAM_SEARCH_RDF_CLASS, getSearchTextValue());
			
			log.info("posting data to " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
			
			Document document = Requestor.retrieveXml(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
			if (document == null) {
				log.warn("Received NULL from the Central INQLE Server.  Perhaps your internet connection is down.");
			}
			
			// XERCES 1 or 2 additionnal classes.
			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
			of.setIndent(1);
			of.setIndenting(true);
			of.setDoctype(null,"users.dtd");
			XMLSerializer serializer = new XMLSerializer(System.out,of);
			log.info("Received Document object:");
			// As a DOM Serializer
			try {
				serializer.asDOMSerializer();
				serializer.serialize( document.getDocumentElement() );
			} catch (IOException e) {
				log.warn("Unable to serialize received XML Document");
			}
			
			setXmlDocument(document);
			refreshTableData();
		}
	}
	
	/**
	 * Create in the provided OntModel, 
	 * the RDF statements that represent the selected subject
	 * @param ontModel
	 * @return
	 */
	public Individual getIndividual(OntModel ontModel) {
		Resource resource = ontModel.createResource(getSubjectUri());
		Individual selectedIndividual = ontModel.createIndividual(resource);
	}

	private String getSubjectUri() {
		TableItem[] selectedItems = table.getSelection();
		if (selectedItems == null || selectedItems.length < 1) {
			return null;
		}
		TableItem selectedItem = selectedItems[0];
		Map<String, String> selectedItemVals = (Map<String, String>)selectedItem.getData();
	}

}
