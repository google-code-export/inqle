package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.ListMapUtil;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.http.lookup.LookupServlet;
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.actions.CreateSubpropertyAction;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvReaderWizard;
import org.inqle.ui.rap.csv.CsvReader;
import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public abstract class SubjectPropertiesPage extends DynaWizardPage implements SelectionListener {

	private static Logger log = Logger.getLogger(SubjectPropertiesPage.class);

	protected String subjectClassUri;

	protected Composite formComposite;

	protected Button enterNewDataPropertyButton;
	protected Button enterNewSubjectPropertyButton;
	
	protected String[] headers;
	
	protected List<IDataFieldShower> dataFieldShowers = new ArrayList<IDataFieldShower>();

	protected Text enterNewDataPropertyButtonExplanation;
	protected Text enterNewSubjectPropertyButtonExplanation;

	private ScrolledComposite scrolledComposite;
	
	public SubjectPropertiesPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertiesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
//	public void addElements() {
	public void createControl(Composite parent) {
		//log.info("SubjectPropertiesPage.createControl...");
		
		scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		GridLayout gl = new GridLayout(1, true);
		scrolledComposite.setLayout(gl);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		scrolledComposite.setLayoutData(gridData);
//		formComposite = new Composite(formComposite, SWT.NONE);
		formComposite = new Composite(scrolledComposite, SWT.NONE);
		
		scrolledComposite.setContent(formComposite);
		
		gl = new GridLayout(2, false);
		formComposite.setLayout(gl);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		formComposite.setLayoutData(gridData);
		
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
//				Rectangle r = scrolledComposite.getClientArea();
//				scrolledComposite.setMinSize(formComposite.computeSize(r.width, SWT.DEFAULT));
				scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});

		
		
		enterNewDataPropertyButton = new Button(formComposite, SWT.PUSH);
		enterNewDataPropertyButton.setText("Enter a new property for data measurements about the subject");
//		enterNewDataPropertyButton.setAlignment(SWT.RIGHT);
		enterNewDataPropertyButton.addSelectionListener(this);
		enterNewDataPropertyButtonExplanation = new Text(formComposite, SWT.WRAP | SWT.READ_ONLY);
		new Label(formComposite, SWT.NONE);
		new Text(formComposite, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY).setText(
				"These are properties that are measured, about the subject.  These values DO change with time.  " +
				"Examples: 'stock price', 'annual Gross Domestic Product (GDP)'");
		
		enterNewSubjectPropertyButton = new Button(formComposite, SWT.PUSH);
		enterNewSubjectPropertyButton.setText("Enter a new, fixed property for the subject");
//		enterNewSubjectPropertyButton.setAlignment(SWT.RIGHT);
		enterNewSubjectPropertyButton.addSelectionListener(this);
		enterNewSubjectPropertyButtonExplanation = new Text(formComposite, SWT.WRAP | SWT.READ_ONLY);
		new Label(formComposite, SWT.NONE);
		new Text(formComposite, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY).setText(
				"These are properties that identify the subject and generally do NOT change with time.  " +
				"Examples: 'has ticker symbol', 'has country code'");
		
		setControl(scrolledComposite);
		onEnterPageFromPrevious();
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		//log.info("Entering SubjectPropertiesPage");
		if (enterNewDataPropertyButtonExplanation==null || enterNewSubjectPropertyButtonExplanation == null) {
			//log.info("Page not yet initialized.  Exiting SubjectPropertiesPage.");
			return;
		}
		
