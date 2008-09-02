package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
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
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.CreateSubpropertyAction;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvImporterWizard;
import org.inqle.ui.rap.csv.CsvImporter;
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
	
	public SubjectPropertiesPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertiesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
	public void addElements() {
		log.info("SubjectPropertiesPage.addElements...");
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		
		formComposite = new Composite(selfComposite, SWT.NONE);
		gl = new GridLayout(2, false);
		formComposite.setLayout(gl);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		formComposite.setLayoutData(gridData);
		
		enterNewDataPropertyButtonLabel = new Label(selfComposite, SWT.NONE);
		enterNewDataPropertyButton = new Button(selfComposite, SWT.PUSH);
		enterNewDataPropertyButton.setText("Enter a new property for data measurements about the subject");
		enterNewDataPropertyButton.addSelectionListener(this);
		new Label(selfComposite, SWT.NONE).setText(
				"These are properties that are measured, about the subject.  These DO change with time.  " +
				"Examples: 'stock price', 'annual Gross Domestic Product (GDP)'");
		
		enterNewSubjectPropertyButtonLabel = new Label(selfComposite, SWT.NONE);
		enterNewSubjectPropertyButton = new Button(selfComposite, SWT.PUSH);
		enterNewSubjectPropertyButton.setText("Enter a new, fixed property for the subject");
		enterNewSubjectPropertyButton.addSelectionListener(this);
		new Label(selfComposite, SWT.NONE).setText(
				"These are properties that identify the subject and generally do NOT change with time.  " +
				"Examples: 'has ticker symbol', 'has country code'");
		onEnterPageFromPrevious();
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering SubjectPropertiesPage...Control created?" + this.isCurrentPage());
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
		CsvImporter csvImporter = getCsvImporter();
		headers = csvImporter.getHeaders();
		for (Map<String, String> row: rowValues) {
			String uri = row.get(PropertyLookup.QUERY_HEADER_URI);
			String label = row.get(PropertyLookup.QUERY_HEADER_LABEL);
			String comment = row.get(PropertyLookup.QUERY_HEADER_COMMENT);
			addPropertyFormItem(uri, label, comment);
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
	
	private CsvImporter getCsvImporter() {
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		return loadCsvFileWizard.getCsvImporter();
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(enterNewDataPropertyButton)) {			
			log.info("Clicked 'new data property' button");
			CreateSubpropertyAction createSubpropertyAction = new CreateSubpropertyAction(
					selfComposite.getShell(), 
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
			addPropertyFormItem(newProperty.getURI(), newProperty.getLabel("EN"), newProperty.getComment("EN"));
		}
		
		if (clickedObject.equals(enterNewSubjectPropertyButton)) {			
			log.info("Clicked 'new subject property' button");
			CreateSubpropertyAction createSubpropertyAction = new CreateSubpropertyAction(
					selfComposite.getShell(), 
					Data.DATA_PROPERTY_DATASET_ROLE_ID, 
					RDF.SUBJECT_PROPERTY);
			
			//add the domain class
			OntModel ontModel = ModelFactory.createOntologyModel();
			OntClass domainSubject = ontModel.createClass(getSubjectUri());
			createSubpropertyAction.setDomainClass(domainSubject);
			
			createSubpropertyAction.run();
			OntResource newProperty = createSubpropertyAction.getOntResource();
			addPropertyFormItem(newProperty.getURI(), newProperty.getLabel("EN"), newProperty.getComment("EN"));
		}
	}

	/**
	 * add the new property to the form
	 * @param uri
	 * @param label
	 * @param comment
	 */
	protected abstract void addPropertyFormItem(String uri, String label, String comment);
}
