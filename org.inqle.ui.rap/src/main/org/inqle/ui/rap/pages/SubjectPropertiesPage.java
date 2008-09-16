package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.CreateSubpropertyAction;
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
	
	protected List<IDataFieldShower> dataFieldShowers;

	protected Label enterNewDataPropertyButtonLabel;
	protected Label enterNewSubjectPropertyButtonLabel;

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
		log.info("SubjectPropertiesPage.createControl...");
		
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
		enterNewDataPropertyButtonLabel = new Label(formComposite, SWT.WRAP);
		new Label(formComposite, SWT.NONE);
		new Label(formComposite, SWT.WRAP).setText(
				"These are properties that are measured, about the subject.  These values DO change with time.  " +
				"Examples: 'stock price', 'annual Gross Domestic Product (GDP)'");
		
		enterNewSubjectPropertyButton = new Button(formComposite, SWT.PUSH);
		enterNewSubjectPropertyButton.setText("Enter a new, fixed property for the subject");
//		enterNewSubjectPropertyButton.setAlignment(SWT.RIGHT);
		enterNewSubjectPropertyButton.addSelectionListener(this);
		enterNewSubjectPropertyButtonLabel = new Label(formComposite, SWT.NONE);
		new Label(formComposite, SWT.NONE);
		new Label(formComposite, SWT.NONE).setText(
				"These are properties that identify the subject and generally do NOT change with time.  " +
				"Examples: 'has ticker symbol', 'has country code'");
		
		setControl(scrolledComposite);
		onEnterPageFromPrevious();
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering SubjectPropertiesPage");
		if (enterNewDataPropertyButtonLabel==null || enterNewSubjectPropertyButtonLabel == null) {
			log.info("Page not yet initialized.  Exiting SubjectPropertiesPage.");
			return;
		}
		
//		formComposite.dispose();
//		formComposite.redraw();
		
		String currentSubjectClassUri = getSubjectUri();
		log.info("currentSubjectClassUri=" + currentSubjectClassUri);
		if (currentSubjectClassUri == null || currentSubjectClassUri.equals(subjectClassUri)) {
			return;
		}
		removePropertyFormElements();
		
		subjectClassUri = currentSubjectClassUri;
		enterNewDataPropertyButtonLabel.setText(getEnterNewDataPropertyButtonLabel());
		enterNewSubjectPropertyButtonLabel.setText(getEnterNewSubjectPropertyButtonLabel());
		
		log.info("lookup properties from 4 places...");
		String dataAndSubjectPropertiesXml = PropertyLookup.lookupAllDataProperties(
				subjectClassUri, 
				10, 
				0);
		Document dataAndSubjectPropertiesDocument = XmlDocumentUtil.getDocument(dataAndSubjectPropertiesXml);
		log.info("dataAndSubjectPropertiesXml=" + dataAndSubjectPropertiesXml);
		String otherSubjectPropertiesXml = PropertyLookup.lookupPropertiesInSchemaFiles(
				subjectClassUri, 
				10, 
				0);
		Document otherSubjectPropertiesDocument = XmlDocumentUtil.getDocument(otherSubjectPropertiesXml);
		log.info("otherSubjectPropertiesDocument=" + otherSubjectPropertiesDocument);
		Document allLocalPropertiesDocument = SparqlXmlUtil.merge(dataAndSubjectPropertiesDocument, otherSubjectPropertiesDocument);
		
		log.info("Looking up remote Data & Subject properties of class <" + subjectClassUri + "> from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
		//do the search
		Map<String, String> params = new HashMap<String, String>();
		params.put(InqleInfo.PARAM_DATA_AND_SUBJECT_PROPERTIES_OF_SUBJECT, subjectClassUri);
		Document remoteDataAndSubjectPropertiesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
		log.info("Received Document object:\n" + XmlDocumentUtil.xmlToString(remoteDataAndSubjectPropertiesDocument));
		
		log.info("Looking up in remote schema files properties of class <" + subjectClassUri + "> from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
		//do the search
		params = new HashMap<String, String>();
		params.put(InqleInfo.PARAM_PROPERTIES_OF_SUBJECT_FROM_SCHEMA_FILES, subjectClassUri);
		Document remotePropertiesFromSchemaFilesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
		log.info("Received Document object:\n" + XmlDocumentUtil.xmlToString(remotePropertiesFromSchemaFilesDocument));
		
		Document allRemotePropertiesDocument = SparqlXmlUtil.merge(remoteDataAndSubjectPropertiesDocument, remotePropertiesFromSchemaFilesDocument);
				
		Document allPropertiesDocument = SparqlXmlUtil.merge(allLocalPropertiesDocument, allRemotePropertiesDocument);
		log.info("Merged all results into:\n" + XmlDocumentUtil.xmlToString(allPropertiesDocument));

		List<Map<String, String>> rowValues = SparqlXmlUtil.getRowValues(allPropertiesDocument);
		
		makePropertyFormElements(rowValues);
		
		scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		formComposite.layout();
//		scrolledComposite.pack(true);
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

	protected void makePropertyFormElements(List<Map<String, String>> rowValues) {
		dataFieldShowers = new ArrayList<IDataFieldShower>();
		CsvReader csvImporter = getCsvReader();
		headers = csvImporter.getHeaders();
		for (Map<String, String> row: rowValues) {
			String uri = row.get(PropertyLookup.QUERY_HEADER_URI);
			String label = row.get(PropertyLookup.QUERY_HEADER_LABEL);
			String comment = row.get(PropertyLookup.QUERY_HEADER_COMMENT);
			String subjectType = row.get(PropertyLookup.QUERY_HEADER_PROPERTY_TYPE);
			addPropertyFormItem(uri, label, comment, subjectType);
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
			log.info("Clicked 'new data property' button");
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
			addPropertyFormItem(newProperty.getURI(), 
					newProperty.getLabel("EN"), 
					newProperty.getComment("EN"),
					RDF.DATA_PROPERTY);
		}
		
		if (clickedObject.equals(enterNewSubjectPropertyButton)) {			
			log.info("Clicked 'new subject property' button");
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
