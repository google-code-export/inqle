package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.ui.rap.CreateSubclassAction;
import org.inqle.ui.rap.CreateSubpropertyAction;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvImporterWizard;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.widgets.DropdownFieldShower;
import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
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
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		
		formComposite = new Composite(selfComposite, SWT.NONE);
		gl = new GridLayout(3, false);
		formComposite.setLayout(gl);
		
		enterNewDataPropertyButtonLabel = new Label(formComposite, SWT.NONE);
		enterNewDataPropertyButton = new Button(formComposite, SWT.PUSH);
		enterNewDataPropertyButton.setText("Enter a new property for data measurements about the subject");
		enterNewDataPropertyButton.addSelectionListener(this);
		new Label(formComposite, SWT.NONE).setText(
				"These are properties that are measured, about the subject.  These DO change with time.  " +
				"Examples: 'stock price', 'annual Gross Domestic Product (GDP)'");
		
		enterNewSubjectPropertyButtonLabel = new Label(formComposite, SWT.NONE);
		enterNewSubjectPropertyButton = new Button(formComposite, SWT.PUSH);
		enterNewSubjectPropertyButton.setText("Enter a new, fixed property for the subject");
		enterNewSubjectPropertyButton.addSelectionListener(this);
		new Label(formComposite, SWT.NONE).setText(
				"These are properties that identify the subject and generally do NOT change with time.  " +
				"Examples: 'has ticker symbol', 'has country code'");
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering SubjectPropertyMappingsPage...");
		String currentSubjectClassUri = getSubjectUri();
		if (subjectClassUri == null || subjectClassUri.equals(currentSubjectClassUri)) {
			return;
		}
		subjectClassUri = currentSubjectClassUri;
		enterNewDataPropertyButtonLabel.setText(getEnterNewDataPropertyButtonLabel());
		enterNewSubjectPropertyButtonLabel.setText(getEnterNewSubjectPropertyButtonLabel());
		String propertiesXml = PropertyLookup.lookupAllDataProperties(
				subjectClassUri, 
				10, 
				0);
		Document propertiesDocument = XmlDocumentUtil.getDocument(propertiesXml);
		List<Map<String, String>> rowValues = SparqlXmlUtil.getRowValues(propertiesDocument);
		
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