//		formComposite.dispose();
//		formComposite.redraw();
		
		String currentSubjectClassUri = getSubjectUri();
		//log.info("currentSubjectClassUri=" + currentSubjectClassUri);
		if (currentSubjectClassUri == null || currentSubjectClassUri.equals(subjectClassUri)) {
			return;
		}
		removePropertyFormElements();
		
		subjectClassUri = currentSubjectClassUri;
		enterNewDataPropertyButtonExplanation.setText(getEnterNewDataPropertyButtonLabel());
		enterNewSubjectPropertyButtonExplanation.setText(getEnterNewSubjectPropertyButtonLabel());
		
		log.trace("lookup properties from 4 places...");
		String dataAndSubjectPropertiesXml = PropertyLookup.lookupAllDataProperties(
				subjectClassUri, 
				10, 
				0);
		Document dataAndSubjectPropertiesDocument = XmlDocumentUtil.getDocument(dataAndSubjectPropertiesXml);
		log.trace("dataAndSubjectPropertiesXml=" + dataAndSubjectPropertiesXml);
		String otherSubjectPropertiesXml = PropertyLookup.lookupPropertiesInSchemaDatasets(
				subjectClassUri, 
				10, 
				0);
		Document otherSubjectPropertiesDocument = XmlDocumentUtil.getDocument(otherSubjectPropertiesXml);
		log.trace("otherSubjectPropertiesDocument=" + otherSubjectPropertiesDocument);
		Document allLocalPropertiesDocument = SparqlXmlUtil.merge(dataAndSubjectPropertiesDocument, otherSubjectPropertiesDocument);
		
		log.trace("Looking up remote Data & Subject & preferred ontology properties for class <" + subjectClassUri + "> from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
		//do the search
		Map<String, String> params = new HashMap<String, String>();
		params.put(LookupServlet.PARAM_PROPERTIES_OF_DATA_AND_PREFERRED_ONTOLOGY, subjectClassUri);
//		Document remoteDataAndSubjectPropertiesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
		Document allRemotePropertiesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
		log.trace("Received Document object:\n" + XmlDocumentUtil.xmlToString(allRemotePropertiesDocument));
		
//		log.trace("Looking up in remote schema files properties of class <" + subjectClassUri + "> from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
//		//do the search
//		params = new HashMap<String, String>();
//		params.put(LookupServlet.PARAM_PROPERTIES_OF_SUBJECT_FROM_SCHEMA_FILES, subjectClassUri);
//		Document remotePropertiesFromSchemaFilesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
//		log.trace("Received Document object:\n" + XmlDocumentUtil.xmlToString(remotePropertiesFromSchemaFilesDocument));
		
//		Document allRemotePropertiesDocument = SparqlXmlUtil.merge(remoteDataAndSubjectPropertiesDocument, remotePropertiesFromSchemaFilesDocument);
				
//		log.info("Merging all LOCAL results:\n" + XmlDocumentUtil.xmlToString(allLocalPropertiesDocument));
		List<SortedMap<String, String>> localRowValues = SparqlXmlUtil.getRowValues(allLocalPropertiesDocument);
		
//		log.info("...with all REMOTE results:\n" + XmlDocumentUtil.xmlToString(allRemotePropertiesDocument));
		List<SortedMap<String, String>> remoteRowValues = SparqlXmlUtil.getRowValues(allRemotePropertiesDocument);
//		Document allPropertiesDocument = SparqlXmlUtil.merge(allLocalPropertiesDocument, allRemotePropertiesDocument);
//		log.info("Merged all results into:\n" + XmlDocumentUtil.xmlToString(allPropertiesDocument));

		List<SortedMap<String, String>> rowValues = ListMapUtil.merge(localRowValues, remoteRowValues);
		
		makePropertyFormElements(rowValues);
		
		refreshScrolledComposite();
//		scrolledComposite.pack(true);
	}

	private void refreshScrolledComposite() {
		scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		formComposite.layout();
	}

	public void addElements() {
		//do nothing; this was handled by createControl()
	}
	
	private String getEnterNewSubjectPropertyButtonLabel() {
		return "Create/register a new property of <" + subjectClassUri + ">";
	}
	
	private String getEnterNewDataPropertyButtonLabel() {
		return "Create/register a new property of data measurements about <" + subjectClassUri + ">";
	}

	/**
	 * Get the URI of the subject
	 * @return
	 */
	private String getSubjectUri() {
		FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
		return wizard.getSubjectClassUri(this);
	}

	protected void makePropertyFormElements(List<SortedMap<String, String>> rowValues) {
		if (rowValues==null) return;
		
		dataFieldShowers = new ArrayList<IDataFieldShower>();
		CsvReader csvImporter = getCsvReader();
		headers = csvImporter.getHeaders();
		for (Map<String, String> row: rowValues) {
			String uri = row.get(PropertyLookup.QUERY_HEADER_URI);
			if (uri != null) uri=uri.trim();
			String label = row.get(PropertyLookup.QUERY_HEADER_LABEL);
			if (label != null) label=label.trim();
			String comment = row.get(PropertyLookup.QUERY_HEADER_COMMENT);
			if (comment != null) comment=comment.trim();
			String propertyType = row.get(PropertyLookup.QUERY_HEADER_PROPERTY_TYPE);
			if (propertyType != null) propertyType=propertyType.trim();
			//log.info("Creating form element w/\nuri=" + uri + "\nlabel=" + label + "\ncomment=" + comment + "\npropertyType=" + propertyType);
			addPropertyFormItem(uri, label, comment, propertyType);
		}
	}

	protected void removePropertyFormElements() {
		if (dataFieldShowers == null) {
			return;
		}
		for (IDataFieldShower dataFieldShower: dataFieldShowers) {
			dataFieldShower.remove();
		}
	}
	
	public IDataFieldShower[] getDataFields() {
		IDataFieldShower[] dataFieldShowerArray = {};
		return dataFieldShowers.toArray(dataFieldShowerArray);
	}
	
	private CsvReader getCsvReader() {
		ICsvReaderWizard loadCsvFileWizard = (ICsvReaderWizard)getWizard();
		return loadCsvFileWizard.getCsvReader();
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(enterNewDataPropertyButton)) {			
			//log.info("Clicked 'new data property' button");
			CreateSubpropertyAction createSubpropertyAction = new CreateSubpropertyAction(
					formComposite.getShell(), 
					Data.DATA_PROPERTY_DATASET_ROLE_ID, 
					RDF.DATA_PROPERTY);
			
			//add the domain class
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass domainSubject = ontModel.createClass();
			Resource subjectResource = ResourceFactory.createResource(getSubjectUri());
			Property subjectProperty = ResourceFactory.createProperty(RDF.HAS_SUBJECT);
			domainSubject.addProperty(subjectProperty, subjectResource);
			createSubpropertyAction.setDomainClass(domainSubject);
			
			createSubpropertyAction.run();
			OntResource newProperty = createSubpropertyAction.getOntResource();
//			log.trace("Adding form item for: uri=" + newProperty.getURI() + 
//					"label=" + newProperty.getLabel("EN") +
//					"description=" + newProperty.getComment("EN"));
			addPropertyFormItem(newProperty.getURI(), 
					newProperty.getLabel("EN"), 
					newProperty.getComment("EN"),
					RDF.DATA_PROPERTY);
//			formComposite.layout();
//			formComposite.pack(true);
//			formComposite.redraw();
			refreshScrolledComposite();
		}
		
		if (clickedObject.equals(enterNewSubjectPropertyButton)) {			
//			log.info("Clicked 'new subject property' button");
			CreateSubpropertyAction createSubpropertyAction = new CreateSubpropertyAction(
					formComposite.getShell(), 
					Data.DATA_PROPERTY_DATASET_ROLE_ID, 
					RDF.SUBJECT_PROPERTY);
			
			//add the domain class
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass domainSubject = ontModel.createClass(getSubjectUri());
			createSubpropertyAction.setDomainClass(domainSubject);
			
			createSubpropertyAction.run();
			OntResource newProperty = createSubpropertyAction.getOntResource();
			addPropertyFormItem(newProperty.getURI(), 
					newProperty.getLabel("EN"), 
					newProperty.getComment("EN"),
					RDF.SUBJECT_PROPERTY);
			refreshScrolledComposite();
		}
	}

	/**
	 * add the new property to the form
	 * @param uri
	 * @param label
	 * @param comment
	 */
	protected abstract void addPropertyFormItem(
			String uri, 
			String label, 
			String comment,
			String propertyTypeUri);
}
