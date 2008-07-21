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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.pages.BeanWizardPage;
import org.inqle.ui.rap.pages.DynaWizardPage;
import org.w3c.dom.Document;

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

//	protected TableViewer tableViewer;

	private Document xmlDocument;

	private Text searchText;

//	private Table table;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param title the title of this page
	 * @param titleImage
	 */
	public LookupRdfPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	/**
	 * Add form elements upon initial construction
	 */
	@Override
	public void addElements() {
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		
		new Label(selfComposite, SWT.NONE).setText("Search for a subject");
		searchText = new Text(selfComposite, SWT.BORDER);
		searchText.setLayoutData(gridData);
		Button searchButton = new Button(selfComposite, SWT.PUSH | SWT.BORDER);
		searchButton.setText("Search");
		searchButton.addSelectionListener(this);
//		composite.setLayout (new GridLayout(2, false));
//		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
	}
	

	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
//		if (table != null) {
//			table.clearAll();
//		}

		log.info("Showing table...");
		Table table = new Table(selfComposite, SWT.NONE);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		SparqlXmlTableLabelProvider labelProvider = new SparqlXmlTableLabelProvider();
		labelProvider.setXmlDocument(xmlDocument);
		
		//add columns
		for (String propertyName: labelProvider.getHeaderVariables()) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			column.setText(propertyName);
			column.setResizable(true);
			column.setWidth(200);
			
			//column.pack();
			log.debug("Added column: " + propertyName);
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
		return searchText.getText();
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
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
